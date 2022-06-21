package common.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ServerTest {
    private final Server server = new Server();

    ServerTest() throws RemoteException {}

    @BeforeEach
    void setUp() {
    }

    @Test @Order(1)
    void getCommand() {
        assertNotNull(server.getCommand("help"));
        assertNotNull(server.getCommand("create_channel"));
        assertNotNull(server.getCommand("kick"));

        assertNull(server.getCommand("nick"));
        assertNull(server.getCommand("abc"));
        assertNull(server.getCommand(""));
        assertNull(server.getCommand(null));
    }

    @Test @Order(2)
    void registerChannel() {
        Channel channel1 = new Channel("test1", "", mock(ConnectionHandler.class));
        Channel channel2 = new Channel("test2", "", mock(ConnectionHandler.class));

        assertTrue(server.registerChannel(channel1));
        assertTrue(server.registerChannel(channel2));
        assertFalse(server.registerChannel(channel1));
    }

    @Test @Order(3)
    void unregisterChannel() {
        Channel channel1 = new Channel("test1", "", mock(ConnectionHandler.class));
        Channel channel2 = new Channel("test2", "", mock(ConnectionHandler.class));
        Channel channel3 = new Channel("test3", "", mock(ConnectionHandler.class));

        server.registerChannel(channel1);
        server.registerChannel(channel2);

        int expected = 2;
        int actual = server.getChannelsAmount();

        assertEquals(expected, actual);

        server.unregisterChannel(channel2);

        expected -= 1;
        actual = server.getChannelsAmount();
        assertEquals(expected, actual);

        server.unregisterChannel(channel2);
        server.unregisterChannel(channel3);
        assertEquals(expected, actual);

        server.unregisterChannel(channel1);

        expected -= 1;
        actual = server.getChannelsAmount();
        assertEquals(expected, actual);
    }

    @Test @Order(4)
    void getChannelFromName() {
        Channel channel1 = new Channel("test1", "", mock(ConnectionHandler.class));
        Channel channel2 = new Channel("test2", "", mock(ConnectionHandler.class));
        Channel channel3 = new Channel("test3", "", mock(ConnectionHandler.class));

        server.registerChannel(channel1);
        server.registerChannel(channel2);

        assertNotNull(server.getChannelFromName("test1"));
        assertNotNull(server.getChannelFromName("test2"));
        assertNull(server.getChannelFromName("test3"));
    }

    @Test @Order(5)
    void broadcast() {
        Channel channel = new Channel("test", "", mock(ConnectionHandler.class));

        server.registerChannel(channel);

        assertFalse(server.broadcast(mock(ConnectionHandler.class), "Test message"));
    }

    @Test @Order(6)
    void printChannelList() {
        String actual = server.printChannelList();
        String expected = "List of available channels:\n<No channels>";
        assertEquals(expected, actual);

        Channel channel = new Channel("test", "", mock(ConnectionHandler.class));

        server.registerChannel(channel);

        actual = server.printChannelList();
        assertEquals(0, channel.getNumberOfUsers());

        expected = "List of available channels:\n- [test] Current users: 0\n";
        assertEquals(expected, actual);

        channel.attemptToJoin(mock(ConnectionHandler.class), "");

        actual = server.printChannelList();
        expected = "List of available channels:\n- [test] Current users: 1\n";
        assertEquals(expected, actual);
    }
}