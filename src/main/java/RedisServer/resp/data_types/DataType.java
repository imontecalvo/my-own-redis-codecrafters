package RedisServer.resp.data_types;

import java.io.BufferedReader;

public interface DataType {

    public default byte[] toBytes() {
        return encode().getBytes();
    }

    public String encode();

    public void print();

    public int getNumberOfBytes();

}

/*
* TODO:
*  1. Estandarizar parseo -> fromBytes
*  2. Añadir chequeos al parsear, si falla retornar null o lanzar excepcion.
* */