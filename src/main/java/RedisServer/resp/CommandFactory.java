package RedisServer.resp;

import RedisServer.resp.commands.*;

public class CommandFactory {
    public static Command createCommand(Request request){
        String command = request.getCommand().toUpperCase();
        return switch (command) {
            case "PING" -> new Ping();
            case "ECHO" -> new Echo(request);
            case "SET" -> Set.fromRequest(request);
            case "GET" -> new Get(request);
            case "INFO" -> new Info(request);
            case "REPLCONF" -> ReplConf.fromRequest(request);
            case "PSYNC" -> new Psync();
            default -> null;
        };
    }
}
