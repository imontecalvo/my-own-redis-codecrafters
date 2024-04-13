package RedisServer.resp.commands;

import RedisServer.resp.Storage;
import RedisServer.resp.data_types.RedisString;

public class Ping implements Command{
    @Override
    public byte[] execute(Storage storage) {
        RedisString response = new RedisString("PONG");
        return response.toBytes();
    }
}
