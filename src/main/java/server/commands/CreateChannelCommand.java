package server.commands;

import server.Channel;
import server.ConnectionHandler;
import server.Server;
import server.Validator;

public class CreateChannelCommand extends Command {
    public CreateChannelCommand(Server serverHandler, Access access) {
        super(serverHandler, access);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        if (args.length == 0) {
            return "Not enough arguments! You should pass channel name and optionally the password";
        }

        String channelName = args[0];
        String password = (args.length >= 2) ? args[1] : "";

        String message = Validator.validateChannelName(channelName);
        if (!message.isEmpty()) {
            return message;
        }

        Channel channel = new Channel(channelName, password, client);
        if (!serverHandler.registerChannel(channel)) {
            return "Problem with creating the channel!";
        }

        return "Successfully created the channel!";
    }
}
