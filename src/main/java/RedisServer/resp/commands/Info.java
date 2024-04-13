package RedisServer.resp.commands;

import RedisServer.Settings;
import RedisServer.resp.Request;
import RedisServer.resp.Storage;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisBulkString;

public class Info implements Command {
    public Info(Request request) {
        DataType[] args = request.getArgs();
        if (args.length != 1 || !((RedisBulkString) args[0]).getContent().equalsIgnoreCase("REPLICATION")) {
            System.out.println("ERROR INFO COMMAND");
        }
    }

    @Override
    public byte[] execute(Storage storage) {
        String role = Settings.getRole();
        String masterReplId = Settings.getMasterReplicationId();
        int masterReplOffset = Settings.getMasterReplicationOffset();
        String rawResponse = String.format("role:%s\r\nmaster_replid:%s\r\nmaster_repl_offset:%d", role, masterReplId, masterReplOffset);
        RedisBulkString response = new RedisBulkString(rawResponse);
        return response.toBytes();
    }
}
