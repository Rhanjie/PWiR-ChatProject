package common.server;

import common.server.commands.ICommand;

import java.io.IOException;
import java.util.PriorityQueue;

public class ConnectionHandler {
    private final Server server;

    private Channel channel;
    private String nickname;

    private PriorityQueue<String> messages = new PriorityQueue<>();

    public ConnectionHandler(Server server) {
        this.server = server;
    }

    public String init(String givenNickname) throws IOException {
        String message = Validator.validateNickname(nickname, server);
        boolean isNicknameValid = message.isEmpty();

        if (!isNicknameValid) {
            return message;
        }

        nickname = givenNickname;

        return """
                    Successfully joined to the server!
                    
                    You are currently in the waiting room. To select a room, type /join <name>
                    To display available commands, type /help
                    """;
    }

    public String sendRequest(String message) throws IOException {
        boolean isCommand = message.startsWith("/");

        if (isCommand) {
            String[] splittedMessage = message.split(" ", 2);
            String[] args = (splittedMessage.length == 2)
                    ? splittedMessage[1].split(" ")
                    : new String[0];

            ICommand command = server.getCommand(splittedMessage[0]);
            if (command == null) {
                return "Unknown command!";
            }

            String output = command.execute(this, args);
            if (!output.isEmpty()) {
                return output;
            }

            return "Command executed!";
        }

        if (!server.broadcast(this, nickname + ": " + message)) {
            return "You are not in channel!";
        }

        return "Message sent!";
    }

    public void sendMessage(String message) {
        messages.add(message);
    }

    public String getLastMessage() {
        return messages.poll();
    }

    public void shutdown() {
        //TODO
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
