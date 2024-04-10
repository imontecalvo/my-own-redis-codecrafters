package resp.commands;

import resp.Storage;

public interface Command {
    public byte[] execute(Storage storage);
}
