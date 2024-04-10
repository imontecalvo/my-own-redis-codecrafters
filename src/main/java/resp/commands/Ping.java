package resp.commands;

import resp.data_types.DataType;
import resp.data_types.RedisString;

import java.util.HashMap;

public class Ping implements Command{
    @Override
    public byte[] execute(HashMap<String, DataType> storage) {
        RedisString response = new RedisString("PONG");
        return response.toBytes();
    }
}
