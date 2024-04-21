package RedisServer.resp.commands;

import RedisServer.Settings;
import RedisServer.resp.Request;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisInteger;

import java.io.IOException;
import java.io.OutputStream;

public class Wait implements Command{
    private DataType[] args;

    public Wait(DataType[] args) {
        this.args = args;
    }

    public static Wait fromRequest(Request request){
        return new Wait(request.getArgs());
    }
    @Override
    public byte[] getResponse() {
        int nOfAck = Integer.parseInt(((RedisBulkString) args[0]).getContent());
        int timeout = Integer.parseInt(((RedisBulkString) args[1]).getContent());

        try{
            synchronized (Settings.ackLock) {
                Settings.setAckCounter(nOfAck);
                Settings.ackLock.wait(timeout);
            }
        }catch (InterruptedException e){
            System.out.println(e);
        }
        int nOfReplicas = Settings.getNumberOfReplicas();
        return new RedisInteger(nOfReplicas).toBytes();
    }
}
