package RedisServer.resp;

import RedisServer.resp.commands2.*;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

import java.io.IOException;

public class CommandFactory {
    public static Command create(RedisArray request) throws IOException {
        String command = ((RedisBulkString) request.getElement(0)).getContent().toUpperCase();
        return switch (command) {
            case Echo.COMMAND -> new Echo(request);
            case Ping.COMMAND -> new Ping();
            case Info.COMMAND -> new Info(request);
            case Get.COMMAND -> new Get(request);
            case Set.COMMAND -> new Set(request);
            case Psync.COMMAND -> new Psync(request);
            case ReplConf.COMMAND -> new ReplConf(request);
            case Wait.COMMAND -> new Wait(request);
            default -> null;
        };
    }
}