package server.commands;

import server.ConnectionHandler;
import server.Server;

public class UserListCommand extends Command {
    public UserListCommand(Server serverHandler, String helpInfo) {
        super(serverHandler, helpInfo);
    }

    @Override
    protected String customBehaviour(ConnectionHandler client, String[] args) {
        var channel = client.getChannel();
        if (channel == null) {
            return "You are not in channel!";
        }

        return channel.printUserList();
    }
}