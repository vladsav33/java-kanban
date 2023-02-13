package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;
import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackupTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager(new File("current.cfg"));
    }

    @Test
    void saveEmptyList() {
        // Empty list of tasks
        manager.save();
        FileBackedTasksManager.loadFromFile(new File("current.cfg"));
        List<Task> listTasks = manager.showAllTasks();
        assertEquals(listTasks, Collections.emptyList());
    }

    @Test
    void saveEmptyEpic() {
        // Epic without subtasks
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.save();
        FileBackedTasksManager.loadFromFile(new File("current.cfg"));
        Epic epic = manager.getEpic(1);
        assertEquals(epic.toString(), "1,EPIC,NEW,Ремонт,Обновить ремонт,null,null");
    }

    @Test
    void saveLoadEmptyHistory() {
        // Empty history - save and load
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.save();
        FileBackedTasksManager.loadFromFile(new File("current.cfg"));
        Epic epic = manager.getEpic(1);
        assertEquals(epic.toString(), "1,EPIC,NEW,Ремонт,Обновить ремонт,null,null");

        HistoryManager historyManager = new InMemoryHistoryManager();
        List<Task> list = historyManager.getHistory();
        assertEquals(list, Collections.emptyList());
    }
}