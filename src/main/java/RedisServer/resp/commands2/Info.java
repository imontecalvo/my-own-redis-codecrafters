package RedisServer.resp.commands2;

import RedisServer.RedisSocket;
import RedisServer.Settings;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

import java.io.IOException;

public class Info extends Command{
    public static final String COMMAND = "INFO";
    private String arg;

    public Info(String arg) {
        this.arg = arg;
    }

    public Info(RedisArray request) throws IOException {
        if (request.length()!=2) throw new IOException();
        this.arg = ((RedisBulkString) request.getElement(1)).getContent();
    }

    @Override
    public void execute() {}

    @Override
    public void respond(RedisSocket socket) throws IOException {
        String role = Settings.getRole();
        String masterReplId = Settings.getMasterReplicationId();
        int masterReplOffset = Settings.getMasterReplicationOffset();
        String rawResponse = String.format("role:%s\r\nmaster_replid:%s\r\nmaster_repl_offset:%d", role, masterReplId, masterReplOffset);

        RedisBulkString response = new RedisBulkString(rawResponse);
        socket.writeBytes(response.toBytes());
    }

    @Override
    public byte[] encode() throws IOException {
        RedisArray msg = RedisArray.bulkStringArray(new String[]{COMMAND, arg});
        return msg.toBytes();
    }
}
