package resp;

import resp.data_types.DataType;
import resp.data_types.RedisBulkString;

import java.util.HashMap;
import java.util.Optional;

public class Storage {
    private final HashMap<String, StorageValue> storage;

    public Storage() {
        this.storage = new HashMap<>();
    }

    public void put(String key, DataType value, Optional<Long> ttl){
        StorageValue sValue = new StorageValue(value, ttl);
        storage.put(key, sValue);
    }

    public DataType get(String key){
//        System.out.println("LLega "+key);
        StorageValue sValue = storage.get(key);
        if (sValue==null) return RedisBulkString.nullString();
//        System.out.println("LLega2");
        if (sValue.isExpired()){
            storage.remove(key);
            return RedisBulkString.nullString();
        }
//        System.out.print("LLega3. Se obtiene:");
        sValue.get().print();

        return sValue.get();
    }
}
