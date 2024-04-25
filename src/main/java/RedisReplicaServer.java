import RedisServer.resp.Parser;
import RedisServer.resp.commands.Psync;
import RedisServer.resp.commands.ReplConf;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;
import RedisServer.Settings;
import RedisServer.RedisSocket;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class RedisReplicaServer extends RedisServer{
    @Override
    public void run(){
        handleConnectionToMaster();
        super.run();
    }

    private void handleConnectionToMaster(){
        try {
            String masterAddress = Settings.getMasterReplicationAddress();
            int masterPort = Settings.getMasterReplicationPort();
            RedisSocket socket = new RedisSocket(masterAddress, masterPort);

            RedisArray msg = new RedisArray(new DataType[]{new RedisBulkString("ping")});
            System.out.println("ENVIO PING");
            out.write(msg.toBytes());

            if (Objects.equals(((RedisString) Objects.requireNonNull(Parser.fromBytes(br))).getContent(), "PONG")) {
                ReplConf command = new ReplConf();
                command.sendFirstMessage(out);
                System.out.println("ENVIO REPLCONF");
                Parser.fromBytes(br).print();

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
}
