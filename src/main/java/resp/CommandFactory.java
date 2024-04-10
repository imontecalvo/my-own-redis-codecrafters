package resp;

import resp.commands.*;
import resp.data_types.RedisBulkString;
import resp.data_types.RedisString;

public class CommandFactory {
    public static Command createCommand(Request request){
        String command = request.getCommand().toUpperCase();
        return switch (command) {
            case "PING" -> new Ping();
            case "ECHO" -> new Echo(request);
            case "SET" -> new Set(request);
            case "GET" -> new Get(request);
            default -> null;
        };
    }
}
