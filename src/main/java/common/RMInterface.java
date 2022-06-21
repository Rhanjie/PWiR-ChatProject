package common;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMInterface extends Remote {
    public String init(String givenNickname) throws RemoteException;
    public String sendRequest(String message) throws IOException;
    public void shutdown();
}
