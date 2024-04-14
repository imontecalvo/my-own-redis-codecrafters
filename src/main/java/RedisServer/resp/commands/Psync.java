package RedisServer.resp.commands;

import RedisServer.Settings;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.io.IOException;
import java.io.OutputStream;

public class Psync implements Command{

    public byte[] getMessage() {
        RedisBulkString command = new RedisBulkString("PSYNC");
        RedisBulkString arg1 = new RedisBulkString("?");
        RedisBulkString arg2 = new RedisBulkString("-1");

        return new RedisArray(new DataType[]{command,arg1, arg2}).toBytes();
    }

    public void send(OutputStream output) throws IOException {
        output.write(this.getMessage());
    }

    @Override
    public byte[] getResponse() {
        String replId = Settings.getMasterReplicationId();
        int replOffset = Settings.getMasterReplicationOffset();

        return new RedisString("FULLRESYNC "+replId+" "+replOffset).toBytes();
    }
}
