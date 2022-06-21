package common.server.commands;

import common.server.ConnectionHandler;

public interface ICommand {
    public String execute(ConnectionHandler client, String[] args);
}
