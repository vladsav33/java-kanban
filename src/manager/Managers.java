package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import http.HttpTaskManager;

import java.net.MalformedURLException;
import java.net.URL;

public class Managers {
    public static TaskManager getDefault() throws MalformedURLException {
        return new HttpTaskManager(new URL("http://localhost:8078"));
    }
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
