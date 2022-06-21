package common.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    Server server;

    @BeforeEach
    void setUp() {
        server = mock(Server.class);
    }

    @Test
    void validateNicknameCannotConnectTwice() {
        String nickname = "test";
        when(server.getConnectionFromNickname(nickname)).thenReturn(null);

        String actual = Validator.validateNickname(nickname, server);
        assertTrue(actual.isEmpty());

        server = mock(Server.class);
        nickname = "test";
        when(server.getConnectionFromNickname(nickname)).thenReturn(mock(ConnectionHandler.class));

        actual = Validator.validateNickname(nickname, server);
        assertFalse(actual.isEmpty());
    }

    @Test
    void validateNickname() {
        when(server.getConnectionFromNickname(any())).thenReturn(null);

        String nickname = "test";
        String actual = Validator.validateNickname(nickname, server);
        assertTrue(actual.isEmpty());

        nickname = "123456789012345678901";
        actual = Validator.validateNickname(nickname, server);
        assertFalse(actual.isEmpty());

        nickname = "31jfEF-_ #_";
        actual = Validator.validateNickname(nickname, server);
        assertFalse(actual.isEmpty());

        nickname = "31jfEF-_ _";
        actual = Validator.validateNickname(nickname, server);
        assertTrue(actual.isEmpty());
    }

    @Test
    void validateChannelName() {
        String channelName = "test";
        String actual = Validator.validateChannelName(channelName, server);
        assertTrue(actual.isEmpty());

        channelName = null;
        actual = Validator.validateChannelName(channelName, server);
        assertFalse(actual.isEmpty());

        channelName = "";
        actual = Validator.validateChannelName(channelName, server);
        assertFalse(actual.isEmpty());

        channelName = "123456789012345678901";
        actual = Validator.validateChannelName(channelName, server);
        assertFalse(actual.isEmpty());

        channelName = "1290 -_fdsa  ";
        actual = Validator.validateChannelName(channelName, server);
        assertTrue(actual.isEmpty());

        channelName = "1290 #_fdsa  ";
        actual = Validator.validateChannelName(channelName, server);
        assertFalse(actual.isEmpty());

        channelName = "Duplicated name";
        when(server.getChannelFromName(channelName)).thenReturn(mock(Channel.class));
        actual = Validator.validateChannelName(channelName, server);
        assertFalse(actual.isEmpty());
    }
}