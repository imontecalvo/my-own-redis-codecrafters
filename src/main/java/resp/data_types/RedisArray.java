package resp.data_types;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import resp.Parser;

public class RedisArray implements DataType{

    private final DataType[] content;

    public RedisArray(DataType[] content) {
        this.content = content;
    }

    public static RedisArray fromBytes(BufferedReader reader) throws IOException {
        int length = Integer.parseInt(reader.readLine());
        DataType[] content = new DataType[length];
        for (int i=0; i<length;i++){
            content[i] = Parser.fromBytes(reader);
            char[] crlf = new char[2];
            reader.read(crlf);
        }

        return new RedisArray(content);
    }

    @Override
    public byte[] toBytes() {
        return null;
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
}
