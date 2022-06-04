package server;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        Thread thread = new Thread(server);

        thread.start();
    }
}
