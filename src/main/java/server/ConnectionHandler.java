package server;

import server.commands.ICommand;

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

            boolean isNicknameValid = false;
            while (!isNicknameValid) {
                sendMessage("Please enter a nickname: ");
                nickname = input.readLine();

                isNicknameValid = validateNickname();
            }

            server.broadcast(nickname + " joined to the server");

            String message;
            while ((message = input.readLine()) != null) {
                boolean isCommand = message.startsWith("/");

                if (isCommand) {
                    String[] splittedMessage = message.split(" ", 2);
                    String[] args = (splittedMessage.length == 2)
                            ? splittedMessage[1].split(" ")
                            : new String[0];

                    ICommand command = server.getCommand(splittedMessage[0]);
                    if (command == null) {
                        sendMessage("Unknown command!");

                        continue;
                    }

                    String output = command.execute(this, args);

                    sendMessage(output);
                }

                else server.broadcast("[" + nickname + "]: " + message);
            }
        }

        catch (IOException exception) {
            shutdown();
        }
    }

    public void sendMessage(String message) {
        output.println(message);
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    //TODO: Move to validator class
    private boolean validateNickname() {
        if (nickname == null || nickname.isEmpty()) {
            sendMessage("[ERROR] You did not enter a nickname!");

            return false;
        }

        if (nickname.length() > 10) {
            sendMessage("[ERROR] Given nickname is too long!");

            return false;
        }

        if (!nickname.matches("[a-zA-Z0-9]*")) {
            sendMessage("[ERROR] The nickname can only contain letters and numbers!");

            return false;
        }

        return true;
    }
}
