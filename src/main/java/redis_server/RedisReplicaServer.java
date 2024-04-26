package redis_server;

import redis_server.connection.MasterConnection;

import java.io.IOException;

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
