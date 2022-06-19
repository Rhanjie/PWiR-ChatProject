package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChannelTest {
    Channel channel1;
    Channel channel2;

    @BeforeEach
    void setUp() {
        channel1 = new Channel("test", "", mock(ConnectionHandler.class));
        channel2 = new Channel("kolejny kanal", "123 ABc", mock(ConnectionHandler.class));
    }

    @Test @Order(1)
    void attemptToJoin() {
        String expected = "You have successfully joined the channel #test";
        String actual = channel1.attemptToJoin(mock(ConnectionHandler.class), "");
        assertEquals(expected, actual);

        actual = channel1.attemptToJoin(mock(ConnectionHandler.class), "432!WQEre");
        assertNotEquals(expected, actual);

        expected = "You have successfully joined the channel #kolejny kanal";
        actual = channel2.attemptToJoin(mock(ConnectionHandler.class), "123 ABc");
        assertEquals(expected, actual);

        actual = channel2.attemptToJoin(mock(ConnectionHandler.class), "123 aBc");
        assertNotEquals(expected, actual);

        actual = channel2.attemptToJoin(mock(ConnectionHandler.class), "");
        assertNotEquals(expected, actual);
    }

    @Test @Order(2)
    void attemptToLeave() {
        String expected = "You have successfully left the channel!";
        ConnectionHandler client = new ConnectionHandler(mock(Server.class), mock(Socket.class));
        ConnectionHandler clientNotInChannel = new ConnectionHandler(mock(Server.class), mock(Socket.class));
        channel2.attemptToJoin(client, "123 ABc");

        //Client is not in the channel
        String actual = channel2.attemptToLeave(clientNotInChannel, mock(Server.class));
        assertNotEquals(expected, actual);

        actual = channel2.attemptToLeave(client, mock(Server.class));
        assertEquals(expected, actual);

        //Client left the channel earlier
        actual = channel2.attemptToLeave(clientNotInChannel, mock(Server.class));
        assertNotEquals(expected, actual);
    }

    @Test @Order(3)
    void isNotChannelOwner() {
        ConnectionHandler owner = new ConnectionHandler(mock(Server.class), mock(Socket.class));
        ConnectionHandler clientWithoutPermissions = new ConnectionHandler(mock(Server.class), mock(Socket.class));

        channel1 = new Channel("test", "", owner);

        assertFalse(channel1.isNotChannelOwner(owner));
        assertTrue(channel1.isNotChannelOwner(clientWithoutPermissions));
    }

    @Test @Order(4)
    void closeChannel() {
        ConnectionHandler owner = new ConnectionHandler(mock(Server.class), mock(Socket.class));
        ConnectionHandler clientWithoutPermissions = new ConnectionHandler(mock(Server.class), mock(Socket.class));

        channel1 = new Channel("test", "", owner);

        assertThrows(IllegalAccessException.class, () -> {
            channel1.closeChannel(clientWithoutPermissions);
        });

        assertDoesNotThrow(() -> {
            channel1.closeChannel(owner);
        });
    }

    @Test @Order(5)
    void kickUser() throws IllegalAccessException {
        ConnectionHandler owner = new ConnectionHandler(mock(Server.class), mock(Socket.class));
        ConnectionHandler member = spy(new ConnectionHandler(mock(Server.class), mock(Socket.class)));

        owner.setNickname("owner");
        member.setNickname("member");

        channel1 = spy(new Channel("test", "", owner));

        //Ignore problematic methods' behaviour
        doNothing().when(channel1).broadcast(any());
        doNothing().when(member).sendMessage(any());

        channel1.attemptToJoin(owner, "");
        channel1.attemptToJoin(member, "");

        //Case: Without permissions
        assertThrows(IllegalAccessException.class, () -> {
            channel1.kickUser(member, owner.getNickname());
        });

        String expected = "";

        //Case: Kick yourself
        String actual = channel1.kickUser(owner, owner.getNickname());
        assertNotEquals(expected, actual);

        //Case: Correct kick
        actual = channel1.kickUser(owner, member.getNickname());
        assertEquals(expected, actual);

        //Case: Kick the client that already kicked
        actual = channel1.kickUser(owner, member.getNickname());
        assertNotEquals(expected, actual);
    }
}