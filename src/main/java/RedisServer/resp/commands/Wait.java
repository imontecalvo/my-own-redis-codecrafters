package RedisServer.resp.commands;

import RedisServer.AckCounter;
import RedisServer.Settings;
import RedisServer.resp.Propagator;
import RedisServer.resp.Request;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisBulkString;
import RedisServer.resp.data_types.RedisInteger;

import java.io.IOException;

public class Wait implements Command{
    private DataType[] args;

    public Wait(DataType[] args) {
        this.args = args;
    }

    public static Wait fromRequest(Request request){
        return new Wait(request.getArgs());
    }
    @Override
    public byte[] getResponse() {
        int nOfAckRequired = Integer.parseInt(((RedisBulkString) args[0]).getContent());
        int timeout = Integer.parseInt(((RedisBulkString) args[1]).getContent());

        //Settings.setAckCounter(nOfAckRequired);
        AckCounter.setLimit(nOfAckRequired);

        try{
            Propagator.askForAcknowledge();
        }catch(IOException e){
            System.out.println(e);
        }

        if (!AckCounter.isReached() && timeout > 0) {
            try{
                synchronized (AckCounter.ackLock) {
                    AckCounter.ackLock.wait(timeout);
                }
            }catch (InterruptedException e){
                System.out.println(e);
            }
        }

        int nOfAcksRecv = AckCounter.getCounter()>0 ? AckCounter.getCounter() : Settings.getNumberOfReplicas();
        AckCounter.reset();
        return new RedisInteger(nOfAcksRecv).toBytes();
    }
    /*
     * TODO:
     *  Si da timeout -> devolver numero de replicas
     * Sino, devolver numero de respuestas
     *
     * Creo que voy a tener que cambiar el contador y cada vez que me llegue un ACK hacer +1.
     * Para chequear si ya está, me fijo si el contador es >= a lo pedido
     * */
}
