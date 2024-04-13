package RedisServer.resp.commands;

import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

public class Psync implements Command{
    @Override
    public byte[] execute() {
        RedisBulkString command = new RedisBulkString("PSYNC");
        RedisBulkString arg1 = new RedisBulkString("?");
        RedisBulkString arg2 = new RedisBulkString("-1");

        return new RedisArray(new DataType[]{command,arg1, arg2}).toBytes();
    }
}
