package common;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInterface extends Remote {
    public String init(String givenNickname) throws RemoteException;
    public String sendRequest(String nickname, String message) throws IOException;
    public String getLastMessage(String nickname) throws IOException;
    public void shutdown(String nickname) throws RemoteException;
}
