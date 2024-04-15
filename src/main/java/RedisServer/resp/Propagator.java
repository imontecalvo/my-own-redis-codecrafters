package RedisServer.resp;

import RedisServer.Settings;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

import java.io.IOException;
import java.io.OutputStream;

public class Propagator {
    public static void propagate(byte[] message) throws IOException {
        for (OutputStream out : Settings.getReplicas()) {
            out.write(message);
        }
    }
}
