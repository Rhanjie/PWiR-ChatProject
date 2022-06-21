package common;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {
    public String init(String givenNickname) throws RemoteException;
    public String sendRequest(String nickname, String message) throws IOException, RemoteException;
    public String getLastMessage(String nickname) throws IOException, RemoteException;
    public void shutdown(String nickname) throws RemoteException;
}
