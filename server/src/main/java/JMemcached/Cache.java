package JMemcached;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cache<V> {
	 private static final Logger LOG = LoggerFactory.getLogger(CLI.class);
    private volatile ConcurrentHashMap<Key, V> globalMap = new ConcurrentHashMap<Key, V>();
    private long defaultTimeout;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread th = new Thread(r);
            th.setDaemon(true);
            return th;
        }
    });

    public Cache(long default_timeout) {
        this.defaultTimeout = default_timeout;
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                long current = System.currentTimeMillis();
                for (Key k : globalMap.keySet()) {
                    if (!k.isLive(current)) {
                        globalMap.remove(k);
                    }
                }
            }
        }, 1, default_timeout, TimeUnit.SECONDS);
    }

    public void put(String key, V value, Optional<Long> timeout) {
        globalMap.put(new Key(key, timeout.orElse(defaultTimeout)), value);
    }

    public V get(String key) {
        return globalMap.get(new Key(key));
    }

    public void remove(Object key) {
        globalMap.remove(new Key(key));
    }

    public void removeAll()
    {
        globalMap.clear();
    }

    private static class Key {

        private final Object key;
        private final long timelife;

        public Key(Object key, long timeout) {
            this.key = key;
            this.timelife = System.currentTimeMillis() + timeout;
        }

        public Key(Object key) {
            this.timelife = System.currentTimeMillis() + 10;
            this.key = key;
        }


        public boolean isLive(long currentTimeMillis) {
            return currentTimeMillis < timelife;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + (this.key != null ? this.key.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return "Key{" + "key=" + key + '}';
        }
    }

}