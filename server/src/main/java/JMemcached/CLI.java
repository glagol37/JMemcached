package JMemcached;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLI {

	private static final Logger LOG = LoggerFactory.getLogger(CLI.class);
	private static final int PORT = 9090;

	public static void main(String[] args) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(PORT);) {
			ExecutorService r = Executors.newFixedThreadPool(10);
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					r.execute(new WorkerThread(new ObjectOutputStream(socket.getOutputStream()),
							new ObjectInputStream(socket.getInputStream())));
				} catch (IOException e) {
					LOG.error(e.getMessage());
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}
		}

	}
}