package server.commands;

import server.Channel;
import server.ConnectionHandler;
import server.Server;
import server.Validator;

public class JoinCommand extends Command {
    public JoinCommand(Server serverHandler) {
        super(serverHandler);
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

        return channel.attemptToJoin(client, password);
    }
}