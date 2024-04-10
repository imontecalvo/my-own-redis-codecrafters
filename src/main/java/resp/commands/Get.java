package resp.commands;

import resp.Request;
import resp.data_types.DataType;
import resp.data_types.RedisBulkString;

import java.util.HashMap;

public class Get implements Command{
    private final String key;

    public Get(Request request) {
        key = ((RedisBulkString)request.getArgs()[0]).getContent();
    }
    @Override
    public byte[] execute(HashMap<String, DataType> storage) {
        DataType response = storage.get(key);
        if (response==null){
            response = new RedisBulkString(null);
        }
        return response.toBytes();
    }
}
