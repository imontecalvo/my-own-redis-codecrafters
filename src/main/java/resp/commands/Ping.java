package resp.commands;

import resp.data_types.RedisBulkString;
import resp.data_types.RedisString;

public class Ping implements Command{
    @Override
    public byte[] execute() {
        RedisString response = new RedisString("PONG");
        return response.toBytes();
    }
}
