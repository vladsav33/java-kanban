import static org.junit.jupiter.api.Assertions.*;

import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;

    @Test
    void getTask() {
        Task task  = manager.getTask(1);
        Assertions.assertNull(task);

        manager.createTask("Уборка", "Подмести");
        task  = manager.getTask(1);
        Assertions.assertTrue(task.toString().equals("1,TASK,NEW,Уборка,Подмести,null,null"));

        task = manager.getTask(2);
        Assertions.assertNull(task);
    }

    @Test
    void getEpic() {
        Epic epic  = manager.getEpic(1);
        Assertions.assertNull(epic);

        manager.createEpic("Ремонт", "Обновить ремонт");
        epic  = manager.getEpic(1);
        Assertions.assertTrue(epic.toString().equals("1,EPIC,NEW,Ремонт,Обновить ремонт,null,null"));

        epic = manager.getEpic(2);
        Assertions.assertNull(epic);
    }

    @Test
    void getSubtask() {
        SubTask subTask  = manager.getSubtask(1);
        Assertions.assertNull(subTask);

        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 1);
        subTask  = manager.getSubtask(2);
        Assertions.assertTrue(subTask.toString().equals("2,SUBTASK,NEW,Покупки,Купить краску,1,null,null"));

        subTask = manager.getSubtask(3);
        Assertions.assertNull(subTask);
    }

    @Test
    void deleteAllTasks() {
        manager.deleteAllTasks();
        Task task  = manager.getTask(1);
        Assertions.assertNull(task);

        manager.createTask("Уборка", "Подмести");
        manager.deleteAllTasks();

        task = manager.getTask(1);
        Assertions.assertNull(task);
    }

    @Test
    void deleteAllEpics() {
        manager.deleteAllEpics();
        Epic epic  = manager.getEpic(1);
        Assertions.assertNull(epic);

        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.deleteAllEpics();

        epic  = manager.getEpic(1);
        Assertions.assertNull(epic);
    }

    @Test
    void deleteAllSubtasks() {
        manager.deleteAllSubtasks();
        SubTask subTask  = manager.getSubtask(1);
        Assertions.assertNull(subTask);

        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 1);
        manager.deleteAllSubtasks();

        subTask  = manager.getSubtask(1);
        Assertions.assertNull(subTask);
    }

    @Test
    void removeTaskById() {
        manager.removeTaskById(1);
        Task task  = manager.getTask(1);
        Assertions.assertNull(task);

        manager.createTask("Уборка", "Подмести");
        manager.removeTaskById(1);
        task  = manager.getTask(1);
        Assertions.assertNull(task);
    }

    @Test
    void removeEpicById() {
        manager.removeEpicById(1);
        Epic epic  = manager.getEpic(1);
        Assertions.assertNull(epic);

        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.removeEpicById(1);
        epic  = manager.getEpic(1);
        Assertions.assertNull(epic);
    }

    @Test
    void removeSubtaskById() {
        manager.removeSubtaskById(1);
        SubTask subTask  = manager.getSubtask(1);
        Assertions.assertNull(subTask);

        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 1);
        manager.removeSubtaskById(2);
        subTask  = manager.getSubtask(2);
        Assertions.assertNull(subTask);
    }

    @Test
    void showAllTasks() {
        List<Task> list;
        list = manager.showAllTasks();
        Assertions.assertEquals(list, List.of());

        Task task = manager.createTask("Уборка", "Подмести");
        List<Task> listToCompare = new ArrayList<>();
        list = manager.showAllTasks();
        listToCompare.add(task);
        Assertions.assertEquals(list, listToCompare);
    }

    @Test
    void showAllEpics() {
        List<Epic> list;
        list = manager.showAllEpics();
        Assertions.assertEquals(list, List.of());

        Epic epic = manager.createEpic("Ремонт", "Обновить ремонт");
        List<Epic> listToCompare = new ArrayList<>();
        list = manager.showAllEpics();
        listToCompare.add(epic);
        Assertions.assertEquals(list, listToCompare);
    }

    @Test
    void showAllSubtasks() {
        List<SubTask> list;
        list = manager.showAllSubtasks();
        Assertions.assertEquals(list, List.of());

        manager.createEpic("Ремонт", "Обновить ремонт");
        SubTask subTask = manager.createSubtask("Покупки", "Купить краску", 1);
        List<Task> listToCompare = new ArrayList<>();
        list = manager.showAllSubtasks();
        listToCompare.add(subTask);
        Assertions.assertEquals(list, listToCompare);
    }

    @Test
    void createTask() {
        Task task = manager.createTask("Уборка", "Подмести");
        assertEquals(task, manager.getTask(1));
    }

    @Test
    void createEpic() {
        Epic epic = manager.createEpic("Ремонт", "Обновить ремонт");
        assertEquals(epic, manager.getEpic(1));
    }

    @Test
    void createSubtask() {
        manager.createEpic("Ремонт", "Обновить ремонт");
        SubTask subTask = manager.createSubtask("Покупки", "Купить краску", 1);
        assertEquals(subTask, manager.getSubtask(2));
    }

    @Test
    void updateTask() {
        manager.createTask("Уборка", "Подмести");
        Task task = new Task(1, Type.TASK, Status.DONE, "Уборка", "Подмести",
                LocalTime.of(17, 20), Duration.ofMinutes(20));
        manager.updateTask(task);
        String result = manager.getTask(1).toString();
        assertTrue (result.equals("1,TASK,DONE,Уборка,Подмести,17:20,PT20M"));
    }

    @Test
    void updateEpic() {
        manager.createEpic("Ремонт", "Обновить ремонт");
        Epic epic = new Epic(1, Type.EPIC, Status.DONE, "Ремонт", "Обновить ремонт");
        manager.updateEpic(epic);
        String result = manager.getEpic(1).toString();
        assertTrue (result.equals("1,EPIC,DONE,Ремонт,Обновить ремонт,null,null"));
    }

    @Test
    void updateSubtask() {
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 1);
        SubTask subTask = new SubTask(2, Type.SUBTASK, Status.DONE, "Покупки", "Купить краску", 1);
        manager.updateSubtask(subTask);
        String result = manager.getSubtask(2).toString();
        assertTrue (result.equals("2,SUBTASK,DONE,Покупки,Купить краску,1,null,null"));
    }

    @Test
    void getAllEpicById() {
        Epic epic = manager.createEpic("Ремонт", "Обновить ремонт");
        SubTask subTask = manager.createSubtask("Покупки", "Купить краску", 1);

        List<SubTask> listToCompare = new ArrayList<>();
        List<SubTask> list = manager.getAllEpicById(epic);
        System.out.println(subTask.toString());
        listToCompare.add(subTask);
        Assertions.assertEquals(list, listToCompare);
    }

    @Test
    void showHistory() {
    }
}