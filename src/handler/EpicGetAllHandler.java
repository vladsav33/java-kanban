package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpCode;
import task.Epic;
import task.SubTask;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import static http.HttpTaskServer.manager;
import static handler.TaskHandler.createGson;

public class EpicGetAllHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            Gson gson = createGson();
            String parameters = httpExchange.getRequestURI().getQuery();
            int epicId = Integer.parseInt(parameters.substring(3));
            Epic epic = new Epic(epicId, null, null, null, null);
            List<SubTask> list = manager.getAllEpicById(epic);
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
        } else {
            httpExchange.sendResponseHeaders(HttpCode.BAD_REQUEST.getCode(), 0);
        }
    }
}
