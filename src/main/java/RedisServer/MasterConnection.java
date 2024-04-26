package RedisServer;

import RedisServer.resp.*;
import RedisServer.resp.commands2.Command;
import RedisServer.resp.commands2.Ping;
import RedisServer.resp.commands2.Psync;
import RedisServer.resp.commands2.ReplConf;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

import java.io.IOException;

public class MasterConnection extends Connection{
    public MasterConnection(RedisSocket socket, Storage storage, Propagator propagator) {
        super(socket, storage, propagator);
    }

    @Override
    public void handle() {
        while (!socket.isClosed()) {
            try{
                DataType request = Parser.fromBytes(socket);
                if (!(request instanceof RedisArray)) continue;

                Command command = CommandFactory.create((RedisArray) request);
                command.bindConnection(this);
                command.execute();
                if (command.isResponseRequiredByReplica()){
                    command.respond();
                }
            }catch (IOException e){
                System.out.println(e);
            }
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
