package common.server.commands;

import common.server.ConnectionHandler;
import common.server.Server;

public abstract class Command implements ICommand {
    protected final Server serverHandler;
    private final String helpInfo;

    Command(Server serverHandler, String helpInfo) {
        this.serverHandler = serverHandler;
        this.helpInfo = helpInfo;
    }

    @Override
    public String execute(ConnectionHandler client, String[] args) {
        return customBehaviour(client, args);
    }

    public String getHelpInfo() {
        return helpInfo;
    }

    protected abstract String customBehaviour(ConnectionHandler client, String[] args);
}
