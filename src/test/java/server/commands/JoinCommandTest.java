package server.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Channel;
import server.ConnectionHandler;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JoinCommandTest {
    JoinCommand command;
    Server server;

    Channel channel;

    @BeforeEach
    void setUp() {
        server = mock(Server.class);
        channel = new Channel("Testowy kanal", "!@#$QwErtY1234", mock(ConnectionHandler.class));

        when(server.getChannelFromName("Testowy kanal")).thenReturn(channel);

        command = new JoinCommand(server, "");
    }

    @Test
    void customBehaviour() {
        ConnectionHandler client = mock(ConnectionHandler.class);
        String expected = "You have successfully joined the channel #Testowy kanal";

        //Case: no arguments
        String actual = command.customBehaviour(client, new String[]{});
        assertNotEquals(expected, actual);

        //Case: Empty channel name
        actual = command.customBehaviour(client, new String[]{"", "!@#$QwErtY1234"});
        assertNotEquals(expected, actual);

        //Case: Only one argument (without password)
        actual = command.customBehaviour(client, new String[]{"Testowy kanal"});
        assertNotEquals(expected, actual);

        //Case: Wrong password
        actual = command.customBehaviour(client, new String[]{"Testowy kanal", "123"});
        assertNotEquals(expected, actual);

        //Case: Non-Existent channel
        actual = command.customBehaviour(client, new String[]{"Testowyttretr kanal", "!@#$QwErtY1234"});
        assertNotEquals(expected, actual);

        //Case: Perfect situation
        actual = command.customBehaviour(client, new String[]{"Testowy kanal", "!@#$QwErtY1234"});

        assertEquals(expected, actual);
    }
}