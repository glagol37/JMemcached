package JMemcached;

import java.io.Serializable;

public class SerializibleObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private Object value;
    private String key;
    private String action;
    private Long cachingTime;

    protected SerializibleObject(Object value, String key, String action, Long cachingTime) {
        this.value = value;
        this.key = key;
        this.action = action;
        this.cachingTime = cachingTime;
    }

    protected Object getValue() {
        return value;
    }

    protected String getKey() {
        return key;
    }

    protected String getAction() {
        return action;
    }

    protected Long getCachingTime() {
        return cachingTime;
    }
}