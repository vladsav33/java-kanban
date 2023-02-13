import http.HttpTaskServer;
import http.KVServer;
import manager.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        TaskManager manager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.httpServerStart();
    }
}
