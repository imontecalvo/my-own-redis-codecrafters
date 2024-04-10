package resp.commands;

import resp.Request;
import resp.Storage;
import resp.data_types.DataType;
import resp.data_types.RedisBulkString;

public class Echo implements Command{
    private final String arg;

    public Echo(Request request) {
        DataType[] args = request.getArgs();;
        arg = ((RedisBulkString)args[0]).getContent();
    }

    @Override
    public byte[] execute(Storage storage) {
        RedisBulkString response = new RedisBulkString(arg);
        return response.toBytes();
    }
}
