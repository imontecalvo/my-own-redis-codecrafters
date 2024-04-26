package redis_server.resp.commands;

import redis_server.RedisSocket;
import redis_server.Settings;
import redis_server.resp.data_types.DataType;
import redis_server.resp.data_types.RedisArray;
import redis_server.resp.data_types.RedisBulkString;
import redis_server.resp.data_types.RedisString;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HexFormat;

public class Psync extends Command {
    public static final String COMMAND = "PSYNC";
    private String arg1;
    private String arg2;
    private static final String EMPTY_RDB_FILE = "524544495330303131fa0972656469732d76657205372e322e30fa0a72656469732d62697473c040fa056374696d65c26d08bc65fa08757365642d6d656dc2b0c41000fa08616f662d62617365c000fff06e3bfec0ff5aa2";

    public Psync(String arg1, String arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Psync(RedisArray request) throws IOException {
        if (request.length()!=3) throw new IOException();
        this.arg1 = ((RedisBulkString) request.getElement(1)).getContent();
        this.arg2 = ((RedisBulkString) request.getElement(2)).getContent();;
    }

    @Override
    public void execute() throws IOException{
        checkConnection();
        connection.notifyReplica();
    }

    /*
    * Responde con:
    *   - Mensaje: FULLRESYNC
    *   - Mensaje: Archivo RDB vacio (para sincronizar a la replica)
    * */
    @Override
    public void respond(RedisSocket socket) throws IOException {
        String replId = Settings.getMasterReplicationId();
        int replOffset = Settings.getMasterReplicationOffset();
        RedisString response = new RedisString("FULLRESYNC "+replId+" "+replOffset);

        socket.writeBytes(response.toBytes());
        socket.writeBytes(getRdbFileMessage());
    }

    /*
     * Retorna un array de bytes correspondiente al mensaje de
     * transferencia de un archivo RDB vacío.
     * */
    private byte[] getRdbFileMessage(){
        byte[] headerMessage = String.format("$%d\r\n",EMPTY_RDB_FILE.length()/2).getBytes();
        byte[] fileContent = HexFormat.of().parseHex(EMPTY_RDB_FILE);

        ByteBuffer buffer = ByteBuffer.allocate(headerMessage.length + fileContent.length);
        buffer.put(headerMessage);
        buffer.put(fileContent);
        return buffer.array();
    }

    @Override
    public byte[] encode() throws IOException {
        RedisArray msg = RedisArray.bulkStringArray(new String[]{COMMAND,arg1, arg2});
        return msg.toBytes();
    }

    public static boolean isValidResponse(DataType response) {
        return response instanceof RedisString && ((RedisString) response).getContent().startsWith("FULLRESYNC");
    }
}
