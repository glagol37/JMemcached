package JMemcached;

public interface JMemcachedClient {
	void put(String key, Object value);
	void put(String key, Object value, long time);
	void clear();
	void remove(String key);
	Object get(String key);
}
