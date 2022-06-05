package server;

public class Validator {
    public static String validateNickname(String nickname, Server serverHandler) {
        if (nickname == null || nickname.isEmpty()) {
            return "[ERROR] You did not enter a nickname!";
        }

        if (nickname.length() > 20) {
            return "[ERROR] Given nickname is too long!";
        }

        if (!nickname.matches("[a-zA-Z0-9 _-]*")) {
            return "[ERROR] The nickname can only contain letters, numbers and simple characters like '-' '_'!";
        }

        if (serverHandler.getConnectionFromNickname(nickname) != null) {
            return "[ERROR] Given nickname is already used! Choose another one";
        }

        return "";
    }

    public static String validateChannelName(String channelName) {
        if (channelName == null || channelName.isEmpty()) {
            return "[ERROR] You did not enter a channel name!";
        }

        if (channelName.length() > 20) {
            return "[ERROR] Given channel name is too long!";
        }

        if (!channelName.matches("[a-zA-Z0-9 _-]*")) {
            return "[ERROR] The nickname can only contain letters, numbers and simple characters like '-' '_'!";
        }

        return "";
    }
}
