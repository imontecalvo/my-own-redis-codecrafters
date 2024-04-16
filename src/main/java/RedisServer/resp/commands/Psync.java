package RedisServer.resp.commands;

import RedisServer.Settings;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HexFormat;

public class Psync implements Command{
    private static final String EMPTY_RDB_FILE = "524544495330303131fa0972656469732d76657205372e322e30fa0a72656469732d62697473c040fa056374696d65c26d08bc65fa08757365642d6d656dc2b0c41000fa08616f662d62617365c000fff06e3bfec0ff5aa2";


    public byte[] getMessage() {
        RedisBulkString command = new RedisBulkString("PSYNC");
        RedisBulkString arg1 = new RedisBulkString("?");
        RedisBulkString arg2 = new RedisBulkString("-1");

        return new RedisArray(new DataType[]{command,arg1, arg2}).toBytes();
    }

    public void send(OutputStream output) throws IOException {
        output.write(this.getMessage());
    }

    @Override
    public byte[] getResponse() {
        String replId = Settings.getMasterReplicationId();
        int replOffset = Settings.getMasterReplicationOffset();

        return new RedisString("FULLRESYNC "+replId+" "+replOffset).toBytes();
    }

    public byte[] getFileContentMessage() {
        byte[] headerMessage = String.format("$%d\r\n",EMPTY_RDB_FILE.length()/2).getBytes();
        byte[] fileContent = HexFormat.of().parseHex(EMPTY_RDB_FILE);

        ByteBuffer buffer = ByteBuffer.allocate(headerMessage.length + fileContent.length);
        buffer.put(headerMessage);
        buffer.put(fileContent);
        return buffer.array();
    }

    @Override
    public void respond(OutputStream out) throws IOException {
        out.write(getResponse());
        out.write(getFileContentMessage());
        out.write("*3\r\n$8\r\nreplconf\r\n$6\r\ngetack\r\n$1\r\n*\r\n".getBytes());
        Settings.addReplica(out);
    }
}
