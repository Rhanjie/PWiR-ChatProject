package server;

public class Validator {
    public static String validateNickname(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return "[ERROR] You did not enter a nickname!";
        }

        if (nickname.length() > 20) {
            return "[ERROR] Given nickname is too long!";
        }

        if (!nickname.matches("[a-zA-Z0-9 _-]*")) {
            return "[ERROR] The nickname can only contain letters, numbers and simple characters like '-' '_'!";
        }

        return "";
    }
}
