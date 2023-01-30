import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
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
    void deleteAllTasks() {
        super.deleteAllTasks();
    }

    @Test
    void deleteAllEpics() {
        super.deleteAllEpics();
    }

    @Test
    void deleteAllSubtasks() {
        super.deleteAllSubtasks();
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
    void showAllTasks() {
        super.showAllTasks();
    }

    @Test
    void showAllEpics() {
        super.showAllEpics();
    }

    @Test
    void showAllSubtasks() {
        super.showAllSubtasks();
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

    @Test
    void getAllEpicById() {
        super.getAllEpicById();
    }

    @Test
    void updateStatus() {
    }

    @Test
    void showHistory() {
    }
}