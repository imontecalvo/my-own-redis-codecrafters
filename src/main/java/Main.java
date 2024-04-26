import RedisServer.Settings;

public class Main {
    public static void main(String[] args) {
        Settings.set(ArgsParser.parse(args));

        RedisServer server = Settings.isReplica() ? new RedisServer() : new RedisReplicaServer();
        server.run();
    }
}