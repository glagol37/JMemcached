package JMemcached;
public class JMemcachedClientFactory {
    private static final int DEFAULT_PORT = 9090;
    private static final String DEFAULT_HOST = "localhost";
    private String host;
    private int port;

    public JMemcachedClientFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public JMemcachedClientFactory() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
    }

    public JMemcachedClient getClient(){
        return new JMemcachedClientImpl(host,port);
    }


}
