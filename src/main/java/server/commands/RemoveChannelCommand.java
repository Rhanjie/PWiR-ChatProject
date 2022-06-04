package server.commands;

import server.Channel;
import server.ConnectionHandler;
import server.Server;
import server.Validator;

public class RemoveChannelCommand extends Command {
    public RemoveChannelCommand(Server serverHandler, Access access) {
        super(serverHandler, access);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        if (args.length == 0) {
            return "Not enough arguments! You should pass channel name";
        }

        String channelName = args[0];

        Channel channel = serverHandler.getChannelFromName(channelName);
        if (channel == null) {
            return "Channel with that name is not exist!";
        }

        //TODO: Check permissions

        serverHandler.unregisterChannel(channel);

        return "Successfully removed the channel!";
    }
}
