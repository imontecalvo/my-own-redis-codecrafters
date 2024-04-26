package RedisServer.resp;

import RedisServer.resp.data_types.DataType;
import RedisServer.resp.data_types.RedisBulkString;

import java.util.HashMap;
import java.util.Optional;

public class Storage {
    private final HashMap<String, StorageValue> storage;

    public Storage() {
        this.storage = new HashMap<>();
    }

    public synchronized void put(String key, DataType value, Optional<Long> ttl){
        StorageValue sValue = new StorageValue(value, ttl);
        storage.put(key, sValue);
    }

    public DataType get(String key){
        StorageValue sValue = storage.get(key);
        if (sValue==null) return RedisBulkString.nullString();

        if (sValue.isExpired()){
            synchronized (storage){
                storage.remove(key);
                return RedisBulkString.nullString();
            }
        }

        return sValue.get();
    }
}
