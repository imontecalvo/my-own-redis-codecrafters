package RedisServer.resp.commands2;

import RedisServer.Connection;
import RedisServer.RedisSocket;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

import java.io.IOException;

public class Echo extends Command{
    public static final String COMMAND = "ECHO";
    private String arg;

    public Echo(String arg) {
        this.arg = arg;
    }

    public Echo(RedisArray request) throws IOException {
        if (request.length()!=2) throw new IOException();
        arg = ((RedisBulkString) request.getElement(1)).getContent();
    }

    @Override
    public void execute() {}

    @Override
    public void respond(RedisSocket socket) throws IOException {
        RedisBulkString response = new RedisBulkString(arg);
        socket.writeBytes(response.toBytes());
    }

    @Override
    public byte[] encode() throws IOException {
        RedisArray msg = RedisArray.bulkStringArray(new String[]{COMMAND, arg});
        return msg.toBytes();
    }
}
