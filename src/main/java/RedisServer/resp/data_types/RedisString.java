package RedisServer.resp.data_types;

import java.io.BufferedReader;
import java.io.IOException;

public class RedisString implements DataType {

    private String content;
    public static final char PREFIX = '+';

    public RedisString(String content) {
        this.content = content;
    }

    public static RedisString fromBytes(BufferedReader reader) throws IOException {
        String content = reader.readLine();

        return new RedisString(content);
    }

    @Override
    public String encode() {
        return String.format("%s%s\r\n", PREFIX, this.content);
    }

    @Override
    public void print() {
        System.out.println("Contenido: "+this.content);
    }

    public String getContent() {
        return content;
    }
}
