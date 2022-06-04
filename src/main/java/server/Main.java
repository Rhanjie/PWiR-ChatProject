package server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        Thread thread = new Thread(server);

        thread.start();
    }
}
