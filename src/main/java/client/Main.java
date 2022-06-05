package client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter server IP:");

        Scanner scanner = new Scanner(System.in);
        String IP = scanner.next();

        Client client = new Client(IP);
        Thread thread = new Thread(client);

        thread.start();
    }
}
