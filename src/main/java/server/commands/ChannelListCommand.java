package server.commands;

import server.ConnectionHandler;
import server.Server;

public class ChannelListCommand extends Command {
    public ChannelListCommand(Server serverHandler) {
        super(serverHandler);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        return serverHandler.printChannelList();
    }
}