package redis_server.connection;

import redis_server.Propagator;
import redis_server.RedisSocket;
import redis_server.storage.Storage;

import java.io.IOException;

public abstract class Connection implements Runnable{
    protected Storage storage;
    public RedisSocket socket;
    protected Propagator propagator;
    protected int bytesReceived;

    public Connection(RedisSocket socket, Storage storage, Propagator propagator) {
        this.socket = socket;
        this.storage = storage;
        this.propagator = propagator;
        this.bytesReceived = 0;
    }

    public abstract void handle();

    @Override
    public void run(){
        handle();
    }
    public Storage getStorage() {
        return storage;
    }

    public void notifyReplica(){
        propagator.registerReplicaConnection(this);
    }

    public void propagate(byte[] msg) throws IOException {
        propagator.propagate(msg);
    }

    public int getNumberOfReplicas() {
        return propagator.getNumberOfReplicas();
    }

    public int getBytesReceived() {
        return bytesReceived;
    }
}
