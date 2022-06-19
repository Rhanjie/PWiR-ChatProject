package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ServerTest {
    private final Server server = new Server();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getCommand() {
    }

    @Test
    void broadcast() {
    }

    @Test
    void broadcastExceptSender() {
    }

    @Test
    void registerChannel() {
    }

    @Test
    void getChannelFromName() {
    }

    @Test
    void printChannelList() {
        String actual = server.printChannelList();
        String expected = "List of available channels:\n<No channels>";
        assertEquals(expected, actual);

        Channel channel = new Channel("test", "", mock(ConnectionHandler.class));

        server.registerChannel(channel);

        actual = server.printChannelList();
        assertEquals(1, channel.getNumberOfUsers());

        expected = "List of available channels:\n- [test] Current users: 1\n";
        assertEquals(expected, actual);
    }

    @Test
    void printCommands() {
    }

    @Test
    void unregisterChannel() {
    }

    @Test
    void getConnectionFromNickname() {
    }
}