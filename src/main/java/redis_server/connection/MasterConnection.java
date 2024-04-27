package redis_server.connection;

import redis_server.Propagator;
import redis_server.RedisSocket;
import redis_server.Settings;
import redis_server.storage.Storage;
import redis_server.resp.*;
import redis_server.resp.commands.Command;
import redis_server.resp.commands.Ping;
import redis_server.resp.commands.Psync;
import redis_server.resp.commands.ReplConf;
import redis_server.resp.data_types.DataType;
import redis_server.resp.data_types.RedisArray;

import java.io.IOException;
import java.util.Arrays;

public class MasterConnection extends Connection {
    public MasterConnection(RedisSocket socket, Storage storage, Propagator propagator) {
        super(socket, storage, propagator);
    }

    @Override
    public void handle() {
        try {
            while (!socket.isClosed()) {
                DataType request = Parser.fromBytes(socket);
                if (!(request instanceof RedisArray)) return;
                //request.print();
                Command command = CommandFactory.create((RedisArray) request);
                command.bindConnection(this);
                command.execute();
                if (command.isResponseRequiredByReplica()){
                    command.respond();
                }
                bytesReceived+=request.encode().length();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
    }

    /*
    * LLeva a cabo el handhhake que implica:
    *   - Enviar PING -> Recibir PONG
    *   - Enviar REPLCONF listening-port <port> -> Recibir OK
    *   - Enviar REPLCONF capa psync2 -> Recibir OK
    *   - Enviar PSYNC ? -1 -> Recibir FULLRESYNC
    *   - Recibir Archivo RDB para sincronizar
    * */
    public void handleHandshake() throws IOException {
        int masterPort = Settings.getMasterReplicationPort();

        new Ping().send(socket);
        new ReplConf("listening-port", String.valueOf(masterPort)).send(socket);
        new ReplConf("capa", "psync2").send(socket);
        new Psync("?", "-1").send(socket);

        if (!Ping.isValidResponse(Parser.fromBytes(socket)) ||
                !ReplConf.isValidResponse(Parser.fromBytes(socket)) ||
                !ReplConf.isValidResponse(Parser.fromBytes(socket)) ||
                !Psync.isValidResponse(Parser.fromBytes(socket)))
        {
            throw new IOException();
        }
        readRDBFile(socket);
    }

    /*
    * Lee archivo RDB desde el socket
    * */
    private void readRDBFile(RedisSocket socket) throws IOException {
        char[] symbol = socket.readChars(1);
        if (symbol[0] == '$') {
            int length = Integer.parseInt(socket.readLine());
        }
    }
}
