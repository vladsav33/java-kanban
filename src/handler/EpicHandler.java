package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Epic;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import static http.HttpTaskServer.*;
import static handler.TaskHandler.createGson;
import static handler.TaskHandler.taskToJson;

public class EpicHandler implements HttpHandler {
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

    public static void epicGetHandler(HttpExchange httpExchange) throws IOException {
        String parameters = httpExchange.getRequestURI().getQuery();
        int epicId = Integer.parseInt(parameters.substring(3));
        Epic epic = manager.getEpic(epicId);
        String result = taskToJson(epic, httpExchange);
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        }
    }

    public static void epicDeleteHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(204, -1);
        manager.deleteAllEpics();
    }

    public static void epicRemoveHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(204, -1);
        String parameters = httpExchange.getRequestURI().getQuery();
        int epicId = Integer.parseInt(parameters.substring(3));
        manager.removeEpicById(epicId);
    }

    public static void epicShowHandler(HttpExchange httpExchange) throws IOException {
        Gson gson = createGson();
        List<Epic> list = manager.showAllEpics();
        StringBuilder result = new StringBuilder();
        try {
            for (Epic epic : list) {
                result.append(gson.toJson(epic) + "\n");
            }
        } catch (JsonSyntaxException exception) {
            System.out.println("Incorrect JSON format");
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.toString().getBytes());
        }
    }

    public static void epicAddUpdateHandler(HttpExchange httpExchange) throws IOException {
        Gson gson = createGson();
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
}

