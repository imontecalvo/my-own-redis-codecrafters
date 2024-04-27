package redis_server;

import redis_server.connection.Connection;
import redis_server.resp.data_types.DataType;
import redis_server.resp.data_types.RedisArray;
import redis_server.resp.data_types.RedisBulkString;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Propagator {
    private final Set<Connection> replicas = new HashSet<>();

    public void registerReplicaConnection(Connection newReplica){
        replicas.add(newReplica);
    }
    public void propagate(byte[] message) throws IOException {
        for (Connection connection : replicas) {
            try{
                connection.socket.writeBytes(message);
            }catch (SocketException e){
                replicas.remove(connection);
            }
        }
    }

    public int getNumberOfReplicas() {
        return replicas.size();
    }
}
