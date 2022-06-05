package server;

import server.commands.*;

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

    private HashMap<String, Channel> channels;
    private HashMap<String, ICommand> registeredCommands;
    private ExecutorService threadPool;

    private int port;
    private boolean done;

    public Server() {
        this.connections = new ArrayList<>();
        this.channels = new HashMap<>();

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
        if (channels.containsKey(newChannel.getChannelName())) {
            return false;
        }

        channels.put(newChannel.getChannelName(), newChannel);

        return true;
    }

    public Channel getChannelFromName(String name) {
        if (!channels.containsKey(name)) {
            return null;
        }

        return channels.get(name);
    }

    public boolean unregisterChannel(Channel newChannel) {
        if (!channels.containsKey(newChannel.getChannelName())) {
            return false;
        }

        channels.remove(newChannel.getChannelName());

        return true;
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

        registeredCommands.put("nick", new NickCommand(this));
        registeredCommands.put("quit", new QuitCommand(this));

        registeredCommands.put("create_channel", new CreateChannelCommand(this));
        registeredCommands.put("remove_channel", new RemoveChannelCommand(this));

        registeredCommands.put("join", new JoinCommand(this));
        registeredCommands.put("leave", new LeaveCommand(this));
        registeredCommands.put("kick", new KickCommand(this));

        //TODO: Missing commands
        //ban ip (admin)
        //help
        //list of current channels
        //list of current users in channel
        //list of all users in the server
    }
}
