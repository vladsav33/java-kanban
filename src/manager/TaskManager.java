package manager;

import java.util.ArrayList;
import task.*;

public interface TaskManager {
    Task getTask(int id);
    Epic getEpic(int id);
    SubTask getSubtask(int id);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(int id);

    ArrayList<Task> showAllTasks();
    ArrayList<Epic> showAllEpics();
    ArrayList<SubTask> showAllSubtasks();

    Task createTask(String name, String description);
    Epic createEpic(String name, String description);
    SubTask createSubtask(String name, String description, int idEpic);

    Task updateTask(Task task);
    Task updateEpic(Epic epic);
    Task updateSubtask(SubTask subTask);

    ArrayList<SubTask> getAllEpicById(Epic epic);

    void updateStatus(int epicId);

    void showHistory();
}
