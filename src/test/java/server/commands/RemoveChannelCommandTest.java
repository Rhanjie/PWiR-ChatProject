package server.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Channel;
import server.ConnectionHandler;
import server.Server;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

class RemoveChannelCommandTest {
    RemoveChannelCommand command;
    Server server;

    ConnectionHandler owner;
    ConnectionHandler client;
    Channel channel;

    @BeforeEach
    void setUp() {
        server = mock(Server.class);
        command = new RemoveChannelCommand(server, "");

        owner = new ConnectionHandler(server, mock(Socket.class));
        client = spy(new ConnectionHandler(server, mock(Socket.class)));

        channel = spy(new Channel("RemoveTest", "", owner));
        channel.attemptToJoin(owner, "");

        when(server.getChannelFromName("RemoveTest")).thenReturn(channel);
        when(server.unregisterChannel(channel)).thenReturn(true);

        //Ignore problematic methods' behaviour
        doNothing().when(channel).broadcast(any());
        doNothing().when(client).sendMessage(any());
    }

    @Test
    void customBehaviour() {
        channel.attemptToJoin(client, "");

        String expected = "Successfully removed the channel!";

        assertNotNull(owner.getChannel());
        assertNotNull(client.getChannel());

        //Case: no arguments
        String actual = command.customBehaviour(owner, new String[]{});
        assertNotEquals(expected, actual);

        //Case: Empty channel name
        actual = command.customBehaviour(owner, new String[]{""});
        assertNotEquals(expected, actual);

        //Case: Non-Existent channel
        actual = command.customBehaviour(owner, new String[]{"abc def"});
        assertNotEquals(expected, actual);

        //Case: No permissions
        actual = command.customBehaviour(client, new String[]{"RemoveTest"});
        assertNotEquals(expected, actual);

        //Case: Perfect situation
        actual = command.customBehaviour(owner, new String[]{"RemoveTest"});
        assertEquals(expected, actual);

        assertNull(owner.getChannel());
        assertNull(client.getChannel());
    }
}