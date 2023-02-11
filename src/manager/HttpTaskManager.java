package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import task.Epic;
import task.SubTask;
import task.Task;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;


public class HttpTaskManager extends FileBackedTasksManager {
    public final KVTaskClient client;
    public HttpTaskManager(URL url) {
        super(new File("current.cfg"));
        client = new KVTaskClient("http://localhost:8078");
    }

    @Override
    public void save() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        for (Task task : tasks.values()) {
            client.put(String.valueOf(task.getId()), gson.toJson(task));
        }
        for (Epic epic : epics.values()) {
            client.put(String.valueOf(epic.getId()), gson.toJson(epic));
        }
        for (SubTask subTask : subTasks.values()) {
            client.put(String.valueOf(subTask.getId()), gson.toJson(subTask));
        }
        String history = historyToString(historyManager);
        if (!history.isEmpty()) {
            client.put("0", historyToString(historyManager));
        }
    }
}


