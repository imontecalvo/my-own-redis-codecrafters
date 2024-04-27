package redis_server.resp.commands;

import redis_server.RedisSocket;
import redis_server.resp.data_types.DataType;
import redis_server.resp.data_types.RedisArray;
import redis_server.resp.data_types.RedisBulkString;
import redis_server.resp.data_types.RedisString;

import java.io.IOException;
import java.util.Optional;

public class Set extends Command{
    public static final String COMMAND = "SET";
    private final String key;
    private final DataType value;
    private final Optional<Long> ttl;

    public Set(String key, DataType value, Optional<Long> ttl) {
        this.key = key;
        this.value = value;
        this.ttl = ttl;
    }
    public Set(String key, DataType value) {
        this.key = key;
        this.value = value;
        this.ttl = Optional.empty();
    }
    public Set(RedisArray request) throws IOException {
        if (request.length()<3) throw new IOException();
        this.key = ((RedisBulkString) request.getElement(1)).getContent();
        this.value = request.getElement(2);
        Optional<Long> ttl = Optional.empty();

        //Chequeo de argumento PX (TTL)
        if (request.length()==5){
            request.print();
            if(((RedisBulkString) request.getElement(3)).getContent().equalsIgnoreCase("PX")){
                ttl = Optional.of(Long.parseLong(((RedisBulkString) request.getElement(4)).getContent()));
            }
        }
        this.ttl = ttl;
    }

    @Override
    public void execute() throws IOException {
        checkConnection();
        connection.getStorage().put(key, value, ttl);
        connection.propagate(encode());
    }

    @Override
    public void respond(RedisSocket socket) throws IOException {
        RedisString response = new RedisString("OK");
        socket.writeBytes(response.toBytes());
    }

    @Override
    public byte[] encode() throws IOException {
        RedisBulkString command = new RedisBulkString(COMMAND);
        RedisBulkString key = new RedisBulkString(this.key);
        RedisArray msg;

        if (ttl.isEmpty()){
            msg = new RedisArray(new DataType[]{command, value, key});
        }else{
            RedisBulkString px = new RedisBulkString("PX");
            RedisBulkString ttl = new RedisBulkString(this.ttl.get().toString());
            msg = new RedisArray(new DataType[]{command, value, key, px, ttl});
        }
        return msg.toBytes();
    }
}
