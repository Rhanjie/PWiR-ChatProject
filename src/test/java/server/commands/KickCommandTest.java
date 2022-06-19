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

class KickCommandTest {
    KickCommand command;
    Server server;

    ConnectionHandler owner;
    ConnectionHandler member;
    Channel channel;

    @BeforeEach
    void setUp() {
        server = mock(Server.class);

        owner = new ConnectionHandler(mock(Server.class), mock(Socket.class));
        member = spy(new ConnectionHandler(mock(Server.class), mock(Socket.class)));
        channel = spy(new Channel("Testowy kanal", "", owner));

        when(server.getChannelFromName("Testowy kanal")).thenReturn(channel);

        command = new KickCommand(server, "");

        owner.setNickname("owner");
        member.setNickname("member");

        //Ignore problematic methods' behaviour
        doNothing().when(channel).broadcast(any());
        doNothing().when(member).sendMessage(any());
    }

    @Test
    void customBehaviour() {
        String expected = "You have successfully kicked " + member.getNickname() + " from the channel!";

        //Case: no arguments
        String actual = command.customBehaviour(owner, new String[]{});
        assertNotEquals(expected, actual);

        //Case: Not in channel
        actual = command.customBehaviour(owner, new String[]{member.getNickname()});
        assertNotEquals(expected, actual);

        channel.attemptToJoin(owner, "");

        //Case: Member is not in channel
        actual = command.customBehaviour(owner, new String[]{member.getNickname()});
        assertNotEquals(expected, actual);

        channel.attemptToJoin(member, "");

        //Case: No permissions
        actual = command.customBehaviour(member, new String[]{owner.getNickname()});
        assertNotEquals(expected, actual);

        //Case: Kick yourself
        actual = command.customBehaviour(owner, new String[]{owner.getNickname()});
        assertNotEquals(expected, actual);

        //Case: Correct kick
        actual = command.customBehaviour(owner, new String[]{member.getNickname()});
        assertEquals(expected, actual);

        //Case: Next kick after correct kick
        actual = command.customBehaviour(owner, new String[]{member.getNickname()});
        assertNotEquals(expected, actual);
    }
}