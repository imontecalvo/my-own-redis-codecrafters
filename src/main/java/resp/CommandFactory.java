package resp;

import resp.commands.Command;
import resp.commands.Echo;
import resp.commands.Ping;
import resp.data_types.RedisBulkString;
import resp.data_types.RedisString;

public class CommandFactory {
    public static Command createCommand(Request request){
        String command = request.getCommand().toUpperCase();
        return switch (command) {
            case "PING" -> new Ping();
            case "ECHO" -> new Echo(request);
            default -> null;
        };
    }
}
