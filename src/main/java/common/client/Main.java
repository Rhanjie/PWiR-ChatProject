package common.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.serve();
        }

        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
