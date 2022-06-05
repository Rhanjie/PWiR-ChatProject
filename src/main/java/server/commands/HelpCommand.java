package server.commands;

import server.ConnectionHandler;
import server.Server;

public class HelpCommand extends Command {
    public HelpCommand(Server serverHandler) {
        super(serverHandler);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        var channel = client.getChannel();
        if (channel == null) {
            return "You are not in channel!";
        }

        return channel.printUserList();
    }
}