package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private Server server;
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    private String nickname;

    ConnectionHandler(Server server, Socket client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);

            sendMessage("Please enter a nickname: ");
            nickname = input.readLine();

            //TODO: Check if nick is good

            server.broadcast(nickname + " joined to the server");

            String message;
            while ((message = input.readLine()) != null) {
                //TODO: Serve commands

                server.broadcast("[" + nickname + "]: " + message);
            }
        }

        catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            input.close();
            output.close();

            if (!client.isClosed()) {
                client.close();
            }
        }

        catch (IOException ignored) { }
    }

    public void sendMessage(String message) {
        output.println(message);
    }
}
