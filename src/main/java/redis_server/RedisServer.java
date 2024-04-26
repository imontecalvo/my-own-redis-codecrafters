package redis_server;

import java.io.IOException;
import java.net.ServerSocket;
import redis_server.connection.ClientConnection;
import redis_server.storage.Storage;

public class RedisServer {

    protected Storage storage;
    protected Propagator propagator;
    public RedisServer() {
        this.storage = new Storage();
        this.propagator = new Propagator();
    }

    public void run(){
        System.out.println("Waiting for connections...");
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
