package JMemcached;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class StartClient {

    public static void main(String... args) throws IOException, InterruptedException {
        JMemcachedClientFactory memcachedClientFactory = new JMemcachedClientFactory();
        JMemcachedClient client = memcachedClientFactory.getClient();
        client.put("test", "Hello world");
        System.out.println(client.get("test"));

        client.remove("test");
        System.out.println(client.get("test"));

        client.put("test", "Hello world");
        client.put("test", new BusinessObject ("TEST"));
        System.out.println(client.get("test"));

        client.clear();
        System.out.println(client.get("test"));

        client.put("devstudy", "Devstudy JMemcached", 2);
        TimeUnit.SECONDS.sleep(3);
        System.out.println(client.get("devstudy"));
        

    }
    private static class BusinessObject implements Serializable {
        private String name;

        BusinessObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("BusinessObject{");
            sb.append("name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

}
