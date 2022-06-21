package common.server;

import common.RMIInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(20000);
            registry.bind("ChatServer", server);

            System.out.println("Server ready");
        }

        catch (Exception exception) {
            System.out.println("Server exception: " + exception.toString());

            exception.printStackTrace();
        }
    }
}
