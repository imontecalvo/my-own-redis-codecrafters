package redis_server.resp.data_types;

import redis_server.RedisSocket;

import java.io.IOException;

public class RedisBulkString implements DataType {

    private String content;
    public static final char PREFIX = '$';
    private static final String SUFFIX = "\r\n";

    public RedisBulkString(String content) {
        this.content = content;
    }

    public static RedisBulkString nullString(){
        return new RedisBulkString(null);
    }

    public static RedisBulkString fromBytes(RedisSocket socket) throws IOException {
        int length = Integer.parseInt(socket.readLine());
        if (length < 0) return RedisBulkString.nullString();

        String content = socket.readString(length);
        socket.readLine();//Leo crlf -> \r\n
        if (content.length() != length) throw new IOException();

        return new RedisBulkString(content);
    }

    @Override
    public String encode() {
        if (content==null){
            return PREFIX+"-1"+SUFFIX;
        }else if (content.isEmpty()){
            return PREFIX+"0"+SUFFIX;
        }
        return String.format("%s%d\r\n%s%s", PREFIX, this.content.length(), this.content, SUFFIX);
    }

    public void print() {
        if (content!=null){
            System.out.println("Contenido: "+this.content);
        }else{
            System.out.println("Contenido: NullString");
        }
    }

    public String getContent() {
        return content;
    }

}
