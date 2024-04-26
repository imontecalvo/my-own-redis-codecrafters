package redis_server;

import redis_server.connection.Connection;
import redis_server.resp.data_types.DataType;
import redis_server.resp.data_types.RedisArray;
import redis_server.resp.data_types.RedisBulkString;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class Propagator {
    private final Set<Connection> replicas = new HashSet<>();

    public void registerReplicaConnection(Connection newReplica){
        replicas.add(newReplica);
    }
    public void propagate(byte[] message) throws IOException {
        for (Connection connection : replicas) {
            connection.socket.writeBytes(message);
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

    public int getNumberOfReplicas() {
        return replicas.size();
    }
}
