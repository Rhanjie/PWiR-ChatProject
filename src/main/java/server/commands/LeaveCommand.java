package server.commands;

import server.Channel;
import server.ConnectionHandler;
import server.Server;

public class LeaveCommand extends Command {
    public LeaveCommand(Server serverHandler) {
        super(serverHandler);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        var channel = client.getChannel();
        if (channel == null) {
            return "You are not in channel!";
        }
        
        return channel.attemptToLeave(client);
    }
}