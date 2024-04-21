package RedisServer.resp.commands;

import RedisServer.Settings;
import RedisServer.resp.data_types.RedisInteger;

import java.io.IOException;
import java.io.OutputStream;

public class Wait implements Command{
    @Override
    public byte[] getResponse() {
        int nOfReplicas = Settings.getNumberOfReplicas();
        return new RedisInteger(nOfReplicas).toBytes();
    }
}
