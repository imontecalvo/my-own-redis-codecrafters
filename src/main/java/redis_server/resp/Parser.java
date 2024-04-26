package redis_server.resp;

import redis_server.RedisSocket;
import redis_server.resp.data_types.*;

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
