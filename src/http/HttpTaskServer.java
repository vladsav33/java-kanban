package http;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;

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
}
