package server.commands;

import server.ConnectionHandler;
import server.Server;

public class QuitCommand extends Command {
    public QuitCommand(Server serverHandler, Access access) {
        super(serverHandler, access);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        return "TODO: Not implemented yet";
    }
}
