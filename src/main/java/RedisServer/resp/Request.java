package RedisServer.resp;

import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;

import java.io.BufferedReader;
import java.io.IOException;

public class Request {
    private final String command;
    private final DataType[] args;
    private final int numberOfBytes;

    public Request(String command, DataType[] args, int numberOfBytes) {
        this.command = command;
        this.args = args;
        this.numberOfBytes = numberOfBytes;
    }

    public static Request fromBytes(BufferedReader reader) throws IOException {
        DataType recv = Parser.fromBytes(reader);
        if (!(recv instanceof RedisArray arrayRequest)){
            return null;
        }

        String command = ((RedisBulkString) arrayRequest.getElement(0)).getContent();
        return new Request(command, arrayRequest.slice(1,-1),recv.getNumberOfBytes());
    }

    public String getCommand() {
        return command;
    }

    public DataType[] getArgs() {
        return args;
    }

    public int getNumberOfBytes(){
        return numberOfBytes;
    }
}
