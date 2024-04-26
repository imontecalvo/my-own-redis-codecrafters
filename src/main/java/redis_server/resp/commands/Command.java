package redis_server.resp.commands;

import redis_server.connection.Connection;
import redis_server.RedisSocket;

import java.io.IOException;

public abstract class Command {
    protected Connection connection = null;

    public abstract void execute() throws IOException;
    public abstract void respond(RedisSocket socket) throws IOException;
    public void respond() throws IOException{
        checkConnection();
        respond(connection.socket);
    }
    public abstract byte[] encode() throws IOException;
    public void send(RedisSocket socket) throws IOException{
        socket.writeBytes(encode());
    }
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
