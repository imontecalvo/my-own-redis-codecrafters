import RedisServer.Settings;
import RedisServer.resp.Parser;
import RedisServer.resp.commands.Command;
import RedisServer.resp.commands.ReplConfFirstMessage;
import RedisServer.resp.commands.ReplConfSecondMessage;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Settings.set(ArgsParser.parse(args));
        int port = Settings.getPort();

        // You can use print statements as follows for debugging, they'll be visible when running tests.
        {
            System.out.println("Logs from your program will appear here!");
        }

        if (!connectToMaster()){
            return;
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

    public static Boolean connectToMaster(){
        try{
            if (Settings.isReplica()){
                String masterAddress = Settings.getMasterReplicationAddress();
                int masterPort = Settings.getMasterReplicationPort();
                Socket socket = new Socket(masterAddress, masterPort);

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream();

                RedisArray msg = new RedisArray(new DataType[]{new RedisBulkString("ping")});
                out.write(msg.toBytes());

                if (Objects.equals(((RedisString) Objects.requireNonNull(Parser.fromBytes(br))).getContent(), "PONG")){
                    Command command = new ReplConfFirstMessage();
                    out.write(command.execute(null));
                    Command command2 = new ReplConfSecondMessage();
                    out.write(command2.execute(null));
                }
            }
        }catch (IOException e){
            System.out.println(e);
            return false;
        }
        return true;
    }
}