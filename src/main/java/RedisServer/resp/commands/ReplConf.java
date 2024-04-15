package RedisServer.resp.commands;

import RedisServer.Settings;
import RedisServer.resp.Request;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.OutputStream;

public class ReplConf implements Command{
    private DataType[] args;

    public ReplConf() {
    }

    public ReplConf(DataType[] args) {
        this.args = args;
    }

    public static ReplConf fromRequest(Request request){
        return new ReplConf(request.getArgs());
    }

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
        if (((RedisBulkString)args[0]).getContent().equalsIgnoreCase("GETACK")){
            RedisBulkString command = new RedisBulkString("REPLCONF");
            RedisBulkString type = new RedisBulkString("ACK");
            RedisBulkString arg = new RedisBulkString("0");

            return "*3\r\n$8\r\nREPLCONF\r\n$3\r\nACK\r\n$1\r\n0\r\n".getBytes();
            //return new RedisArray(new DataType[]{command,type,arg}).toBytes();
        }
        return new RedisString("OK").toBytes();
    }
}
