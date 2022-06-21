package common.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            //System.out.println("Enter server IP:");

            //Scanner scanner = new Scanner(System.in);
            //String IP = scanner.next();

            Client client = new Client("");
            client.serve();
        }

        catch (NotBoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
