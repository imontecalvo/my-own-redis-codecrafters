import RedisServer.resp.Storage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import RedisServer.Settings;
import RedisServer.RedisSocket;

public class RedisServer {

    protected Storage storage;

    public RedisServer() {
        this.storage = Storage.getInstance();
    }

    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(Settings.getPort());
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            while (true) {
                RedisSocket clientSocket = new RedisSocket(serverSocket.accept());

                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
