package resp.commands;

import resp.Request;
import resp.Storage;
import resp.data_types.DataType;
import resp.data_types.RedisBulkString;

public class Get implements Command{
    private final String key;

    public Get(Request request) {
        key = ((RedisBulkString)request.getArgs()[0]).getContent();
    }
    @Override
    public byte[] execute(Storage storage) {
        DataType response = storage.get(key);
        return response.toBytes();
    }
}
