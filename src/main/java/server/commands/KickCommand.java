package server.commands;

import server.ConnectionHandler;
import server.Server;

public class KickCommand extends Command {
    public KickCommand(Server serverHandler, String helpInfo) {
        super(serverHandler, helpInfo);
    }

    @Override
    protected String customBehaviour(ConnectionHandler client, String[] args) {
        if (args.length == 0) {
            return "Not enough arguments! You should pass the nickname of the person you are trying to throw out";
        }

        String nickname = args[0];

        var channel = client.getChannel();
        if (channel == null) {
            return "You are not in channel!";
        }

        try {
            var message = channel.kickUser(client, nickname);
            if (!message.isEmpty()) {
                return message;
            }

            serverHandler.broadcastExceptSender(client, nickname + " has been kicked from the channel by owner!");
            return "You have successfully kicked " + nickname + " from the channel!";
        }
        catch (IllegalAccessException exception) {
            return "You have not permission to execute this command!";
        }
    }
}