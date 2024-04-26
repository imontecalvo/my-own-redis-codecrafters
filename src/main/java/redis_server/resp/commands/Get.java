package redis_server.resp.commands;

import redis_server.RedisSocket;
import redis_server.storage.Storage;
import redis_server.resp.data_types.DataType;
import redis_server.resp.data_types.RedisArray;
import redis_server.resp.data_types.RedisBulkString;

import java.io.IOException;

public class Get extends Command{

    public static final String COMMAND = "GET";
    private String key;

    public Get(String key) {
        this.key = key;
    }

    public Get(RedisArray request) throws IOException {
        if (request.length()!=2) throw new IOException();
        key = ((RedisBulkString) request.getElement(1)).getContent();
    }

    @Override
    public void execute() {}

    @Override
    public void respond(RedisSocket socket) throws IOException {
        Storage storage = connection.getStorage();
        DataType response = storage.get(key);
        socket.writeBytes(response.toBytes());
    }

    @Override
    public byte[] encode() throws IOException {
        RedisArray msg = RedisArray.bulkStringArray(new String[]{COMMAND, key});
        return msg.toBytes();
    }
}
