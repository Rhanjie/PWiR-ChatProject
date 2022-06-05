package server.commands;

import server.Channel;
import server.ConnectionHandler;
import server.Server;
import server.Validator;

public class RemoveChannelCommand extends Command {
    public RemoveChannelCommand(Server serverHandler) {
        super(serverHandler);
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

        if (!channel.isChannelOwner(client)) {
            return "You have not permission to execute this command!";
        }

        serverHandler.broadcastExceptSender(client,"Current channel has been deleted. Switched to waiting room");

        try { channel.closeChannel(client); }
        catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }

        serverHandler.unregisterChannel(channel);
        client.setChannel(null);

        return "Successfully removed the channel!";
    }
}
