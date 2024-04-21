package RedisServer.resp;

import RedisServer.resp.data_types.*;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {
    public static DataType fromBytes(BufferedReader reader) throws IOException {
        DataType data = parse(reader);
        if (data!=null){
            //TODO: Mejorar diseño para prescindir de esta parte.
            //Todos los tipos de datos deben leerse hasta el final (\r\n)
            //El array teermina de leerse cuando leo su ultimo elemento.
            // El \r\n del ultimo elemento marca el fin de este, no del array
            if (!(data instanceof RedisString || data instanceof RedisInteger) && !(data instanceof RedisBulkString && ((RedisBulkString) data).isNull())) {
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
                case RedisString.PREFIX -> RedisString.fromBytes(reader);
                case RedisBulkString.PREFIX -> RedisBulkString.fromBytes(reader);
                case RedisArray.PREFIX -> RedisArray.fromBytes(reader);
                case RedisInteger.PREFIX -> RedisInteger.fromBytes(reader);
                default -> null;
            };
        }
        return null;
    }
}
