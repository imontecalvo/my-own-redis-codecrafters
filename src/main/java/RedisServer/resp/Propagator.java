package RedisServer.resp;

import RedisServer.Settings;

import java.io.IOException;
import java.io.OutputStream;

public class Propagator {
    public static void propagate(byte[] message) throws IOException {
        for (OutputStream out : Settings.getReplicas()) {
            out.write(message);
        }
    }
}
