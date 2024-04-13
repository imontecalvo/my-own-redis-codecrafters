package RedisServer.resp.commands;

import RedisServer.resp.Storage;

public interface Command {
    public byte[] execute(Storage storage);
}
