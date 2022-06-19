package server.commands;

import server.ConnectionHandler;
import server.Server;

public class HelpCommand extends Command {
    public HelpCommand(Server serverHandler, String helpInfo) {
        super(serverHandler, helpInfo);
    }

    @Override
    protected String customBehaviour(ConnectionHandler client, String[] args) {
        return serverHandler.printCommands();
    }
}