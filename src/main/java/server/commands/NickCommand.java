package server.commands;

import server.ConnectionHandler;
import server.Server;
import server.Validator;

public class NickCommand extends Command {
    public NickCommand(Server serverHandler, String helpInfo) {
        super(serverHandler, helpInfo);
    }

    @Override
    public String customBehaviour(ConnectionHandler client, String[] args) {
        String nickname = String.join(" ", args);
        String message = Validator.validateNickname(nickname, serverHandler);

        if (!message.isEmpty()) {
            return message;
        }

        message = client.getNickname() + " renamed themselves to " + nickname;
        serverHandler.broadcastExceptSender(client, message);

        client.setNickname(nickname);

        return "Successfully changed the nickname!";
    }
}
