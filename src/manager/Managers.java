package manager;

import java.net.MalformedURLException;
import java.net.URL;

public class Managers {
    public static TaskManager getDefault() throws MalformedURLException {
        return new HttpTaskManager(new URL("http://localhost:8080"));
    }
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
