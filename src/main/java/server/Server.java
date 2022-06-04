package server;

import server.commands.Command;
import server.commands.ICommand;
import server.commands.NickCommand;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private ServerSocket server;
    private ArrayList<ConnectionHandler> connections;
    private HashMap<String, ICommand> registeredCommands;
    private ExecutorService threadPool;

    private int port;
    private boolean done;

    public Server() {
        this.connections = new ArrayList<>();

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

    public void broadcast(String message) {
        System.out.println(message);

        for (var connection : connections) {
            if (connection != null) {
                connection.sendMessage(message);
            }
        }
    }

    public void broadcastExcept(String message, ConnectionHandler except) {
        System.out.println(message);

        for (var connection : connections) {
            if (connection != null && connection != except) {
                connection.sendMessage(message);
            }
        }
    }

    public ICommand getCommand(String command) {
        command = command.replace("/", "");

        if (!registeredCommands.containsKey(command)) {
            return null;
        }

        return registeredCommands.get(command);
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
    }
}
