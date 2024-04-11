package resp.commands;

import resp.Request;
import resp.Storage;
import resp.data_types.DataType;
import resp.data_types.RedisArray;
import resp.data_types.RedisBulkString;

public class Info implements Command {
    public Info(Request request) {
        DataType[] args = request.getArgs();
        if (args.length != 1 || !((RedisBulkString) args[0]).getContent().equalsIgnoreCase("REPLICATION")){
            System.out.println("ERROR INFO COMMAND");
        }
    }

    @Override
    public byte[] execute(Storage storage) {
        RedisBulkString response = new RedisBulkString("role:master");
        return response.toBytes();
    }
}
