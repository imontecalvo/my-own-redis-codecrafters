package RedisServer;

import RedisServer.resp.Propagator;
import RedisServer.resp.Storage;

public abstract class Connection implements Runnable{
    protected Storage storage;
    public RedisSocket socket;
    protected Propagator propagator;

    public Connection(RedisSocket socket, Storage storage, Propagator propagator) {
        this.socket = socket;
        this.storage = storage;
        this.propagator = propagator;
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
        propagator.registerReplica(this);
    }
}
