import RedisServer.Settings;
import RedisServer.resp.CommandFactory;
import RedisServer.resp.Parser;
import RedisServer.resp.Request;
import RedisServer.resp.Storage;
import RedisServer.resp.commands.Command;
import RedisServer.resp.commands.Psync;
import RedisServer.resp.commands.ReplConf;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Settings.set(ArgsParser.parse(args));
        int port = Settings.getPort();

        Storage storage = Storage.getInstance();

        // You can use print statements as follows for debugging, they'll be visible when running tests.
        {
            System.out.println("Logs from your program will appear here!");
        }

        if (Settings.isReplica()) {
            Thread thread = connectToMaster();
            if (thread != null) {
                thread.start();
            }
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

    public static Thread connectToMaster() {
        try {
            String masterAddress = Settings.getMasterReplicationAddress();
            int masterPort = Settings.getMasterReplicationPort();
            Socket socket = new Socket(masterAddress, masterPort);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            RedisArray msg = new RedisArray(new DataType[]{new RedisBulkString("ping")});
            out.write(msg.toBytes());

            if (Objects.equals(((RedisString) Objects.requireNonNull(Parser.fromBytes(br))).getContent(), "PONG")) {
                ReplConf command = new ReplConf();
                command.sendFirstMessage(out);
                Parser.fromBytes(br);

                command.sendSecondMessage(out);
                Parser.fromBytes(br);

                Psync command3 = new Psync();
                command3.send(out);
                Parser.fromBytes(br);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                readRDBFile(br,dataInputStream);
                return new Thread(() -> listeningForReplicationCommands(socket, out, br));
            }
        } catch (IOException e) {
            System.out.println("Cannot sync to master server. " + e);
        }
        return null;
    }

    public static void readRDBFile(BufferedReader reader, DataInputStream inputReader) throws IOException {
        char[] symbol = new char[1];
        reader.read(symbol);
        if (symbol[0] == '$') {
            int length = Integer.parseInt(reader.readLine());
        }
    }

    public static void listeningForReplicationCommands(Socket socket, OutputStream out, BufferedReader br) {
        while (!socket.isClosed()) {
            try {
                Request request = Request.fromBytes(br);
                if (request != null) {
                    Command commandRecv = CommandFactory.createCommand(request);
                    if (request.getCommand().equalsIgnoreCase("REPLCONF") &&
                            ((RedisBulkString) request.getArgs()[0]).getContent().equalsIgnoreCase("GETACK")){
                        commandRecv.respond(out); //TODO: Cambia por command.isResponseRequiredByReplica
                    }else{
                        commandRecv.getResponse();
                    }
                    Settings.addOffset(request.getNumberOfBytes());-
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}