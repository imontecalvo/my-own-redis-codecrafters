package RedisServer.resp;

import RedisServer.resp.commands2.Command;
import RedisServer.resp.commands2.Ping;
import RedisServer.resp.commands2.Psync;
import RedisServer.resp.commands2.ReplConf;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

import java.io.IOException;

public class CommandFactory {
/*    public static Command createCommand(Request request){
        String command = request.getCommand().toUpperCase();
        System.out.println("Recibi: "+command);
        return switch (command) {
            case "PING" -> new Ping();
            case "ECHO" -> new Echo(request);
            case "SET" -> Set.fromRequest(request);
            case "GET" -> new Get(request);
            case "INFO" -> new Info(request);
            case "REPLCONF" -> ReplConf.fromRequest(request);
            case "PSYNC" -> new Psync();
            case "WAIT" -> Wait.fromRequest(request);
            default -> null;
        };
    }*/

    public static Command create(RedisArray request) throws IOException {
        String command = ((RedisBulkString) request.getElement(0)).getContent().toUpperCase();
        return switch (command) {
            case "PING" -> new Ping();
            case "PSYNC" -> new Psync(request);
            case "REPLCONF" -> new ReplConf(request);
            default -> null;
        };
    }
}
/*
* TODO:
*  1. Cambiar todo a .fromRequest()
*  2. Crear constructor en base a parametros
*  3. Intentar modelar distinto para evitar el switch case
* */
