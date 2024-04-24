package RedisServer;

public class AckCounter {
    private static int limit;
    private static int counter = 0;
    public static Object ackLock = new Object();

    public static synchronized void setLimit(int limit) {
        AckCounter.limit = limit;
    }

    public static boolean isReached() {
        return limit <= counter;
    }

    public synchronized static void reset() {
        limit = 0;
        counter = 0;
    }

    public static int getCounter() {
        return counter;
    }

    public static void newAck() {
        synchronized (ackLock){
            counter++;
            if (counter >= limit) ackLock.notify();
        }
    }
}
