package common.server.commands;

import common.server.ConnectionHandler;
import common.server.Server;

public class ChannelListCommand extends Command {
    public ChannelListCommand(Server serverHandler, String helpInfo) {
        super(serverHandler, helpInfo);
    }

    @Override
    protected String customBehaviour(ConnectionHandler client, String[] args) {
        return serverHandler.printChannelList();
    }
}