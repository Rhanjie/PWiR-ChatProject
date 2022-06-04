package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;

    private boolean done = false;

    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 25565);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);

            InputHandler handler = new InputHandler(this);
            Thread thread = new Thread(handler);
            thread.start();

            String message;
            while ((message = input.readLine()) != null) {
                System.out.println(message);
            }
        }

        catch (IOException exception) {
            System.out.println("Cannot connect to the server!");

            shutdown();
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public void shutdown() {
        try {
            done = true;

            if (input != null) {
                input.close();
            }

            if (output != null) {
                output.close();
            }

            if (client != null && !client.isClosed()) {
                client.close();
            }
        }

        catch (IOException ignored) { }
    }

    public boolean isDone() {
        return done;
    }
}
