package server;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Channel {
    private final String channelName;
    private final String password;

    private final ConnectionHandler owner;
    private final ArrayList<ConnectionHandler> users;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Channel(String channelName, String password, ConnectionHandler client) {
        this.channelName = channelName;
        this.password = password;

        users = new ArrayList<>();
        users.add(client);

        client.setChannel(this);
        owner = client;
    }

    public String attemptToJoin(ConnectionHandler client, String givenPassword) {
        if (!password.equals(givenPassword)) {
            return "Given password is wrong!";
        }

        for (ConnectionHandler user : users) {
            if (user.equals(client)) {
                return "You are already on this channel!";
            }
        }

        broadcast(client.getNickname() + " joined the channel");

        users.add(client);
        client.setChannel(this);

        return "You have successfully joined the channel #" + channelName;
    }

    public String attemptToLeave(ConnectionHandler client, Server serverHandler) {
        for (ConnectionHandler user : users) {
            if (user.equals(client)) {
                users.remove(user);
                client.setChannel(null);

                //Remove channel if empty
                if (users.size() == 0) {
                    try { closeChannel(owner); }
                    catch (IllegalAccessException exception) {
                        exception.printStackTrace();
                    }

                    serverHandler.unregisterChannel(this);
                }

                else broadcast(client.getNickname() + " left the channel");

                return "You have successfully left the channel!";
            }
        }

        return "You are not on this channel!";
    }

    public void closeChannel(ConnectionHandler client) throws IllegalAccessException {
        if (isNotChannelOwner(client)) {
            throw new IllegalAccessException("You do not have permission to execute this command!");
        }

        for (ConnectionHandler user : users) {
            user.setChannel(null);
        }
    }

    public String kickUser(ConnectionHandler client, String nickname) throws IllegalAccessException {
        if (isNotChannelOwner(client)) {
            throw new IllegalAccessException("You do not have permission to execute this command!");
        }

        for (ConnectionHandler user : users) {
            if (user.getNickname().equals(nickname)) {
                if (user == client) {
                    return "You cannot kick yourself! Use /leave command instead";
                }

                users.remove(user);

                user.setChannel(null);
                user.sendMessage("You have been kicked from the channel by owner!");

                return "";
            }
        }

        return "User " + nickname + " is not in this channel!";
    }

    public boolean isNotChannelOwner(ConnectionHandler client) {
        return client != owner;
    }

    public void broadcast(String message) {
        String currentTime = formatter.format(LocalTime.now());

        System.out.println(message);

        for (var connection : users) {
            if (connection != null) {
                connection.sendMessage("[" + currentTime + "] " + message);
            }
        }
    }

    public void broadcastExcept(String message, ConnectionHandler except) {
        String currentTime = formatter.format(LocalTime.now());

        System.out.println(message);

        for (var connection : users) {
            if (connection != null && connection != except) {
                connection.sendMessage("[" + currentTime + "] " + message);
            }
        }
    }

    public String printUserList() {
        StringBuilder builder = new StringBuilder("Number of users in this channel: " + getNumberOfUsers() + "\n[ ");

        if (!users.isEmpty()) {
            for (var user : users) {
                String line = "'" + user.getNickname() + "' ";

                builder.append(line);
            }
        }

        builder.append("]");

        return builder.toString();
    }

    public String getChannelName() {
        return channelName;
    }

    public int getNumberOfUsers() {
        return users.size();
    }
}
