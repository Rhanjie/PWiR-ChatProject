package server;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Channel {
    private String channelName;
    private String password;

    private final ArrayList<ConnectionHandler> users;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Channel(String channelName, String password, ConnectionHandler owner) {
        this.channelName = channelName;
        this.password = password;

        users = new ArrayList<>();
        users.add(owner);

        owner.setChannel(this);
    }

    public void closeChannel() {
        for (ConnectionHandler user : users) {
            user.setChannel(null);
        }
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

        users.add(client);
        client.setChannel(this);

        return "You have successfully joined to the channel #" + channelName;
    }

    public String attemptToLeave(ConnectionHandler client) {
        for (ConnectionHandler user : users) {
            if (user.equals(client)) {
                users.remove(user);
                client.setChannel(null);

                return "You have successfully left the channel!";
            }
        }

        return "You are not on this channel!";
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

    public String getChannelName() {
        return channelName;
    }

}
