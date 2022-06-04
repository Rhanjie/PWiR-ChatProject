package server;

import server.commands.Command;
import server.commands.ICommand;
import server.commands.NickCommand;
import server.commands.QuitCommand;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private ServerSocket server;
    private ArrayList<ConnectionHandler> connections;
    private ArrayList<Channel> channels;

    private HashMap<String, ICommand> registeredCommands;
    private ExecutorService threadPool;

    private int port;
    private boolean done;

    public Server() {
        this.connections = new ArrayList<>();
        this.channels = new ArrayList<>();

        Random random = new Random();
        port = random.nextInt(10000) + 10000;

        //TODO: Only for tests
        port = 25565;
        done = false;

        registerCommands();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            threadPool = Executors.newCachedThreadPool();

            System.out.println("Server is started at the port #" + port);

            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(this, client);

                connections.add(handler);
                threadPool.execute(handler);
            }
        }

        catch (IOException exception) {
            shutdown();
        }
    }

    public ICommand getCommand(String command) {
        command = command.replace("/", "");

        if (!registeredCommands.containsKey(command)) {
            return null;
        }

        return registeredCommands.get(command);
    }

    public boolean broadcast(ConnectionHandler sender, String message) {
        var channel = sender.getChannel();
        if (channel != null) {
            channel.broadcast(message);

            return true;
        }

        return false;
    }

    public boolean broadcastExceptSender(ConnectionHandler sender, String message) {
        var channel = sender.getChannel();
        if (channel != null) {
            channel.broadcastExcept(message, sender);

            return true;
        }

        return false;
    }

    public boolean registerChannel(Channel newChannel) {
        for (Channel channel : channels) {
            if (channel.equals(newChannel)) {
                return false;
            }
        }

        channels.add(newChannel);

        return true;
    }

    public boolean unregisterChannel(Channel newChannel) {
        for (Channel channel : channels) {
            if (channel.equals(newChannel)) {
                channels.remove(channel);

                return true;
            }
        }

        return false;
    }


    private void shutdown() {
        try {
            done = true;

            threadPool.shutdown();
            if (!server.isClosed()) {
                server.close();
            }

            for (var connection : connections) {
                connection.shutdown();
            }
        }

        catch (IOException ignored) { }
    }

    private void registerCommands() {
        registeredCommands = new HashMap<>();

        registeredCommands.put("nick", new NickCommand(this, Command.Access.MEMBER));
        registeredCommands.put("quit", new QuitCommand(this, Command.Access.MEMBER));
    }
}
