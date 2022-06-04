package server.commands;

import server.ConnectionHandler;
import server.Server;

public class QuitCommand extends Command {
    public QuitCommand(Server serverHandler, Access access) {
        super(serverHandler, access);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        client.shutdown();

        serverHandler.broadcast("User " + client.getNickname() + " left the server!");

        return "";
    }
}
