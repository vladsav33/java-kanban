import manager.FileBackedTasksManager;
import task.*;

import java.io.File;
import java.time.LocalTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final String configFile = "current.cfg";
        FileBackedTasksManager manager = new FileBackedTasksManager(new File(configFile));

        manager.createTask("Уборка", "Подмести");
        Task task = new Task(1, Type.TASK, Status.NEW, "Уборка", "Подмести");
        task.setStartTime(LocalTime.of(19,10));
        manager.updateTask(task);

        manager.createTask("Уборка", "Помыть пол");
        task = new Task(2, Type.TASK, Status.NEW, "Уборка", "Помыть пол");
        task.setStartTime(LocalTime.of(19,30));
        manager.updateTask(task);

        manager.createTask("Уборка", "Помыть стены");
        task = new Task(3, Type.TASK, Status.NEW, "Уборка", "Помыть стены");
        task.setStartTime(LocalTime.of(19,20));
        manager.updateTask(task);

        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 4);
        manager.createSubtask("Покупки", "Купить кисточку", 4);
        manager.createSubtask("Объявления", "Посмотреть объявления", 4);
        manager.createEpic("Купить машину", "Новый кроссовер");

        SubTask subTsk = new SubTask(5, Type.SUBTASK, Status.DONE, "Покупки", "Купить краску", 4);
        manager.updateSubtask(subTsk);
        subTsk = new SubTask(6, Type.SUBTASK, Status.DONE, "Покупки", "Купить кисточку", 4);
        manager.updateSubtask(subTsk);
        subTsk = new SubTask(7, Type.SUBTASK, Status.DONE, "Объявления", "Посмотреть объявления", 4);
        manager.updateSubtask(subTsk);

        List<Task> list = manager.getPrioritizedTasks();
        for (Task taskInList : list) {
            System.out.println(taskInList.toString());
        }

        System.out.println("-------------------------------------------------------------");

        manager.getAllEpicById(manager.getEpic(4));
        manager.showHistory();

        manager.getTask(1);
        manager.showHistory();
        manager.getTask(2);
        manager.getTask(1);
        manager.showHistory();

        manager.getSubtask(5);
        manager.showHistory();
        manager.getSubtask(6);
        manager.showHistory();
        manager.getSubtask(7);
        manager.showHistory();

        manager.getEpic(4);
        manager.showHistory();
        manager.getEpic(8);
        manager.showHistory();

        System.out.println("----------Loaded from config file-----------------------------");
        manager = FileBackedTasksManager.loadFromFile(new File(configFile));
        manager.showAllTasks();
        manager.showAllEpics();
        manager.showAllSubtasks();
        manager.showHistory();
        System.out.println("-------------------------------------------------------------");
    }
}
