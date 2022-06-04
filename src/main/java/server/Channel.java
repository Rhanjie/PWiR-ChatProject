package server;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Channel {
    private ArrayList<ConnectionHandler> users;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
}
