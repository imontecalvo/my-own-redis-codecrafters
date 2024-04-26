package RedisServer.resp.commands2;

import RedisServer.Connection;
import RedisServer.RedisSocket;
import RedisServer.resp.data_types.DataType;

import java.io.IOException;

public abstract class Command {
    protected Connection connection = null;

    public abstract void execute() throws IOException;
    public abstract void respond(RedisSocket socket) throws IOException;
    public void respond() throws IOException{
        checkConnection();
        respond(connection.socket);
    }
    public abstract void send(RedisSocket socket) throws IOException;
    public void send() throws IOException{
        checkConnection();
        send(connection.socket);
    }
    public void bindConnection(Connection connection){
        this.connection = connection;
    }
    public boolean isResponseRequiredByReplica(){
        return false;
    }
    void checkConnection() throws IOException{
        if (connection==null) throw new IOException();
    }
}
