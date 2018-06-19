
package JMemcached;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class JMemcachedClientImpl implements JMemcachedClient {
	private static final String PUT = "put";
	private static final String REMOVE = "remove";
	private static final String GET = "get";
	private static final long DEFAULT_CACHING_TIME = 600L;
	private String host;
	private int port;

	public JMemcachedClientImpl(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void put(String key, Object value) {
		dataTransfer(value, key, PUT, DEFAULT_CACHING_TIME);
	}

	public void put(String key, Object value, long time) {
		dataTransfer(value, key, PUT, time);
	}

	public void clear() {
		dataTransfer(0, "1", "clear", 0L);
	}

	public void remove(String key) {
		dataTransfer(0, key, REMOVE, 0L);
	}

	@Override
	public Object get(String key) {
		Object value = null;
		try (Socket socket = new Socket(host, port);
				ObjectOutputStream serializer = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream imput = new ObjectInputStream(socket.getInputStream())) {
			SerializibleObject serialization = new SerializibleObject(0, key, GET, null);
			serializer.writeObject(serialization);
			SerializibleObject object = (SerializibleObject) imput.readObject();
			value = object.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;

	}

	private void dataTransfer(Object value, String key, String action, Long time) {
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			socket.setKeepAlive(true);
			ObjectOutputStream serializer = new ObjectOutputStream(socket.getOutputStream());
			SerializibleObject serialization = new SerializibleObject(value, key, action, time);
			serializer.writeObject(serialization);
			serializer.flush();
			serializer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

		}

	}
}
