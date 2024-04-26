package RedisServer;

import RedisServer.resp.CommandFactory;
import RedisServer.resp.Parser;
import RedisServer.resp.Propagator;
import RedisServer.resp.Storage;
import RedisServer.resp.commands2.Command;
import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisArray;

import java.io.IOException;

public class ClientConnection extends Connection {
    public ClientConnection(RedisSocket socket, Storage storage, Propagator propagator) {
        super(socket, storage, propagator);
    }

    @Override
    public void handle() {
        while (!socket.isClosed()) {
            try {
                DataType request = Parser.fromBytes(socket);
                if (!(request instanceof RedisArray)) continue;

                Command command = CommandFactory.create((RedisArray) request);
                command.bindConnection(this);
                command.execute();
                command.respond();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
