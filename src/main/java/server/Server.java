package server;

import server.commands.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private ServerSocket server;
    private ExecutorService threadPool;

    private final ArrayList<ConnectionHandler> connections;
    private final HashMap<String, Channel> channels;
    private final HashMap<String, ICommand> registeredCommands;

    private final int port = 20000;
    private boolean done = false;

    public Server() {
        this.connections = new ArrayList<>();
        this.registeredCommands = new HashMap<>();
        this.channels = new HashMap<>();

        registerCommands();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            threadPool = Executors.newCachedThreadPool();

            System.out.println("Server is started at the port #" + port);

            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(this, client);

                connections.add(handler);
                threadPool.execute(handler);
            }
        }

        catch (IOException exception) {
            shutdown();
        }
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

    public void unregisterChannel(Channel newChannel) {
        if (!channels.containsKey(newChannel.getChannelName())) {
            return;
        }

        channels.remove(newChannel.getChannelName());
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

    private void shutdown() {
        try {
            done = true;

            threadPool.shutdown();
            if (!server.isClosed()) {
                server.close();
            }

            for (var connection : connections) {
                connection.shutdown();
            }
        }

        catch (IOException ignored) { }
    }

    private void registerCommands() {
        registeredCommands.put("help", new HelpCommand(this, "Display list of commands"));
        registeredCommands.put("nick", new NickCommand(this, "Change nick"));
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
