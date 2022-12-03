import manager.Manager;
import task.Status;
import task.Task;
import task.SubTask;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.createTask("Уборка", "Подмести");
        manager.createTask("Уборка", "Помыть пол");
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 3);
        manager.createSubtask("Покупки", "Купить кисточку", 3);
        manager.createEpic("Купить машину", "Новый кроссовер");
        manager.createSubtask("Объявления", "Посмотреть объявления", 6);

        manager.showAllTasks();
        manager.showAllEpics();
        manager.showAllSubtasks();

        System.out.println("-------------------------------------------------------------");

        manager.getAllEpicById(manager.showEpicById(3));

        System.out.println("-------------------------------------------------------------");

        Task task = manager.showTaskById(1);
        task.setStatus(Status.DONE);
        manager.updateTask(task);

        task = manager.showTaskById(2);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);

        SubTask subTask = manager.showSubtaskById(4);
        subTask.setStatus(Status.NEW);
        manager.updateSubtask(subTask);

        subTask = manager.showSubtaskById(5);
        subTask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subTask);

        subTask = manager.showSubtaskById(7);
        subTask.setStatus(Status.DONE);
        manager.updateSubtask(subTask);

        manager.showAllTasks();
        manager.showAllEpics();
        manager.showAllSubtasks();

        System.out.println("-------------------------------------------------------------");

        manager.removeEpicById(6);
        manager.removeTaskById(2);

        manager.showAllTasks();
        manager.showAllEpics();
        manager.showAllSubtasks();
    }
}
