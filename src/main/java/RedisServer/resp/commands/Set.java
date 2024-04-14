package RedisServer.resp.commands;

import RedisServer.resp.Propagator;
import RedisServer.resp.Request;
import RedisServer.resp.Storage;
import RedisServer.resp.data_types.DataType;
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
        if (ttl.isEmpty()){
            return String.format("$3\r\nset\r\n%s\r\n%s",key,value).getBytes();
        }
        return String.format("$5\r\nset\r\n%s\r\n%s\r\npx\r\n%d\r\n",key,value, ttl.get()).getBytes();
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
