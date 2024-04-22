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

    public static void askForAcknowledge() throws IOException {
        for (OutputStream out : Settings.getReplicas()) {
            RedisBulkString s1 = new RedisBulkString("REPLCONF");
            RedisBulkString s2 = new RedisBulkString("GETACK");
            RedisBulkString s3 = new RedisBulkString("*");

            out.write(new RedisArray(new DataType[]{s1,s2,s3}).toBytes());
            //out.write("*3\r\n$8\r\nreplconf\r\n$6\r\ngetack\r\n$1\r\n*\r\n".getBytes());
        }
    }
}
