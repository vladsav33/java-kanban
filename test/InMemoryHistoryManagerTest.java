import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager manager;
    InMemoryHistoryManager historyManager;

    @BeforeEach
    public void BeforeEach() {
        manager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add() {
        List<Task> list;
        list = historyManager.getHistory();
        assertEquals(list, List.of());

        Epic epic = manager.createEpic("Ремонт", "Обновить ремонт");
        historyManager.add(epic);
        historyManager.add(epic);
        list = historyManager.getHistory();
        assertEquals(list, List.of(epic));
    }

    @Test
    void remove() {
        List<Task> list = new ArrayList<>();
        List<Task> listToCompare;

        Task task = manager.createTask("Задача", "Задача 1");
        historyManager.add(task);
        list.add(task);

        task = manager.createTask("Задача", "Задача 2");
        historyManager.add(task);
        list.add(task);

        task = manager.createTask("Задача", "Задача 3");
        historyManager.add(task);
        list.add(task);

        listToCompare = historyManager.getHistory();
        assertEquals(list, listToCompare);

        //Удаление задач из очереди: из середины, начала, конца
        historyManager.remove(2);
        listToCompare = historyManager.getHistory();
        list.remove(1);
        assertEquals(list, listToCompare);

        historyManager.remove(1);
        listToCompare = historyManager.getHistory();
        list.remove(0);
        assertEquals(list, listToCompare);

        historyManager.remove(3);
        listToCompare = historyManager.getHistory();
        list.remove(0);
        assertEquals(list, listToCompare);
    }

    @Test
    void getHistory() {
        List<Task> list = new ArrayList<>();
        List<Task> listToCompare;

        Task task = manager.createTask("Задача", "Задача 1");
        historyManager.add(task);
        list.add(task);

        listToCompare = historyManager.getHistory();
        assertEquals(list, listToCompare);
    }
}