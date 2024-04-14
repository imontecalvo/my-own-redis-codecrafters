package RedisServer.resp;

import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisString;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {
    public static DataType fromBytes(BufferedReader reader) throws IOException {
        DataType data = parse(reader);
        if (data!=null){
            if (!(data instanceof RedisString) && !(data instanceof RedisBulkString && ((RedisBulkString) data).isNull())) {
                char[] crlf = new char[2];
                reader.read(crlf);
            }
            return data;
        }
        return null;
    }

    public static DataType parse(BufferedReader reader) throws IOException {
        char[] r = new char[1];

        if (reader.read(r) > 0) {
            return switch (r[0]) {
                case '+' -> RedisString.fromBytes(reader);
                case '$' -> RedisBulkString.fromBytes(reader);
                case '*' -> RedisArray.fromBytes(reader);
                default -> null;
            };
        }
        return null;
    }
}
