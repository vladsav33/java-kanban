package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpCode;
import task.SubTask;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import static http.HttpTaskServer.*;
import static handler.TaskHandler.createGson;
import static handler.TaskHandler.taskToJson;

public class SubtaskHandler implements HttpHandler {
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
                httpExchange.sendResponseHeaders(HttpCode.BAD_REQUEST.getCode(), 0);
        }
    }

    public static void subTaskGetHandler(HttpExchange httpExchange) throws IOException {
        String parameters = httpExchange.getRequestURI().getQuery();
        int subTaskId = Integer.parseInt(parameters.substring(3));
        SubTask subTask = manager.getSubtask(subTaskId);
        String result = taskToJson(subTask, httpExchange);
        httpExchange.sendResponseHeaders(HttpCode.OK.getCode(), 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(result.getBytes());
        }
    }

    public static void subTaskDeleteHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(HttpCode.NO_CONTENT.getCode(), -1);
        manager.deleteAllSubtasks();
    }

    public static void subTaskRemoveHandler(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(HttpCode.NO_CONTENT.getCode(), -1);
        String parameters = httpExchange.getRequestURI().getQuery();
        int subTaskId = Integer.parseInt(parameters.substring(3));
        manager.removeSubtaskById(subTaskId);
    }

    public static void subTaskShowHandler(HttpExchange httpExchange) throws IOException {
        Gson gson = createGson();
        List<SubTask> list = manager.showAllSubtasks();
        StringBuilder result = new StringBuilder();
        try {
            for (SubTask subTask : list) {
                result.append(gson.toJson(subTask)).append("\n");
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

    public static void subTaskAddUpdateHandler(HttpExchange httpExchange) throws IOException {
        Gson gson = createGson();
        httpExchange.sendResponseHeaders(HttpCode.OK.getCode(), 0);
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


}
