import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int port = 6379;
        if (args.length == 2 && args[0].equals("--port")){
            port = Integer.parseInt(args[1]);
        }
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        {
            System.out.println("Logs from your program will appear here!");
        }

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            // Wait for connection from client.

            while (true) {
                clientSocket = serverSocket.accept();

                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}