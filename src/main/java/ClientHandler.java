import RedisServer.resp.CommandFactory;
import RedisServer.resp.Request;
import RedisServer.resp.Storage;
import RedisServer.resp.commands.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable{

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream output = socket.getOutputStream();

            while (!socket.isClosed()){
                Request request = Request.fromBytes(br);
                if (request!=null){
                    Command command = CommandFactory.createCommand(request);
                    output.write(command.execute());
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage() + Arrays.toString(e.getStackTrace()));
        }finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage() + Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
