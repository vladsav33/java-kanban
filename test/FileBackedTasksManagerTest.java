import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest {

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(new File("current.cfg"));
    }

    @Test
    void save() {
        List<Task> listTasks;
        FileBackedTasksManager manager = new FileBackedTasksManager(new File("current.cfg"));

        // Пустой список задач
        manager.save();
        FileBackedTasksManager.loadFromFile(new File("current.cfg"));
        listTasks = manager.showAllTasks();
        assertEquals(listTasks, List.of());

        // Эпик без подзадач
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.save();
        FileBackedTasksManager.loadFromFile(new File("current.cfg"));
        Epic epic = manager.getEpic(1);
        assertEquals(epic.toString(), "1,EPIC,NEW,Ремонт,Обновить ремонт,null,null");
    }

    @Test
    void getTask() {
        super.getTask();
    }

    @Test
    void getEpic() {
        super.getEpic();
    }

    @Test
    void getSubtask() {
        super.getSubtask();
    }

    @Test
    void removeTaskById() {
        super.removeTaskById();
    }

    @Test
    void removeEpicById() {
        super.removeEpicById();
    }

    @Test
    void removeSubtaskById() {
        super.removeSubtaskById();
    }

    @Test
    void createTask() {
        super.createTask();
    }

    @Test
    void createEpic() {
        super.createEpic();
    }

    @Test
    void createSubtask() {
        super.createSubtask();
    }

    @Test
    void updateTask() {
        super.updateTask();
    }

    @Test
    void updateEpic() {
        super.updateEpic();
    }

    @Test
    void updateSubtask() {
        super.updateSubtask();
    }
}