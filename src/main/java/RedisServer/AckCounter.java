package RedisServer;

public class AckCounter {
    private int limit;
    private int counter;
    private static AckCounter instance = null;

    private AckCounter(){
        counter=0;
        limit=0;
    }
    public static AckCounter getInstance(){
        if (instance==null){
            instance = new AckCounter();
        }
        return instance;
    }

    public synchronized void setLimit(int limit) {
        this.limit = limit;
    }

    public synchronized void newAck() {
        counter++;
        if (counter >= limit) notifyAll();
    }

    public synchronized void reset() {
        limit = 0;
        counter = 0;
    }

    public synchronized void waitForAck(int timeout){
        try{
            if (counter < limit){
                wait(timeout);
            }
        }catch (InterruptedException e){
            System.out.println(e);
        }
    }

    public int getCounter() {
        return counter;
    }

}

/*TODO:
* 1. Convertir AckCounter a singleton
* 2. Convertir ACkCounter a Monitor
*   a. Arreglar metodos existentes
*   b.  Crear nuevos metodos necesarios
* 3. Usar AckCounter en Wait y cuando llega ACK
* 4. Ver Offset de ACK
* 5. Ver respuesta de Wait
* */
