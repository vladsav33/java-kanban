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
    private TaskManager manager;
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void BeforeEach() {
        manager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void addDuplicate() {
        //Дублирование
        List<Task> list = historyManager.getHistory();
        assertEquals(list, List.of());

        Epic epic = manager.createEpic("Ремонт", "Обновить ремонт");
        historyManager.add(epic);
        historyManager.add(epic);
        list = historyManager.getHistory();
        assertEquals(list, List.of(epic));
    }

    @Test
    void add() {
        //Добавление в очередь
        List<Task> list = historyManager.getHistory();
        assertEquals(list, List.of());

        Epic epic = manager.createEpic("Ремонт", "Обновить ремонт");
        historyManager.add(epic);
        list = historyManager.getHistory();
        assertEquals(list, List.of(epic));
    }

    @Test
    void remove() {
        //Удаление из очереди
        List<Task> list = new ArrayList<>();
        List<Task> listToCompare;

        Task task = manager.createTask("Задача", "Задача 1");
        historyManager.add(task);
        list.add(task);

        listToCompare = historyManager.getHistory();
        assertEquals(list, listToCompare);
    }

    @Test
    void removeFromMiddle() {
        //Удаление задач из середины очереди
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

        historyManager.remove(2);
        listToCompare = historyManager.getHistory();
        list.remove(1);
        assertEquals(list, listToCompare);
    }

    @Test
    void removeFromBeginning() {
        //Удаление задач из начала очереди
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

        historyManager.remove(1);
        listToCompare = historyManager.getHistory();
        list.remove(0);
        assertEquals(list, listToCompare);
    }

    @Test
    void removeFromEnd() {
        //Удаление задач из конца очереди
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

        historyManager.remove(3);
        listToCompare = historyManager.getHistory();
        list.remove(2);
        assertEquals(list, listToCompare);
    }

    @Test
    void getHistory() {
        //Check history
        List<Task> listToCompare = new ArrayList<>();

        Task task = manager.createTask("Задача", "Задача 1");
        historyManager.add(task);
        listToCompare.add(task);
        task = manager.createTask("Задача", "Задача 2");
        historyManager.add(task);
        listToCompare.add(task);
        task = manager.createTask("Задача", "Задача 3");
        historyManager.add(task);
        listToCompare.add(task);

        List<Task> list = historyManager.getHistory();
        assertEquals(list, listToCompare);
    }

    @Test
    void checkEmptyHistory() {
        //Check history
        List<Task> listToCompare = new ArrayList<>();

        List<Task> list = historyManager.getHistory();
        assertEquals(list, listToCompare);
    }
}