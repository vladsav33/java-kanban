package manager;

import java.util.List;
import task.*;

public interface TaskManager {

    List<Task> getPrioritizedTasks();

    Task getTask(int id);
    Epic getEpic(int id);
    SubTask getSubtask(int id);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(int id);

    List<Task> showAllTasks();
    List<Epic> showAllEpics();
    List<SubTask> showAllSubtasks();

    Task createTask(String name, String description);
    Task createTask(Task task);

    Epic createEpic(String name, String description);
    Epic createEpic(Epic epic);
    SubTask createSubtask(String name, String description, int idEpic);
    SubTask createSubtask(SubTask subtask);

    Task updateTask(Task task);
    Task updateEpic(Epic epic);
    Task updateSubtask(SubTask subTask);

    List<SubTask> getAllEpicById(Epic epic);

    List<Task> showHistory();
}
