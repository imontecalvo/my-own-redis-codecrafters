package resp;

import resp.data_types.DataType;
import resp.data_types.RedisArray;
import resp.data_types.RedisBulkString;
import resp.data_types.RedisString;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {
    public static DataType fromBytes(BufferedReader reader) throws IOException {
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
