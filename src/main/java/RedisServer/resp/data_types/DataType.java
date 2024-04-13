package RedisServer.resp.data_types;

public interface DataType {

    public default byte[] toBytes() {
        return encode().getBytes();
    }

    public String encode();

    public void print();
}
