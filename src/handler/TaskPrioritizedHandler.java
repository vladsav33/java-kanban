package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import static http.HttpTaskServer.*;
import static handler.TaskHandler.createGson;

public class TaskPrioritizedHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            Gson gson = createGson();
            List<Task> list = manager.getPrioritizedTasks();
            StringBuilder result = new StringBuilder();
            try {
                for (Task task : list) {
                    result.append(gson.toJson(task) + "\n");
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
    }
}