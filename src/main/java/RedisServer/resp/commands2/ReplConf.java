package RedisServer.resp.commands2;

import RedisServer.AckCounter;
import RedisServer.RedisSocket;
import RedisServer.Settings;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.io.IOException;

public class ReplConf extends Command{
    public static final String COMMAND = "REPLCONF";
    private String arg1;
    private String arg2;

    public ReplConf(String arg1, String arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public ReplConf(RedisArray request) throws IOException {
        if (request.length()!=3) throw new IOException();
        this.arg1 = ((RedisBulkString) request.getElement(1)).getContent();
        this.arg2 = ((RedisBulkString) request.getElement(2)).getContent();;
    }

    @Override
    public void execute() {
        if (arg1.equalsIgnoreCase("ACK")){
            AckCounter.getInstance().newAck();
        }
    }

    /*
    * Si se recibe un REPLCONF de handshake (listening-port o capa sync2), se responde "OK"
    * Si se recibe un REPLCONF GETACK *, se responde emitiendo un REPLCONF ACK <offset>
    * En caso contrario, no se emite respuesta.
    * */
    @Override
    public void respond(RedisSocket socket) throws IOException {
        if (isHandshakeMessage()){
            RedisString response = new RedisString("OK");
            socket.writeBytes(response.toBytes());

        }else if (arg1.equalsIgnoreCase("GETACK")){
            int replicationOffset = Settings.getMasterReplicationOffset();
            RedisArray response = RedisArray.bulkStringArray(new String[]{COMMAND,"ACK",String.valueOf(replicationOffset)});
            socket.writeBytes(response.toBytes());
        }
    }

    @Override
    public byte[] encode() throws IOException {
        RedisArray msg = RedisArray.bulkStringArray(new String[]{COMMAND, arg1,arg2});
        return msg.toBytes();
    }

    public static boolean isValidResponse(DataType response) {
        return response instanceof RedisString && ((RedisString) response).getContent().equalsIgnoreCase("OK");
    }

    @Override
    public boolean isResponseRequiredByReplica(){
        return arg1.equalsIgnoreCase("GETACK");
    }

    private boolean isHandshakeMessage() {
        return arg1.equalsIgnoreCase("listening-port") ||
                (arg1.equalsIgnoreCase("capa") && arg2.equalsIgnoreCase("psync2"));
    }
}
