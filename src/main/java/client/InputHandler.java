package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputHandler implements Runnable {
    Client client;

    InputHandler(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            while (!client.isDone()) {
                String message = input.readLine();

                //TODO: Serve the commands

                client.sendMessage(message);
            }
        }

        catch (IOException exception) {
            client.shutdown();
        }
    }
}
