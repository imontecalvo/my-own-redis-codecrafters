import java.util.Arrays;

public class Request {
    String type;
    String[] arguments;
    public Request(char[] data) {
        String[] parsed_data = String.valueOf(data).trim().split(" ");
        this.type = parsed_data[0].toUpperCase();
        this.arguments = parsed_data.length > 1 ? Arrays.copyOfRange(parsed_data, 1, parsed_data.length) : null;
    }

    public byte[] getResponse(){
        return switch (this.type) {
            case "PING" -> "+PONG\r\n".getBytes();
            case "ECHO" -> String.format("$%d\r\n%s\\r\n",this.arguments[0].length(),this.arguments[0]).getBytes();
            default -> "+unkown\r\n".getBytes();
        };
    }
}
