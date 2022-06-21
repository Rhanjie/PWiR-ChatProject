package common.server.commands;

import common.server.Channel;
import common.server.ConnectionHandler;
import common.server.Server;
import common.server.Validator;

public class CreateChannelCommand extends Command {
    public CreateChannelCommand(Server serverHandler, String helpInfo) {
        super(serverHandler, helpInfo);
    }

    @Override
    protected String customBehaviour(ConnectionHandler client, String[] args) {
        if (args.length == 0) {
            return "Not enough arguments! You should pass channel name and optionally the password";
        }

        String channelName = args[0];
        String password = (args.length >= 2) ? args[1] : "";

        String message = Validator.validateChannelName(channelName, serverHandler);
        if (!message.isEmpty()) {
            return message;
        }

        Channel channel = new Channel(channelName, password, client);
        if (!serverHandler.registerChannel(channel)) {
            return "Channel with that name is already exists!";
        }

        var currentChannel = client.getChannel();
        if (currentChannel != null) {
            currentChannel.attemptToLeave(client, serverHandler);
        }

        channel.attemptToJoin(client, password);

        return "Successfully created the channel!";
    }
}
