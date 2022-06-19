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

    private Channel channel;
    private String nickname;

    ConnectionHandler(Server server, Socket client) {
        this.server = server;
        this.client = client;

        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
        }

        catch (IOException exception) {
            shutdown();
        }
    }

    @Override
    public void run() {
        try {
            init();

            serveClientRequests();
        }

        catch (IOException exception) {
            shutdown();
        }
    }

    private void init() throws IOException {
        String givenNickname = "";

        boolean isNicknameValid = false;
        while (!isNicknameValid) {
            sendMessage("Please enter a nickname: ");
            givenNickname = input.readLine();

            String message = Validator.validateNickname(givenNickname, server);
            isNicknameValid = message.isEmpty();

            if (!message.isEmpty()) {
                sendMessage(message);
            }
        }

        nickname = givenNickname;

        sendMessage("""
                    Successfully joined to the server!
                    
                    You are currently in the waiting room. To select a room, type /join <name>
                    To display available commands, type /help
                    """);

        sendMessage(server.printChannelList());
    }

    private void serveClientRequests() throws IOException {
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
                if (!output.isEmpty()) {
                    sendMessage(output);
                }

                continue;
            }

            if (!server.broadcast(this, nickname + ": " + message)) {
                sendMessage("You are not in channel!");
            }
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

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }
}
