package server;

public class Validator {
    public static String validateNickname(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return "[ERROR] You did not enter a nickname!";
        }

        if (nickname.length() > 20) {
            return "[ERROR] Given nickname is too long!";
        }

        if (!nickname.matches("[a-zA-Z0-9]*")) {
            return "[ERROR] The nickname can only contain letters and numbers!";
        }

        return "";
    }
}
