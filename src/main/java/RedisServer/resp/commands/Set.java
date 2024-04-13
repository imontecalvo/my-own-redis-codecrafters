package RedisServer.resp.commands;

import RedisServer.resp.Request;
import RedisServer.resp.Storage;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.util.Optional;

public class Set implements Command{
    private final String key;
    private final DataType value;
    private Optional<Long> ttl;

    public Set(Request request) {
        ttl = Optional.empty();
        DataType[] args = request.getArgs();
        key = ((RedisBulkString)args[0]).getContent();
        value = request.getArgs()[1];

        if (args.length==4){
            if(((RedisBulkString) args[2]).getContent().equalsIgnoreCase("PX")){
                ttl = Optional.of(Long.parseLong(((RedisBulkString)args[3]).getContent()));
            }
        }
    }
    @Override
    public byte[] execute(Storage storage) {
        storage.put(key, value, ttl);
        RedisString response = new RedisString("OK");
        return response.toBytes();
    }
}
