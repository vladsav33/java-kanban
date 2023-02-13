package http;

import com.google.gson.Gson;
import manager.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;
import task.Type;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import static handler.TaskHandler.createGson;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;
    private KVTaskClient client;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        client = new KVTaskClient("http://localhost:8078");
        manager = new HttpTaskManager(new URL("http://localhost:8078"));
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    void save() {
        Gson gson = createGson();

        Task task = new Task(1, Type.TASK, Status.NEW, "Уборка", "Подмести", LocalTime.of(10, 30), null);
        manager.createTask(task);
        String jsonString = gson.toJson(task);
        assertEquals(jsonString, client.load("1"));

        task = new Task(2, Type.TASK, Status.NEW, "Уборка", "Подмести", LocalTime.of(11, 30), null);
        manager.createTask(task);
        jsonString = gson.toJson(task);
        assertEquals(jsonString, client.load("2"));
    }
}