import RedisServer.resp.Propagator;
import RedisServer.resp.Storage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import RedisServer.Settings;
import RedisServer.RedisSocket;
import RedisServer.Connection;
import RedisServer.ClientConnection;

public class RedisServer {

    protected Storage storage;
    protected Propagator propagator;
    public RedisServer() {
        this.storage = new Storage();
        this.propagator = new Propagator();
    }

    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(Settings.getPort());
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            while (true) {
                RedisSocket clientSocket = new RedisSocket(serverSocket.accept());
                Thread connection = new Thread(new ClientConnection(clientSocket, storage, propagator));
                connection.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
