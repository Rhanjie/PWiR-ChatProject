package server.commands;

import server.Channel;
import server.ConnectionHandler;
import server.Server;

public class QuitCommand extends Command {
    public QuitCommand(Server serverHandler) {
        super(serverHandler);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        var channel = client.getChannel();
        if (channel != null) {
            channel.attemptToLeave(client);
        }
        
        client.shutdown();

        serverHandler.broadcast(client, client.getNickname() + " left the server!");

        return "";
    }
}
