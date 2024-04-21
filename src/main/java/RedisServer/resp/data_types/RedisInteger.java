package RedisServer.resp.data_types;

import java.io.BufferedReader;
import java.io.IOException;

public class RedisInteger implements DataType{
    private int content;
    private static final char PREFIX = ':';
    private static final String SUFFIX = "\r\n";

    public RedisInteger(int content){
        this.content = content;
    }

    public static RedisInteger fromBytes(BufferedReader reader) throws IOException {
        String recv = reader.readLine();
        if (recv.charAt(0) == PREFIX){
            int startIndex = 1;
            if (recv.charAt(1) == '-' || recv.charAt(1) == '+'){
                startIndex = 2;
            }
            return new RedisInteger(Integer.parseInt(recv.substring(startIndex)));
        }
        return null;
    }

    @Override
    public String encode() {
        String sign = content < 0 ? "-" : "";
        return PREFIX+sign+String.valueOf(content)+SUFFIX;
    }

    @Override
    public void print() {
        System.out.printf("Contenido: %d\n", content);
    }

    @Override
    public int getNumberOfBytes() {
        return this.encode().length();
    }
}
