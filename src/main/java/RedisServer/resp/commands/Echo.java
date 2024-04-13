package RedisServer.resp.commands;

import RedisServer.resp.Request;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisBulkString;

public class Echo implements Command{
    private final String arg;

    public Echo(Request request) {
        DataType[] args = request.getArgs();;
        arg = ((RedisBulkString)args[0]).getContent();
    }

    @Override
    public byte[] getResponse() {
        RedisBulkString response = new RedisBulkString(arg);
        return response.toBytes();
    }
}
