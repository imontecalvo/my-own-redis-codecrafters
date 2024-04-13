package RedisServer.resp.commands;

import RedisServer.resp.Request;
import RedisServer.resp.Storage;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisBulkString;

public class Get implements Command{
    private final String key;

    public Get(Request request) {
        key = ((RedisBulkString)request.getArgs()[0]).getContent();
    }
    @Override
    public byte[] execute() {
        DataType response = Storage.getInstance().get(key);
        return response.toBytes();
    }
}
