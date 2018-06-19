package JMemcached;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

public interface CacheAdapter {
	void put(Object value, String key, Optional time);
	void get(String key, ObjectOutputStream output) throws IOException;
	void remove(String key);
	void clear();
}

