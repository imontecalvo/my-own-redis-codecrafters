package resp.commands;

import resp.data_types.DataType;

import java.util.HashMap;

public interface Command {
    public byte[] execute(HashMap<String, DataType> storage);
}
