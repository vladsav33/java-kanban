package http;

import org.junit.jupiter.api.*;
import java.io.IOException;

class KVTaskClientTest {
    KVServer kvServer;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterEach
    void afterEach() throws IOException {
        kvServer.stop();
    }

    @Test
    void saveDuplicate() {
        KVTaskClient client = new KVTaskClient("http://localhost:8078");
        client.put("1", "one");
        String result = client.load("1");
        Assertions.assertEquals(result, "one");
        client.put("1", "two");
        result = client.load("1");
        Assertions.assertEquals(result, "two");
    }

    @Test
    void loadNull() {
        KVTaskClient client = new KVTaskClient("http://localhost:8078");
        String result = client.load("1");
        Assertions.assertNull(result);
    }
}