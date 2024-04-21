package RedisServer.resp.data_types;

import java.io.BufferedReader;
import java.io.IOException;

public class RedisString implements DataType {

    private String content;
    private static final String PREFIX = "+";

    public RedisString(String content) {
        this.content = content;
    }

    public static RedisString fromBytes(BufferedReader reader) throws IOException {
        char[] r = new char[1];

        StringBuilder content = new StringBuilder();

        while (reader.read(r) > 0 && r[0] != '\r') {
            content.append(r[0]);
        }
        ;
        if (reader.read(r) == 0 || r[0] != '\n') {
            throw new IOException();
        }

        return new RedisString(content.toString());
    }

    @Override
    public String encode() {
        return String.format("%s%s\r\n", PREFIX, this.content);
    }

    @Override
    public void print() {
        System.out.println("Contenido: "+this.content);
    }

    @Override
    public int getNumberOfBytes() {
        return PREFIX.length()+content.length();
    }

    public String getContent() {
        return content;
    }

}
