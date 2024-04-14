package RedisServer;

import RedisServer.resp.Parser;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Settings {
    private static final int DEF_PORT = 6379;

    private static final String MASTER_REPLID = "8371b4fb1155b71f4a04d3e1bc3e18c4a990aeeb";
    private static final int MASTER_REPL_OFFSET = 0;
    private static HashMap<String, String[]> settings;

    private static List<OutputStream> replicas;

    public static void set(HashMap<String, String[]> settings) {
        Settings.settings = settings;
        //replicas = new ArrayList<>();
    }


    public static int getPort(){
        if (settings!=null && settings.containsKey("--port")){
            try{
                return Integer.parseInt(settings.get("--port")[0]);
            } catch (NumberFormatException e) {
                return DEF_PORT;
            }
        }
        return DEF_PORT;
    }

    public static String getRole(){
        if (settings!=null) return settings.containsKey("--replicaof") ? "slave" : "master";
        return null;
    }

    public static Boolean isReplica(){
        return settings.containsKey("--replicaof");
    }


    public static String getMasterReplicationId(){
        return MASTER_REPLID;
    }

    public static int getMasterReplicationOffset(){
        return MASTER_REPL_OFFSET;
    }

    public static String getMasterReplicationAddress() {
        if (settings!=null && settings.containsKey("--replicaof")){
            return settings.get("--replicaof")[0];
        }
        return null;
    }

    public static int getMasterReplicationPort() {
        if (settings!=null && settings.containsKey("--replicaof")){
            return Integer.parseInt(settings.get("--replicaof")[1]);
        }
        return 0;
    }

    public synchronized static void addReplica(OutputStream out) {
        replicas.add(out);
    }

    public static List<OutputStream> getReplicas(){
        return replicas;
    }
}
