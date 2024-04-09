package resp;

import resp.data_types.DataType;
import resp.data_types.RedisArray;
import resp.data_types.RedisBulkString;
import resp.data_types.RedisString;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class Request {
    private String command;
    private DataType[] args;

    public Request(String command, DataType[] args) {
        this.command = command;
        this.args = args;
    }

    public static Request fromBytes(BufferedReader reader) throws IOException {
        char[] r = new char[1];

        if (reader.read(r) > 0) {
            return switch (r[0]) {
                case '+' -> simpleRequest(RedisString.fromBytes(reader));
                case '$' -> simpleRequest(RedisBulkString.fromBytes(reader));
                case '*' -> compoundRequest(RedisArray.fromBytes(reader));
                default -> null;
            };
        }
        return null;
    }

    public static Request simpleRequest(RedisString command){
        return new Request(command.getContent(), null);
    }

    public static Request simpleRequest(RedisBulkString command){
        return new Request(command.getContent(), null);
    }

    public static Request compoundRequest(RedisArray array){
        String command = ((RedisBulkString) array.getElement(0)).getContent();
        return new Request(command, array.slice(1,-1));
    }

    public String getCommand() {
        return command;
    }

    public DataType[] getArgs() {
        return args;
    }
}
