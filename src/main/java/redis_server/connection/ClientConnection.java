package redis_server.connection;

import redis_server.Propagator;
import redis_server.RedisSocket;
import redis_server.resp.CommandFactory;
import redis_server.resp.Parser;
import redis_server.resp.commands.Command;
import redis_server.resp.data_types.DataType;
import redis_server.resp.data_types.RedisArray;
import redis_server.storage.Storage;

import java.io.IOException;
import java.util.Arrays;

public class ClientConnection extends Connection {
    public ClientConnection(RedisSocket socket, Storage storage, Propagator propagator) {
        super(socket, storage, propagator);
    }

    @Override
    public void handle() {
        try {
            while (!socket.isClosed()) {
                DataType request = Parser.fromBytes(socket);
                if (!(request instanceof RedisArray)) return;
                Command command = CommandFactory.create((RedisArray) request);
                command.bindConnection(this);
                command.execute();
                command.respond();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
    }
}
