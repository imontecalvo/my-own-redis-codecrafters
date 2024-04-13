package RedisServer;

import java.util.HashMap;

public abstract class Settings {
    private static final int DEF_PORT = 6379;

    private static final String MASTER_REPLID = "8371b4fb1155b71f4a04d3e1bc3e18c4a990aeeb";
    private static final int MASTER_REPL_OFFSET = 0;
    private static HashMap<String, String[]> settings;

    public static void set(HashMap<String, String[]> settings) {
        Settings.settings = settings;
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

    public static String getMasterReplicationId(){
        return MASTER_REPLID;
    }

    public static int getMasterReplicationOffset(){
        return MASTER_REPL_OFFSET;
    }
}
