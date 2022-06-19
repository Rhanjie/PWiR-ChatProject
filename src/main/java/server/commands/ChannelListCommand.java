package server.commands;

import server.ConnectionHandler;
import server.Server;

public class ChannelListCommand extends Command {
    public ChannelListCommand(Server serverHandler, String helpInfo) {
        super(serverHandler, helpInfo);
    }

    @Override
    protected String customBehaviour(ConnectionHandler client, String[] args) {
        return serverHandler.printChannelList();
    }
}