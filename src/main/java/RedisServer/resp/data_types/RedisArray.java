package RedisServer.resp.data_types;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import RedisServer.resp.Parser;

public class RedisArray implements DataType{

    private final DataType[] content;
    public static final char PREFIX = '*';

    public RedisArray(DataType[] content) {
        this.content = content;
    }

    public static RedisArray fromBytes(BufferedReader reader) throws IOException {
        int length = Integer.parseInt(reader.readLine());
        DataType[] content = new DataType[length];
        for (int i=0; i<length;i++){
            content[i] = Parser.parse(reader);
            char[] crlf = new char[2];
            if (i<length-1) reader.read(crlf);
        }

        return new RedisArray(content);
    }

    @Override
    public String encode() {
        int length = content.length;
        StringBuilder str = new StringBuilder(PREFIX + length + "\r\n");
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

    @Override
    public int getNumberOfBytes() {
        int bytesCounter = 1+String.valueOf(content.length).length()+"\r\n".length();
        for (DataType d : content){
            bytesCounter += d.getNumberOfBytes();
        }
        return bytesCounter;
    }

    public DataType getElement(int i) {
        return content[0];
    }

    public DataType[] slice(int start, int end) {
        if (end<0) end= content.length;
        return Arrays.copyOfRange(content, start, end);
    }
}
