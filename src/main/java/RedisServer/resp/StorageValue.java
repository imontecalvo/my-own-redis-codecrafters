package RedisServer.resp;

import RedisServer.resp.data_types.DataType;

import java.util.Date;
import java.util.Optional;

public class StorageValue {
    private DataType value;
    private Date createdAt;
    private Optional<Long> ttl;

    public StorageValue(DataType value, Optional<Long> ttl) {
        this.value = value;
        this.createdAt = new Date();
        this.ttl = ttl;
    }

    public DataType get(){
        return value;
    }

    public Boolean isExpired(){
        if (ttl.isEmpty()) return false;
        return createdAt.getTime() + ttl.get() <= System.currentTimeMillis();
    }
}