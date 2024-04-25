package RedisServer.resp;

import RedisServer.resp.data_types.*;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {
    public static DataType fromBytes(BufferedReader reader) {
        try {
            char[] r = new char[1];
            reader.read(r);
            return switch (r[0]) {
                case RedisString.PREFIX -> RedisString.fromBytes(reader);
                case RedisBulkString.PREFIX -> RedisBulkString.fromBytes(reader);
                case RedisArray.PREFIX -> RedisArray.fromBytes(reader);
                case RedisInteger.PREFIX -> RedisInteger.fromBytes(reader);
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }
}
