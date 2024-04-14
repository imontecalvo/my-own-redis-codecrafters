package RedisServer.resp.commands;

import RedisServer.resp.Propagator;
import RedisServer.resp.Request;
import RedisServer.resp.Storage;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.io.IOException;
import java.util.Optional;

public class Set implements Command{
    private final String key;
    private final DataType value;
    private Optional<Long> ttl;

    public Set(String key, DataType value, Optional<Long> ttl) {
        this.key = key;
        this.value = value;
        this.ttl = ttl;
    }

    public byte[] getMessage(){
        RedisBulkString command = new RedisBulkString("SET");
        RedisBulkString keyArg = new RedisBulkString(key);

        if (ttl.isEmpty()){
            return new RedisArray(new DataType[]{command,keyArg,value}).toBytes();
        }

        RedisBulkString pxArg = new RedisBulkString("PX");
        RedisBulkString pxValue = new RedisBulkString(ttl.get().toString());
        return new RedisArray(new DataType[]{command,keyArg,value,pxArg,pxValue}).toBytes();
    }

    public static Set fromRequest(Request request){
        Optional<Long> ttl = Optional.empty();
        DataType[] args = request.getArgs();
        String key = ((RedisBulkString)args[0]).getContent();
        DataType value = request.getArgs()[1];

        if (args.length==4){
            if(((RedisBulkString) args[2]).getContent().equalsIgnoreCase("PX")){
                ttl = Optional.of(Long.parseLong(((RedisBulkString)args[3]).getContent()));
            }
        }

        Set command = new Set(key, value, ttl);
        try{
            Propagator.propagate(command.getMessage());
        }finally {
            return command;
        }
    }
    @Override
    public byte[] getResponse() {
        Storage.getInstance().put(key, value, ttl);
        RedisString response = new RedisString("OK");
        return response.toBytes();
    }
}
