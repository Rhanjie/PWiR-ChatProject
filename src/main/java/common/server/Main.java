package common.server;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        try {
            Naming.rebind("//localhost/Server", new Server());

            System.out.println("Server ready");
        }

        catch (Exception exception) {
            System.out.println("Server exception: " + exception.toString());

            exception.printStackTrace();
        }
    }
}
