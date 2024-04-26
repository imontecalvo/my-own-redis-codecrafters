package RedisServer.resp.commands2;

import RedisServer.AckCounter;
import RedisServer.RedisSocket;
import RedisServer.Settings;
import RedisServer.resp.data_types.RedisArray;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisInteger;

import java.io.IOException;

public class Wait extends Command{
    public final String COMMAND = "WAIT";
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

    }

    @Override
    public void respond(RedisSocket socket) throws IOException {
        int nOfAcksRecv = AckCounter.getCounter()>0 ? AckCounter.getCounter() : Settings.getNumberOfReplicas();
        AckCounter.reset();
        socket.writeBytes(new RedisInteger(nOfAcksRecv).toBytes());
    }
    @Override
    public byte[] encode() throws IOException {
        String[] args = new String[]{COMMAND, String.valueOf(ackRequired),String.valueOf(timeout)};
        RedisArray msg = RedisArray.bulkStringArray(args);
        return msg.toBytes();
    }
}
