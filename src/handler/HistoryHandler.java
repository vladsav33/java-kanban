package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static handler.TaskHandler.createGson;
import static http.HttpTaskServer.*;

public class HistoryHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            Gson gson = createGson();
            StringBuilder result = new StringBuilder();
            List<Task> list = manager.showHistory();
            for (Task task : list) {
                try {
                    result.append(gson.toJson(task) + "\n");
                } catch (JsonSyntaxException e) {
                    System.out.println("Некорректный JSON");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
            }
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(result.toString().getBytes());
            }
        } else {
            httpExchange.sendResponseHeaders(400, 0);
        }
        httpExchange.close();
    }
}
