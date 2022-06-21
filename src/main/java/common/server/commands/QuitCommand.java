package common.server.commands;

import common.server.ConnectionHandler;
import common.server.Server;

public class QuitCommand extends Command {
    public QuitCommand(Server serverHandler, String helpInfo) {
        super(serverHandler, helpInfo);
    }

    @Override
    protected String customBehaviour(ConnectionHandler client, String[] args) {
        var channel = client.getChannel();
        if (channel != null) {
            channel.attemptToLeave(client, serverHandler);
        }
        
        client.shutdown();

        serverHandler.broadcast(client, client.getNickname() + " left the server!");

        return "";
    }
}
