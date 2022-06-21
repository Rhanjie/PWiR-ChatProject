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

    Client(String IP) throws RemoteException, MalformedURLException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(20000);
        rmiService = (RMIInterface) registry.lookup("ChatServer");

        do {
            Scanner scanner = new Scanner(System.in);
            nickname = scanner.nextLine();

            response = rmiService.init(nickname);
        } while (!response.contains("Successfully joined to the server!"));

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        reader = new NonBlockingBufferedReader(new BufferedReader(inputStreamReader));
    }

    public void serve() throws IOException {
        while (true) {
            response = rmiService.getLastMessage(nickname);
            if (response != null)
                System.out.println(response);

            var message = reader.readLine();
            if (message.equalsIgnoreCase("quit")) {
                //rmiService.shutdown(nickname);
                return;
            }

            rmiService.sendRequest(nickname, message);
        }
    }

    /*public void run() {
        try {
            InputHandler handler = new InputHandler(this);
            Thread thread = new Thread(handler);
            thread.start();

            String message;
            while ((message = input.readLine()) != null) {
                System.out.println(message);
            }
        }

        catch (IOException exception) {
            System.out.println("Cannot connect to the server!");

            shutdown();
        }
    }*/
}
