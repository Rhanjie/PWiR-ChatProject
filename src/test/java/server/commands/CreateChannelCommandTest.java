package server.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ConnectionHandler;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateChannelCommandTest {
    CreateChannelCommand command;
    Server server;

    @BeforeEach
    void setUp() {
        server = mock(Server.class);

        when(server.registerChannel(any())).thenReturn(true);

        command = new CreateChannelCommand(server, "");
    }

    @Test
    void customBehaviour() {
        ConnectionHandler client = mock(ConnectionHandler.class);

        String expected = "Successfully created the channel!";

        //Case: no arguments
        String actual = command.customBehaviour(client, new String[]{});
        assertNotEquals(expected, actual);

        //Case: Empty channel name
        actual = command.customBehaviour(client, new String[]{"", "!@#$QwErtY1234"});
        assertNotEquals(expected, actual);

        //Case: Only one argument
        actual = command.customBehaviour(client, new String[]{"Testowy kanal"});
        assertEquals(expected, actual);

        //Case: More than the required arguments
        actual = command.customBehaviour(client, new String[]{
                "Testowy kanal", "!@#$QwErtY1234", "blabla", "123321", "^&^@#%^*("
        });
        assertEquals(expected, actual);

        //Case: More than the required arguments
        actual = command.customBehaviour(client, new String[]{
                "Testowy kanal", "!@#$QwErtY1234", "blabla", "123321", "^&^@#%^*("
        });
        assertEquals(expected, actual);

        //Case: Perfect situation
        actual = command.customBehaviour(client, new String[]{"Testowy kanal", "!@#$QwErtY1234"});

        assertEquals(expected, actual);
    }
}