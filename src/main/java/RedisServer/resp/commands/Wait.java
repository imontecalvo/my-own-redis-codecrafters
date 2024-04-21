package RedisServer.resp.commands;

import java.io.IOException;
import java.io.OutputStream;

public class Wait implements Command{
    @Override
    public byte[] getResponse() {
        return "0".getBytes();
    }
}
