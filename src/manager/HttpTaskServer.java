package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import task.Epic;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalTime;
import java.util.List;

public class HttpTaskServer {
    private final static int PORT = 8080;
    private HttpServer httpServer;
    public static TaskManager manager;

    public void httpServerStart() throws IOException {
        httpServer = HttpServer.create();
        manager = new InMemoryTaskManager();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/tasks/subtask/epic", new EpicGetAllHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.createContext("/tasks", new TaskPrioritizedHandler());

        httpServer.start();
        System.out.println("HTTP server has started on port: " + PORT);
    }

    public void httpServerStop() {
        httpServer.stop(1);
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String[] parameters = httpExchange.getRequestURI().getPath().split("/");
            switch (method) {
                case "GET":
                    if (httpExchange.getRequestURI().getQuery() != null) {
                        taskGetHandler(httpExchange);
                    } else if (parameters.length == 3) {
                        taskShowHandler(httpExchange);
                    }
                    break;
                case "POST":
                    taskAddUpdateHandler(httpExchange);
                    break;
                case "DELETE":
                    if (httpExchange.getRequestURI().getQuery() != null) {
                        taskRemoveHandler(httpExchange);
                    } else if (parameters.length == 3) {
                        taskDeleteHandler(httpExchange);
                    }
                default:
                    httpExchange.sendResponseHeaders(400, 0);
            }
        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String[] parameters = httpExchange.getRequestURI().getPath().split("/");
            switch (method) {
                case "GET":
                    if (parameters.length == 4) {
                        epicGetHandler(httpExchange);
                    } else if (parameters.length == 3) {
                        epicShowHandler(httpExchange);
                    }
                    break;
                case "POST":
                    epicAddUpdateHandler(httpExchange);
                    break;
                case "DELETE":
                    if (httpExchange.getRequestURI().getQuery() != null) {
                        epicRemoveHandler(httpExchange);
                    } else if (parameters.length == 3) {
                        epicDeleteHandler(httpExchange);
                    }
                default:
                    httpExchange.sendResponseHeaders(400, 0);
            }
        }
    }

    static class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String[] parameters = httpExchange.getRequestURI().getPath().split("/");
            switch (method) {
                case "GET":
                    if (parameters.length == 4) {
                        subTaskGetHandler(httpExchange);
                    } else if (parameters.length == 3) {
                        subTaskShowHandler(httpExchange);
                    }
                    break;
                case "POST":
                    subTaskAddUpdateHandler(httpExchange);
                    break;
                case "DELETE":
                    if (httpExchange.getRequestURI().getQuery() != null) {
                        subTaskRemoveHandler(httpExchange);
                    } else if (parameters.length == 3) {
                        subTaskDeleteHandler(httpExchange);
                    }
                default:
                    httpExchange.sendResponseHeaders(400, 0);
            }
        }
    }

    public static void taskGetHandler(HttpExchange httpExchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String parameters = httpExchange.getRequestURI().getQuery();
        int taskId = Integer.parseInt(parameters.substring(3));
        Task task = manager.getTask(taskId);
        String result = "";
        try {
            result = gson.toJson(task);
        } catch (JsonSyntaxException exception) {
            System.out.println("Incorrect JSON format");
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        }
    }

    public static void epicGetHandler(HttpExchange httpExchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String parameters = httpExchange.getRequestURI().getQuery();
        int epicId = Integer.parseInt(parameters.substring(3));
        Epic epic = manager.getEpic(epicId);
        String result = "";
        try {
            result = gson.toJson(epic);
        } catch (JsonSyntaxException exception) {
            System.out.println("Incorrect JSON format");
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        }
    }

    public static void subTaskGetHandler(HttpExchange httpExchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        String parameters = httpExchange.getRequestURI().getQuery();
        int subTaskId = Integer.parseInt(parameters.substring(3));
        SubTask subTask = manager.getSubtask(subTaskId);
        String result = "";
        try {
            result = gson.toJson(subTask);
        } catch (JsonSyntaxException exception) {
            System.out.println("Incorrect JSON format");
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        }
    }

    public static void taskDeleteHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(204, -1);
        manager.deleteAllTasks();
    }

    public static void epicDeleteHandler(HttpExchange httpExchange) throws IOException {
            httpExchange.sendResponseHeaders(204, -1);
            manager.deleteAllEpics();
    }

    public static void subTaskDeleteHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(204, -1);
        manager.deleteAllSubtasks();
    }

    public static void taskRemoveHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(204, -1);
        String parameters = httpExchange.getRequestURI().getQuery();
        int taskId = Integer.parseInt(parameters.substring(3));
        manager.removeTaskById(taskId);
    }

    public static void epicRemoveHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(204, -1);
        String parameters = httpExchange.getRequestURI().getQuery();
        int epicId = Integer.parseInt(parameters.substring(3));
        manager.removeEpicById(epicId);
    }

    public static void subTaskRemoveHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(204, -1);
        String parameters = httpExchange.getRequestURI().getQuery();
        int subTaskId = Integer.parseInt(parameters.substring(3));
        manager.removeSubtaskById(subTaskId);
    }

    public static void taskShowHandler(HttpExchange httpExchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        List<Task> list = manager.showAllTasks();
        String result = "";
        try {
            for (Task task : list) {
                result += gson.toJson(task) + "\n";
            }
        } catch (JsonSyntaxException exception) {
            System.out.println("Incorrect JSON format");
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        }
    }

    public static void epicShowHandler(HttpExchange httpExchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        List<Epic> list = manager.showAllEpics();
        String result = "";
        try {
            for (Epic epic : list) {
                result += gson.toJson(epic) + "\n";
            }
        } catch (JsonSyntaxException exception) {
            System.out.println("Incorrect JSON format");
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        }
    }

    public static void subTaskShowHandler(HttpExchange httpExchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        List<SubTask> list = manager.showAllSubtasks();
        String result = "";
        try {
            for (SubTask subTask : list) {
                result += gson.toJson(subTask) + "\n";
            }
        } catch (JsonSyntaxException exception) {
            System.out.println("Incorrect JSON format");
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        }
    }

    public static void taskAddUpdateHandler(HttpExchange httpExchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        httpExchange.sendResponseHeaders(200, 0);
        InputStream is = httpExchange.getRequestBody();
        String jsonString = new String(is.readAllBytes());
        try {
            Task task = gson.fromJson(jsonString, Task.class);
            if (manager.getTask(task.getId()) == null) {
                manager.createTask(task);
            } else {
                manager.updateTask(task);
            }
        } catch (JsonSyntaxException exception) {
            System.out.println("Некорректный JSON");
        }
        httpExchange.close();
    }

    public static void epicAddUpdateHandler(HttpExchange httpExchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        httpExchange.sendResponseHeaders(200, 0);
        InputStream is = httpExchange.getRequestBody();
        String jsonString = new String(is.readAllBytes());
        try {
            Epic epic = gson.fromJson(jsonString, Epic.class);
            if (manager.getEpic(epic.getId()) == null) {
                manager.createEpic(epic);
            } else {
                manager.updateEpic(epic);
            }
        } catch (JsonSyntaxException e) {
            System.out.println("Некорректный JSON");
        }
        httpExchange.close();
    }

    public static void subTaskAddUpdateHandler(HttpExchange httpExchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        httpExchange.sendResponseHeaders(200, 0);
        InputStream is = httpExchange.getRequestBody();
        String jsonString = new String(is.readAllBytes());
        try {
            SubTask subTask = gson.fromJson(jsonString, SubTask.class);
            if (manager.getSubtask(subTask.getId()) == null) {
                manager.createSubtask(subTask);
            } else {
                manager.updateSubtask(subTask);
            }
        } catch (JsonSyntaxException e) {
            System.out.println("Некорректный JSON");
        }
        httpExchange.close();
    }

    static class EpicGetAllHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
                gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
                Gson gson = gsonBuilder.create();
                String parameters = httpExchange.getRequestURI().getQuery();
                int epicId = Integer.parseInt(parameters.substring(3));
                Epic epic = new Epic(epicId, null, null, null, null);
                List<SubTask> list = manager.getAllEpicById(epic);
                String result = "";
                try {
                    for (SubTask subTask : list) {
                        result += gson.toJson(subTask) + "\n";
                    }
                } catch (JsonSyntaxException exception) {
                    System.out.println("Incorrect JSON format");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(result.getBytes());
                }
            } else {
                httpExchange.sendResponseHeaders(400, 0);
            }
        }
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
                gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
                Gson gson = gsonBuilder.create();
                String result = "";
                List<Task> list = manager.showHistory();
                for (Task task : list) {
                    try {
                        result += gson.toJson(task) + "\n";
                    } catch (JsonSyntaxException e) {
                        System.out.println("Некорректный JSON");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                }
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(result.getBytes());
                }
            } else {
                httpExchange.sendResponseHeaders(400, 0);
            }
            httpExchange.close();
        }
    }

    static class TaskPrioritizedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
                gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
                Gson gson = gsonBuilder.create();
                List<Task> list = manager.getPrioritizedTasks();
                String result = "";
                try {
                    for (Task task : list) {
                        result += gson.toJson(task) + "\n";
                    }
                } catch (JsonSyntaxException exception) {
                    System.out.println("Incorrect JSON format");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(result.getBytes());
                }
            }
        }
    }
}
