package server.commands;

import server.ConnectionHandler;
import server.Server;

public class KickCommand extends Command {
    public KickCommand(Server serverHandler) {
        super(serverHandler);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        if (args.length < 2) {
            return "Not enough arguments! You should pass channel name and optionally the password";
        }

        String channelName = args[0];
        String nickname = args[1];

        var channel = serverHandler.getChannelFromName(channelName);
        if (channel == null) {
            return "Channel with that name is not exist!";
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