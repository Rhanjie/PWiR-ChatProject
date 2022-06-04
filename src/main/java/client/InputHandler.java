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
                client.sendMessage(message);

                if (message.equals("/quit")) {
                    input.close();

                    client.shutdown();
                }
            }
        }

        catch (IOException exception) {
            client.shutdown();
        }
    }
}
