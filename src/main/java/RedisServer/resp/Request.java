package RedisServer.resp;

import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

import java.io.BufferedReader;
import java.io.IOException;

public class Request {
    private String command;
    private DataType[] args;

    public Request(String command, DataType[] args) {
        this.command = command;
        this.args = args;
    }

    public static Request fromBytes(BufferedReader reader) throws IOException {
        DataType recv = Parser.fromBytes(reader);
        if (!(recv instanceof RedisArray arrayRequest)){
            return null;
        }

        String command = ((RedisBulkString) arrayRequest.getElement(0)).getContent();
        return new Request(command, arrayRequest.slice(1,-1));
    }

    public String getCommand() {
        return command;
    }

    public DataType[] getArgs() {
        return args;
    }
}
