package RedisServer.resp;

import RedisServer.Settings;

import java.io.IOException;

public class Propagator {
    public static void propagate (byte[] message) throws IOException {
        Settings.getReplicas().write(message);
    }
}
