package RedisServer.resp.commands;

import RedisServer.resp.data_types.RedisInteger;

import java.io.IOException;
import java.io.OutputStream;

public class Wait implements Command{
    @Override
    public byte[] getResponse() {
        return new RedisInteger(0).toBytes();
    }
}
