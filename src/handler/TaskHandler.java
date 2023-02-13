package handler;

import adapter.DurationAdapter;
import adapter.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpCode;
import task.Task;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static http.HttpTaskServer.*;

public class TaskHandler implements HttpHandler {
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
                httpExchange.sendResponseHeaders(HttpCode.BAD_REQUEST.getCode(), 0);
        }
    }

    public static void taskGetHandler(HttpExchange httpExchange) throws IOException {
        String parameters = httpExchange.getRequestURI().getQuery();
        int taskId = Integer.parseInt(parameters.substring(3));
        Task task = manager.getTask(taskId);
        String result = taskToJson(task, httpExchange);
        httpExchange.sendResponseHeaders(HttpCode.OK.getCode(), 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        }
    }

    public static void taskDeleteHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(HttpCode.NO_CONTENT.getCode(), -1);
        manager.deleteAllTasks();
    }

    public static void taskRemoveHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(HttpCode.NO_CONTENT.getCode(), -1);
        String parameters = httpExchange.getRequestURI().getQuery();
        int taskId = Integer.parseInt(parameters.substring(3));
        manager.removeTaskById(taskId);
    }

    public static void taskShowHandler(HttpExchange httpExchange) throws IOException {
        Gson gson = createGson();
        List<Task> list = manager.showAllTasks();
        StringBuilder result = new StringBuilder();
        try {
            for (Task task : list) {
                result.append(gson.toJson(task)).append("\n");
            }
        } catch (JsonSyntaxException exception) {
            System.out.println("Incorrect JSON format");
            httpExchange.sendResponseHeaders(HttpCode.BAD_REQUEST.getCode(), 0);
            return;
        }
        httpExchange.sendResponseHeaders(HttpCode.OK.getCode(), 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.toString().getBytes());
        }
    }

    public static void taskAddUpdateHandler(HttpExchange httpExchange) throws IOException {
        Gson gson = createGson();
        httpExchange.sendResponseHeaders(HttpCode.OK.getCode(), 0);
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

    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public static String taskToJson(Task task, HttpExchange httpExchange) throws IOException {
        Gson gson = createGson();
        try {
            return gson.toJson(task);
        } catch (JsonSyntaxException exception) {
            System.out.println("Incorrect JSON format");
            httpExchange.sendResponseHeaders(HttpCode.BAD_REQUEST.getCode(), 0);
            return null;
        }
    }
}