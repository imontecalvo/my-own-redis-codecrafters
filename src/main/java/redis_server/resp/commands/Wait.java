package redis_server.resp.commands;

import redis_server.AckCounter;
import redis_server.RedisSocket;
import redis_server.resp.data_types.RedisArray;
import redis_server.resp.data_types.RedisBulkString;
import redis_server.resp.data_types.RedisInteger;

import java.io.IOException;

public class Wait extends Command{
    public static final String COMMAND = "WAIT";
    private int ackRequired;
    private int timeout;

    public Wait(int ackRequired, int timeout) {
        this.ackRequired = ackRequired;
        this.timeout = timeout;
    }
    public Wait(RedisArray request) throws IOException {
        if (request.length()!=3) throw new IOException();
        String ackRequired = ((RedisBulkString) request.getElement(1)).getContent();
        String timeout = ((RedisBulkString) request.getElement(2)).getContent();

        this.ackRequired = Integer.parseInt(ackRequired);
        this.timeout = Integer.parseInt(timeout);
    }

    @Override
    public void execute() throws IOException {
        checkConnection();
        ReplConf msg = new ReplConf("GETACK","*");
        connection.propagate(msg.encode());

        AckCounter.getInstance().waitForAck(timeout);
    }

    @Override
    public void respond(RedisSocket socket) throws IOException {
        int nOfAcksRecv = AckCounter.getInstance().getCounter();
        RedisInteger response = new RedisInteger(nOfAcksRecv>0 ? nOfAcksRecv : connection.getNumberOfReplicas());
        AckCounter.getInstance().reset();
        socket.writeBytes(response.toBytes());
    }
    @Override
    public byte[] encode() throws IOException {
        String[] args = new String[]{COMMAND, String.valueOf(ackRequired),String.valueOf(timeout)};
        RedisArray msg = RedisArray.bulkStringArray(args);
        return msg.toBytes();
    }
}
