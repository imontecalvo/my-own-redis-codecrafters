import RedisServer.RedisSocket;
import RedisServer.Settings;
import RedisServer.resp.Parser;
import RedisServer.resp.commands2.Ping;
import RedisServer.resp.commands2.Psync;
import RedisServer.resp.commands2.ReplConf;
import RedisServer.resp.data_types.RedisString;
import RedisServer.MasterConnection;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

public class RedisReplicaServer extends RedisServer{
    @Override
    public void run(){
        MasterConnection masterConnection = handleConnectionToMaster();
        if (masterConnection==null) return;
        Thread masterConnectionThread = new Thread(masterConnection);

        masterConnectionThread.start(); //Escuchar mensajes de master
        super.run(); //Escuchar mensaje de clientes
    }

    /*
    * Intenta establecer la conexion con el servidor master y hacer el handshake
    * En caso de exito, devuelve la conexion
    * En caso contrario, retorna null
    * */
    private MasterConnection handleConnectionToMaster(){
        try {
            String masterAddress = Settings.getMasterReplicationAddress();
            int masterPort = Settings.getMasterReplicationPort();
            RedisSocket socket = new RedisSocket(masterAddress, masterPort);

            MasterConnection masterConnection = new MasterConnection(socket, storage, propagator);
            masterConnection.handleHandshake();

            return masterConnection;
        } catch (IOException e) {
            System.out.println("Cannot sync to master server.\n" + e);
            return null;
        }
    }

}
