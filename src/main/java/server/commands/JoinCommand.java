package server.commands;

import server.Channel;
import server.ConnectionHandler;
import server.Server;
import server.Validator;

public class JoinCommand extends Command {
    public JoinCommand(Server serverHandler, String helpInfo) {
        super(serverHandler, helpInfo);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        if (args.length == 0) {
            return "Not enough arguments! You should pass channel name and optionally the password";
        }

        String channelName = args[0];
        String password = (args.length >= 2) ? args[1] : "";

        var channel = serverHandler.getChannelFromName(channelName);
        if (channel == null) {
            return "Channel with that name is not exist!";
        }

        var currentChannel = client.getChannel();
        if (currentChannel != null) {
            if (currentChannel == channel) {
                return "You are already on this channel!";
            }

            currentChannel.attemptToLeave(client);
        }

        return channel.attemptToJoin(client, password);
    }
}