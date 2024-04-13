package RedisServer.resp.commands;

import RedisServer.Settings;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.io.IOException;
import java.io.OutputStream;

public class ReplConf implements Command{

    public byte[] getFirstMessage(){
        RedisBulkString command = new RedisBulkString("REPLCONF");
        RedisBulkString arg = new RedisBulkString("listening-port");
        RedisBulkString port = new RedisBulkString(Integer.toString(Settings.getPort()));

        return new RedisArray(new DataType[]{command, arg, port}).toBytes();
    }

    public byte[] getSecondMessage(){
        RedisBulkString command = new RedisBulkString("REPLCONF");
        RedisBulkString arg1 = new RedisBulkString("capa");
        RedisBulkString arg2 = new RedisBulkString("npsync2");

        return new RedisArray(new DataType[]{command, arg1, arg2}).toBytes();
    }

    public void sendFirstMessage(OutputStream output) throws IOException {
        output.write(this.getFirstMessage());
    }

    public void sendSecondMessage(OutputStream output) throws IOException {
        output.write(this.getSecondMessage());
    }

    @Override
    public byte[] getResponse() {
        return new RedisString("OK").toBytes();
    }
}
