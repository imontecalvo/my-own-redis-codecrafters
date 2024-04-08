import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

      ServerSocket serverSocket = null;
      Socket clientSocket = null;
      int port = 6379;
      try {
          serverSocket = new ServerSocket(port);
          // Since the tester restarts your program quite often, setting SO_REUSEADDR
          // ensures that we don't run into 'Address already in use' errors
          serverSocket.setReuseAddress(true);
          // Wait for connection from client.
          clientSocket = serverSocket.accept();

          BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          OutputStream output = clientSocket.getOutputStream();

          while (!clientSocket.isClosed()){
              char[] r = new char[100];
              if (br.read(r) > 0){
                  String response = "+PONG\r\n";
                  output.write(response.getBytes());
              }
          }

      } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
      } finally {
          try {
              if (clientSocket != null) {
                  clientSocket.close();
              }
          } catch (IOException e) {
              System.out.println("IOException: " + e.getMessage());
          }
      }


  }
}
