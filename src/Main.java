import manager.Managers;
import manager.TaskManager;
import task.Task;
import task.SubTask;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        manager.createTask("Уборка", "Подмести");
        manager.createTask("Уборка", "Помыть пол");
        manager.createEpic("Ремонт", "Обновить ремонт");
        manager.createSubtask("Покупки", "Купить краску", 3);
        manager.createSubtask("Покупки", "Купить кисточку", 3);
        manager.createEpic("Купить машину", "Новый кроссовер");
        manager.createSubtask("Объявления", "Посмотреть объявления", 6);


        System.out.println("-------------------------------------------------------------");

        manager.getAllEpicById(manager.getEpic(3));

        Task task = manager.getTask(1);
        task = manager.getTask(2);

        SubTask subTask = manager.getSubtask(4);
        subTask = manager.getSubtask(5);
        subTask = manager.getSubtask(7);

        System.out.println("-------------------------------------------------------------");

        manager.showHistory();
    }
}
