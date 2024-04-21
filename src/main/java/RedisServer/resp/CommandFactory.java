package RedisServer.resp;

import RedisServer.resp.commands.*;

public class CommandFactory {
    public static Command createCommand(Request request){
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
            case "WAIT" -> new Wait();
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
