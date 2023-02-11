import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.DurationAdapter;
import manager.HttpTaskServer;
import manager.KVServer;
import manager.LocalTimeAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    KVServer kvServer;
    HttpTaskServer httpTaskServer;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.httpServerStart();
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
        httpTaskServer.httpServerStop();
    }

    @Test
    void taskGetHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task taskIn = new Task("Task", "Task 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(taskIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskOut = gson.fromJson(response.body(), Task.class);
        assertEquals(taskIn, taskOut);
    }

    @Test
    void epicGetHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicOut = gson.fromJson(response.body(), Epic.class);
        assertEquals(epicIn, epicOut);
    }

    @Test
    void subTaskGetHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subTaskIn = new SubTask("SubTask", "SubTask 1", 1);
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        jsonString = gson.toJson(subTaskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskOut = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTaskIn, subTaskOut);
    }

    @Test
    void taskDeleteHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task taskIn = new Task("Task", "Task 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(taskIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskOut = gson.fromJson(response.body(), Task.class);
        assertNull(taskOut);
    }

    @Test
    void epicDeleteHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicOut = gson.fromJson(response.body(), Epic.class);
        assertNull(epicOut);
    }

    @Test
    void subTaskDeleteHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subTaskIn = new SubTask("SubTask", "SubTask 1", 1);
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        jsonString = gson.toJson(subTaskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskOut = gson.fromJson(response.body(), SubTask.class);
        assertNull(subTaskOut);
    }

    @Test
    void taskRemoveHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task taskIn = new Task("Task", "Task 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(taskIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskOut = gson.fromJson(response.body(), Task.class);
        assertNull(taskOut);
    }

    @Test
    void epicRemoveHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicOut = gson.fromJson(response.body(), Epic.class);
        assertNull(epicOut);
    }

    @Test
    void subTaskRemoveHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subTaskIn = new SubTask("SubTask", "SubTask 1", 1);
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        jsonString = gson.toJson(subTaskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskOut = gson.fromJson(response.body(), SubTask.class);
    }

    @Test
    void taskShowHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task taskIn = new Task("Task", "Task 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(taskIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskOut = gson.fromJson(response.body(), Task.class);
        assertEquals(taskIn, taskOut);
    }

    @Test
    void epicShowHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicOut = gson.fromJson(response.body(), Epic.class);
        assertEquals(epicIn, epicOut);
    }

    @Test
    void subTaskShowHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subTaskIn = new SubTask("SubTask", "SubTask 1", 1);
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        jsonString = gson.toJson(subTaskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskOut = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTaskIn, subTaskOut);
    }

    @Test
    void taskAddUpdateHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task taskIn = new Task("Task", "Task 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(taskIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        taskIn = new Task(1, Type.TASK, Status.NEW, "Уборка", "Подмести");
        url = URI.create("http://localhost:8080/tasks/task");
        jsonString = gson.toJson(taskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskOut = gson.fromJson(response.body(), Task.class);
        assertEquals(taskIn, taskOut);
    }

    @Test
    void epicAddUpdateHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        epicIn = new Epic(1, Type.TASK, Status.NEW, "Уборка", "Подмести");
        url = URI.create("http://localhost:8080/tasks/epic");
        jsonString = gson.toJson(epicIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicOut = gson.fromJson(response.body(), Epic.class);
        assertEquals(epicIn, epicOut);
    }

    @Test
    void subTaskAddUpdateHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subTaskIn = new SubTask("SubTask", "SubTask 1", 1);
        jsonString = gson.toJson(subTaskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        subTaskIn = new SubTask(2, Type.SUBTASK, Status.NEW, "SubTask", "SubTask 2", 1);
        jsonString = gson.toJson(subTaskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskOut = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTaskIn, subTaskOut);
    }

    @Test
    void epicGetAllHandler() throws IOException, InterruptedException {
        List<SubTask> listIn = new ArrayList<>();
        List<SubTask> listOut = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epicIn = new Epic("Epic", "Epic 1");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(epicIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subTaskIn = new SubTask("SubTask", "SubTask 1", 1);
        listIn.add(subTaskIn);
        jsonString = gson.toJson(subTaskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask");
        subTaskIn = new SubTask("SubTask", "SubTask 2", 1);
        listIn.add(subTaskIn);
        jsonString = gson.toJson(subTaskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/subtask/epic?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String[] jsonLines = response.body().trim().split("\n");
        for (String jsonLine : jsonLines) {
            SubTask subTaskOut = gson.fromJson(jsonLine, SubTask.class);
            listOut.add(subTaskOut);
        }
        assertEquals(listIn, listOut);
    }

    @Test
    void taskPrioritizedHandler() throws IOException, InterruptedException {
        List<Task> listIn = new ArrayList<>();
        List<Task> listOut = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task taskIn2 = new Task(1, Type.TASK, Status.NEW, "Task", "Task 1", LocalTime.of(19, 30), null);
        Task taskIn1 = new Task(2, Type.TASK, Status.NEW, "Task", "Task 1", LocalTime.of(19, 10), null);
        listIn.add(taskIn1);
        listIn.add(taskIn2);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(taskIn2);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task");
        jsonString = gson.toJson(taskIn1);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String[] jsonLines = response.body().trim().split("\n");
        for (String jsonLine : jsonLines) {
            Task TaskOut = gson.fromJson(jsonLine, Task.class);
            listOut.add(TaskOut);
        }
        assertEquals(listIn, listOut);
    }

    @Test
    void historyHandler() throws IOException, InterruptedException {
        List<Task> listIn = new ArrayList<>();
        List<Task> listOut = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task taskIn = new Task(1, Type.TASK, Status.NEW, "Task", "Task 1", LocalTime.of(19, 30), null);
        listIn.add(taskIn);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(taskIn);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonString);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task");
        taskIn = new Task(2, Type.TASK, Status.NEW, "Task", "Task 1", LocalTime.of(19, 10), null);
        listIn.add(taskIn);
        jsonString = gson.toJson(taskIn);
        body = HttpRequest.BodyPublishers.ofString(jsonString);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/task/?id=2");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        url = URI.create("http://localhost:8080/tasks/history");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String[] jsonLines = response.body().trim().split("\n");
        for (String jsonLine : jsonLines) {
            Task TaskOut = gson.fromJson(jsonLine, Task.class);
            listOut.add(TaskOut);
        }
        assertEquals(listIn, listOut);
    }
}