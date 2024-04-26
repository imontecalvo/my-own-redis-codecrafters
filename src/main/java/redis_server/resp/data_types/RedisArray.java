package redis_server.resp.data_types;

import java.io.IOException;
import java.util.Arrays;

import redis_server.RedisSocket;
import redis_server.resp.Parser;

public class RedisArray implements DataType{

    private final DataType[] content;
    public static final char PREFIX = '*';

    public RedisArray(DataType[] content) {
        this.content = content;
    }

    public static RedisArray fromBytes(RedisSocket socket) throws IOException {
        int length = Integer.parseInt(socket.readLine());
        DataType[] content = new DataType[length];
        for (int i=0; i<length;i++){
            content[i] = Parser.fromBytes(socket);
        }

        return new RedisArray(content);
    }

    public static RedisArray bulkStringArray(String[] args) throws IOException {
        int length = args.length;
        DataType[] content = new DataType[length];
        for (int i=0; i<length;i++){
            content[i] = new RedisBulkString(args[i]);
        }
        return new RedisArray(content);
    }

    @Override
    public String encode() {
        int length = content.length;
        StringBuilder str = new StringBuilder(String.valueOf(PREFIX) + length + "\r\n");
        for (DataType d : content){
            str.append(d.encode());
        }
        return str.toString();
    }

    @Override
    public void print() {
        System.out.println("[");
        for (DataType d: this.content){
            d.print();
        }
        System.out.println("]");
    }

    public DataType getElement(int i) {
        return content[0];
    }

    public DataType[] slice(int start, int end) {
        if (end<0) end= content.length;
        return Arrays.copyOfRange(content, start, end);
    }

    public int length() {
        return content.length;
    }
}
