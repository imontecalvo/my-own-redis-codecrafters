package resp.commands;

import resp.Request;
import resp.data_types.DataType;
import resp.data_types.RedisBulkString;
import resp.data_types.RedisString;

import java.util.HashMap;

public class Set implements Command{
    private final String key;
    private final DataType value;

    public Set(Request request) {
        key = ((RedisBulkString)request.getArgs()[0]).getContent();
        value = request.getArgs()[1];
    }
    @Override
    public byte[] execute(HashMap<String, DataType> storage) {
        storage.put(key,value);
        RedisString response = new RedisString("OK");
        return response.toBytes();
    }
}
