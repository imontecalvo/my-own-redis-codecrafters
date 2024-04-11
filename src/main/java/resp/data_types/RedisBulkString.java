package resp.data_types;

import java.io.BufferedReader;
import java.io.IOException;

public class RedisBulkString implements DataType {

    private String content;

    public RedisBulkString(String content) {
        this.content = content;
    }

    public static RedisBulkString nullString(){
        return new RedisBulkString(null);
    }

    public static RedisBulkString fromBytes(BufferedReader reader) throws IOException {
        char[] r = new char[1];
        StringBuilder string = new StringBuilder();

        int length = readStringLength(reader);
        if (length < 0) return RedisBulkString.nullString();

        for (int i=0; i<length;i++){
            reader.read(r);
            string.append(r[0]);
        }
        return new RedisBulkString(string.toString());
    }

    private static Integer readStringLength(BufferedReader reader) throws IOException {
        StringBuilder length = new StringBuilder();
        char[] r = new char[1];

        while (reader.read(r) > 0 && r[0]!='\r'){
            length.append(r[0]);
        }
        reader.read(r);
        return Integer.parseInt(length.toString());
    }

    @Override
    public byte[] toBytes() {
        if (content==null){
            return "$-1\r\n".getBytes();
        }else if (content.isEmpty()){
            return "$0\r\n".getBytes();
        }
        return String.format("$%d\r\n%s\r\n", this.content.length(), this.content).getBytes();
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
