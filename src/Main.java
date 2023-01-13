import manager.FileBackedTasksManager;
import manager.TaskManager;
import task.Task;
import task.SubTask;
import task.Epic;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        final String configFile = "current.cfg";
        TaskManager manager = new FileBackedTasksManager(new File(configFile));

        manager.createTask("Уборка", "Подмести");
        manager.createTask("Уборка", "Помыть пол");
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 3);
        manager.createSubtask("Покупки", "Купить кисточку", 3);
        manager.createSubtask("Объявления", "Посмотреть объявления", 3);
        manager.createEpic("Купить машину", "Новый кроссовер");

        System.out.println("-------------------------------------------------------------");

        manager.getAllEpicById(manager.getEpic(3));
        manager.showHistory();

        Task task = manager.getTask(1);
        manager.showHistory();
        task = manager.getTask(2);
        task = manager.getTask(1);
        manager.showHistory();

        SubTask subTask = manager.getSubtask(4);
        manager.showHistory();
        subTask = manager.getSubtask(5);
        manager.showHistory();
        subTask = manager.getSubtask(6);
        manager.showHistory();

        Epic epic = manager.getEpic(3);
        manager.showHistory();
        epic = manager.getEpic(7);
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
