import resp.CommandFactory;
import resp.Request;
import resp.commands.Command;
import resp.data_types.DataType;
import resp.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable{

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        HashMap<String,DataType> storage = new HashMap<>();

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream output = socket.getOutputStream();

            while (!socket.isClosed()){
                Request request = Request.fromBytes(br);
                if (request!=null){
                    Command command = CommandFactory.createCommand(request);
                    output.write(command.execute(storage));
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }
}
