package RedisServer.resp.commands2;

import RedisServer.RedisSocket;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.io.IOException;

public class Ping extends Command {
    public static final String COMMAND = "PING";

    @Override
    public void execute() {}

    @Override
    public void respond(RedisSocket socket) throws IOException {
        RedisString response = new RedisString("PONG");
        socket.writeBytes(response.toBytes());
    }

    @Override
    public byte[] encode() throws IOException {
        RedisArray msg = RedisArray.bulkStringArray(new String[]{COMMAND});
        return msg.toBytes();
    }

    public static boolean isValidResponse(DataType response) {
        return response instanceof RedisString && ((RedisString) response).getContent().equalsIgnoreCase("PONG");
    }
}
