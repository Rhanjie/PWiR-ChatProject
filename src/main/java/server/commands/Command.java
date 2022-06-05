package server.commands;

import server.ConnectionHandler;
import server.Server;

public abstract class Command implements ICommand {
    protected final Server serverHandler;

    Command(Server serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    public String execute(ConnectionHandler client, String[] args) {
        return customBehaviour(client, args);
    }

    public abstract String customBehaviour(ConnectionHandler client, String[] args);
}
