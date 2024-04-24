package RedisServer.resp.commands;

import RedisServer.AckCounter;
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
        //Replica response when master asks for ACK
        if (((RedisBulkString)args[0]).getContent().equalsIgnoreCase("GETACK")){
            RedisBulkString command = new RedisBulkString("REPLCONF");
            RedisBulkString type = new RedisBulkString("ACK");
            RedisBulkString arg = new RedisBulkString(String.valueOf(Settings.getMasterReplicationOffset()));

            return new RedisArray(new DataType[]{command,type,arg}).toBytes();
        }
        //Master response when replica sends REPLCONF commands
        if (((RedisBulkString)args[0]).getContent().equalsIgnoreCase("ACK")){
            System.out.println("Recibi ACK");
            AckCounter.newAck();
        }
        return new RedisString("OK").toBytes();
    }
}
