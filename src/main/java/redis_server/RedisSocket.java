package redis_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class RedisSocket {
    private Socket socket;
    private OutputStream writer;
    private BufferedReader reader;

    public RedisSocket(String address, int port) throws IOException {
        socket = new Socket(address, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = socket.getOutputStream();
    }

    public RedisSocket(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = socket.getOutputStream();
    }

    /*
     * Lee del socket una cantidad n de caracteres
     * En caso de exito retorna los caracteres leidos
     * En caso de error, dispara IOException;
     * */
    public char[] readChars(int n) throws IOException {
        char[] r = new char[n];
        if (reader.read(r)==0) throw new IOException();
        return r;
    }

    /*
     * Lee string hasta salto de linea del socket
     * En caso de exito retorna el string
     * En caso de error, dispara IOException;
     * */
    public String readLine() throws IOException {
        String string = reader.readLine();
        return string;
    }

    /*
     * Escribe en el sockets los bytes que recibe
     * En caso de error, dispara IOException;
     * */
    public void writeBytes(byte[] bytes) throws IOException {
        writer.write(bytes);
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public String readString(int length) throws IOException {
        char[] chars = new char[length];
        reader.read(chars);
        return new String(chars);
    }
}
