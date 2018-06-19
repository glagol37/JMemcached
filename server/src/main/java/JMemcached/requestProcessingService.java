package JMemcached;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class requestProcessingService {
	private static final Logger LOG = LoggerFactory.getLogger(WorkerThread.class);
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	public static final String PUT = "put";
	public static final String REMOVE = "remove";
	public static final String GET = "get";

	public requestProcessingService(ObjectOutputStream outputStream, ObjectInputStream inputStream) {
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}

	public void processingRequest() {

		try {
			SerializibleObject receivedObject = (SerializibleObject) inputStream.readObject();
			LOG.info(receivedObject.getAction());
			CacheAdapter cacheAdapter = CacheAdapterImpl.getInstance();
			switch (receivedObject.getAction()) {
			case PUT:
				cacheAdapter.put(receivedObject.getValue(), receivedObject.getKey(),
						Optional.ofNullable(receivedObject.getCachingTime()));
				break;
			case GET:
				cacheAdapter.get(receivedObject.getKey(), outputStream);
				break;
			case REMOVE:
				cacheAdapter.remove(receivedObject.getKey());
				break;
			default:
				cacheAdapter.clear();
				break;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
}
