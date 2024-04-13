package RedisServer.resp.commands;

import java.io.IOException;
import java.io.OutputStream;

public interface Command {

    public byte[] getResponse();
    public default void respond(OutputStream output) throws IOException {
        output.write(this.getResponse());
    }
}