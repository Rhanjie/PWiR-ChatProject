package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private ServerSocket server;
    private ArrayList<ConnectionHandler> connections;
    private ExecutorService threadPool;

    private int port = 25565;
    private boolean done = false;

    public Server() {
        this.connections = new ArrayList<>();

        Random random = new Random();
        port = random.nextInt(10000) + 10000;

        //TODO: Only for tests
        port = 25565;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            threadPool = Executors.newCachedThreadPool();

            System.out.println("Server is started at the port " + port);

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
        for (var connection : connections) {
            if (connection != null) {
                connection.sendMessage(message);
            }
        }
    }

    private void shutdown() {
        try {
            done = true;

            if (!server.isClosed()) {
                server.close();
            }

            for (var connection : connections) {
                connection.shutdown();
            }
        }

        catch (IOException ignored) { }
    }
}
