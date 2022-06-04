package server.commands;

import server.ConnectionHandler;
import server.Server;

public class NickCommand extends Command {
    public NickCommand(Server serverHandler, Access access) {
        super(serverHandler, access);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        var nickname = String.join(" ", args);

        //TODO: validateNickname

        serverHandler.broadcastExcept(client.getNickname() + " renamed themselves to " + nickname, client);

        client.setNickname(nickname);

        return "Successfully changed the nickname!";
    }
}
