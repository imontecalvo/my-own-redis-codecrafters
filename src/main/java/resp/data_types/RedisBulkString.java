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
        char[] crlf = new char[2];

        StringBuilder string = new StringBuilder();

        if (reader.read(r) > 0) {
            if (r[0]=='-') return RedisBulkString.nullString();
            int length = Integer.parseUnsignedInt(String.copyValueOf(r));

            if (reader.read(crlf) > 0 && crlf[0] == '\r' && crlf[1] == '\n') {
                for (int i=0; i<length;i++){
                    reader.read(r);
                    string.append(r[0]);
                }
                return new RedisBulkString(string.toString());
            }
        }
        throw new IOException();
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
