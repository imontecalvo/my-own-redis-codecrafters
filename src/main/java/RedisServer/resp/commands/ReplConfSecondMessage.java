package RedisServer.resp.commands;

import RedisServer.resp.Storage;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

public class ReplConfSecondMessage implements Command{
    @Override
    public byte[] execute(Storage storage) {
        RedisBulkString command = new RedisBulkString("REPLCONF");
        RedisBulkString arg1 = new RedisBulkString("capa");
        RedisBulkString arg2 = new RedisBulkString("npsync2");

        return new RedisArray(new DataType[]{command, arg1, arg2}).toBytes();
    }
}
