package RedisServer.resp;

import RedisServer.RedisSocket;
import RedisServer.resp.data_types.*;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {
    public static DataType fromBytes(RedisSocket socket) {
        try {
            char[] r = socket.readChars(1);
            return switch (r[0]) {
                case RedisString.PREFIX -> RedisString.fromBytes(socket);
                case RedisBulkString.PREFIX -> RedisBulkString.fromBytes(socket);
                case RedisArray.PREFIX -> RedisArray.fromBytes(socket);
                case RedisInteger.PREFIX -> RedisInteger.fromBytes(socket);
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }
}
