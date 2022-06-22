package common.server;

import common.RMIInterface;
import common.server.commands.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends UnicastRemoteObject implements RMIInterface {
    private final ArrayList<ConnectionHandler> connections;
    private final HashMap<String, Channel> channels;
    private final HashMap<String, ICommand> registeredCommands;

    public Server() throws RemoteException {
        super();

        this.connections = new ArrayList<>();
        this.registeredCommands = new HashMap<>();
        this.channels = new HashMap<>();

        registerCommands();
    }

    @Override
    public String init(String givenNickname) throws RemoteException {
        ConnectionHandler handler = new ConnectionHandler(this);

        String response;

        try {
            response = handler.init(givenNickname);

            if (!response.contains("Successfully joined to the server!")) {
                return response;
            }
        }

        catch (IOException exception) {
            exception.printStackTrace();

            return "An internal error has occured!";
        }

        connections.add(handler);
        return response;
    }

    @Override
    public String sendRequest(String nickname, String message) throws IOException {
        var connection = getConnectionFromNickname(nickname);
        if (connection == null) {
            return "You are not logged on to the server";
        }

        return connection.sendRequest(message);
    }

    @Override
    public String getLastMessage(String nickname) throws IOException {
        var connection = getConnectionFromNickname(nickname);
        if (connection == null) {
            return "You are not logged on to the server";
        }

        return connection.getLastMessage();
    }

    @Override
    public void shutdown(String nickname) throws RemoteException {
        var connection = getConnectionFromNickname(nickname);
        if (connection == null) {
            return;
        }

        connection.shutdown();
    }


    public ICommand getCommand(String command) {
        if (command == null) {
            return null;
        }

        command = command.replace("/", "");

        if (!registeredCommands.containsKey(command)) {
            return null;
        }

        return registeredCommands.get(command);
    }

    public boolean broadcast(ConnectionHandler sender, String message) {
        var channel = sender.getChannel();
        if (channel != null) {
            channel.broadcast(message);

            return true;
        }

        return false;
    }

    public boolean broadcastExceptSender(ConnectionHandler sender, String message) {
        var channel = sender.getChannel();
        if (channel != null) {
            channel.broadcastExcept(message, sender);

            return true;
        }

        return false;
    }

    public boolean registerChannel(Channel newChannel) {
        if (channels.containsKey(newChannel.getChannelName())) {
            return false;
        }

        channels.put(newChannel.getChannelName(), newChannel);
        return true;
    }

    public boolean unregisterChannel(Channel newChannel) {
        if (!channels.containsKey(newChannel.getChannelName())) {
            return false;
        }

        channels.remove(newChannel.getChannelName());
        return true;
    }

    public Channel getChannelFromName(String name) {
        if (!channels.containsKey(name)) {
            return null;
        }

        return channels.get(name);
    }

    public String printChannelList() {
        StringBuilder builder = new StringBuilder("List of available channels:\n");

        if (!channels.isEmpty()) {
            for (var channel : channels.values()) {
                String line = "- [" + channel.getChannelName() + "] Current users: " + channel.getNumberOfUsers() + "\n";

                builder.append(line);
            }
        }

        else builder.append("<No channels>");

        return builder.toString();
    }

    public String printCommands() {
        StringBuilder builder = new StringBuilder("List of commands:\n");

        for (var command : registeredCommands.entrySet()) {
            String line = "/" + command.getKey() + " - " + ((Command) command.getValue()).getHelpInfo() + "\n";

            builder.append(line);
        }

        return builder.toString();
    }

    public int getChannelsAmount() {
        return channels.size();
    }

    public ConnectionHandler getConnectionFromNickname(String nickname) {
        for (var connection : connections) {
            if (connection.getNickname() != null && connection.getNickname().equalsIgnoreCase(nickname)) {
                return connection;
            }
        }

        return null;
    }

    private void registerCommands() {
        registeredCommands.put("help", new HelpCommand(this, "Display list of commands"));
        registeredCommands.put("quit", new QuitCommand(this, "Quit the server"));

        registeredCommands.put("create_channel", new CreateChannelCommand(this, "Create new channel with given name and optionally password"));
        registeredCommands.put("remove_channel", new RemoveChannelCommand(this, "Remove channel with given name (owner command)"));

        registeredCommands.put("join", new JoinCommand(this, "Join to the channel with given name"));
        registeredCommands.put("leave", new LeaveCommand(this, "Leave the channel and back to the waiting room"));
        registeredCommands.put("kick", new KickCommand(this, "Kick the user from the channel (owner command)"));

        registeredCommands.put("channels", new ChannelListCommand(this, "Display the all current available channels"));
        registeredCommands.put("users", new UserListCommand(this, "Display the users in the current room you are in"));
    }
}
