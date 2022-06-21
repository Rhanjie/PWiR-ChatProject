package common.client;

import common.RMIInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    private static RMIInterface rmiService;

    NonBlockingBufferedReader reader;
    String nickname = "";
    String response;

    Client() throws RemoteException, MalformedURLException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(20000);
        rmiService = (RMIInterface) registry.lookup("ChatServer");

        do {
            System.out.println("Give a nickname: ");
            Scanner scanner = new Scanner(System.in);
            nickname = scanner.nextLine();

            response = rmiService.init(nickname);
        } while (!response.contains("Successfully joined to the server!"));

        System.out.println(response);

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        reader = new NonBlockingBufferedReader(new BufferedReader(inputStreamReader));
    }

    public void serve() throws IOException {
        while (true) {
            response = rmiService.getLastMessage(nickname);
            if (response != null)
                System.out.println(response);

            var message = reader.readLine();
            if (message != null) {
                if (message.equalsIgnoreCase("quit")) {
                    rmiService.shutdown(nickname);

                    return;
                }

                response = rmiService.sendRequest(nickname, message);
                if (!response.isEmpty()) {
                    System.out.println(response);
                }
            }
        }
    }
}
