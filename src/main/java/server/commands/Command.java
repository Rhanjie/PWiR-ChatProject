package server.commands;

import server.ConnectionHandler;
import server.Server;

public abstract class Command implements ICommand {
    public enum Access { MEMBER, ADMIN }

    protected final Server serverHandler;
    public final Access access;

    Command(Server serverHandler, Access access) {
        this.serverHandler = serverHandler;
        this.access = access;
    }

    @Override
    public String execute(ConnectionHandler client, String[] args) {
        if (!checkPermission(client)) {
            return "You do not have permissions to execute this command!";
        }

        return customBehaviour(client, args);
    }

    private boolean checkPermission(ConnectionHandler client) {
        //TODO: Check permissions

        return true;
    }

    public abstract String customBehaviour(ConnectionHandler client, String[] args);
}
