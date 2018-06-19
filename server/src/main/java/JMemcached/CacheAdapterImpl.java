package JMemcached;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class CacheAdapterImpl implements CacheAdapter {
    private Cache<Object> cache;
    private static CacheAdapter instance;

    private CacheAdapterImpl() {
        cache = new Cache<Object>(600);
    }

    public static CacheAdapter getInstance() {
        if (instance == null) {
            instance = new CacheAdapterImpl();
        }
        return instance;
    }

    @Override
    public void put(Object value, String key, Optional time) {
        cache.put(key, value, time);
    }

    public void get(String key, ObjectOutputStream serializer) throws IOException {
        SerializibleObject serialization = new SerializibleObject(cache.get(key), null, null, null);
        serializer.writeObject(serialization);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.removeAll();
    }


}

