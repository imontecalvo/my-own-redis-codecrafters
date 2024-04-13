package RedisServer.resp.commands;

import RedisServer.resp.data_types.RedisString;

public class Ping implements Command{
    @Override
    public byte[] execute() {
        RedisString response = new RedisString("PONG");
        return response.toBytes();
    }
}
