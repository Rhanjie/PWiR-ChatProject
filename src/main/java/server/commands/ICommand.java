package server.commands;

import server.ConnectionHandler;

public interface ICommand {
    public String execute(ConnectionHandler client, String[] args);
}
