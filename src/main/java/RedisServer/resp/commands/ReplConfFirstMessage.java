package RedisServer.resp.commands;

import RedisServer.Settings;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

public class ReplConfFirstMessage implements Command{


    @Override
    public byte[] getResponse() {
        RedisBulkString command = new RedisBulkString("REPLCONF");
        RedisBulkString arg = new RedisBulkString("listening-port");
        RedisBulkString port = new RedisBulkString(Integer.toString(Settings.getPort()));

        return new RedisArray(new DataType[]{command, arg, port}).toBytes();
    }
}
