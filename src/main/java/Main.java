import redis_server.RedisReplicaServer;
import redis_server.RedisServer;
import redis_server.Settings;

public class Main {
    public static void main(String[] args) {
        Settings.set(ArgsParser.parse(args));

        RedisServer server = !Settings.isReplica() ? new RedisServer() : new RedisReplicaServer();
        server.run();
    }
}