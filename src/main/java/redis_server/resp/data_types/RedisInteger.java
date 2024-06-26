package redis_server.resp.data_types;

import redis_server.RedisSocket;

import java.io.IOException;

public class RedisInteger implements DataType{
    private int content;
    public static final char PREFIX = ':';
    private static final String SUFFIX = "\r\n";

    public RedisInteger(int content){
        this.content = content;
    }

    public static RedisInteger fromBytes(RedisSocket socket) throws IOException {
        String recv = socket.readLine();

        int startIndex = 0;
        if (recv.charAt(startIndex) == '-' || recv.charAt(startIndex) == '+') {
            startIndex = 1;
        }

        return new RedisInteger(Integer.parseInt(recv.substring(startIndex)));
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
}
