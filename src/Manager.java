import java.util.Scanner;

public class Manager {
    static int id;
    Tasks tasks;
    Epics epics;
    SubTasks subTasks;
    Scanner scanner;

    Manager() {
        id = 0;
        tasks = new Tasks();
        epics = new Epics();
        subTasks = new SubTasks();
        scanner = new Scanner(System.in);
    }

    public static int nextId() {
        return ++id;
    }

    public static int lastId() {
        return id;
    }

    public void showTaskById() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();
        tasks.showTaskById(id);
        epics.showTaskById(id, subTasks);
        subTasks.showTaskById(id);
    }

    public void deleteAllTasks() {
        tasks.deleteAllTasks();
        epics.deleteAllTasks();
        subTasks.deleteAllTasks();
    }

    public void removeTaskById() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();
        tasks.removeTaskById(id);
        epics.removeTaskById(id, subTasks);
        subTasks.removeTaskById(id, epics);
    }

    public void showAllTasks() {
        tasks.showAllTasks();
        epics.showAllTasks(subTasks);
    }

    public void showEpicById() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();
        epics.showTaskById(id, subTasks);
    }

    public void createTask() {
        System.out.println("Введите тип задачи:");
        System.out.println("1. Задача");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадача");
        int choice = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите название задачи:");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();

        switch(choice) {
            case 1:
                tasks.createTask(name, description);
                break;
            case 2:
                epics.createTask(name, description);
                break;
            case 3:
                subTasks.createTask(name, description, epics);
                break;
            default:
                System.out.println("Неправильный тип задачи");
        }
    }

    public void updateTask() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();
        Tasks.Task task = tasks.updateTask(id);
        if (task != null) {
            return;
        }
        task = epics.updateTask(id);
        if (task != null) {
            return;
        }
        subTasks.updateTask(id, epics);
    }

    public void makeTest() {
        tasks.createTask("Уборка", "Подмести");
        tasks.createTask("Уборка", "Помыть пол");
        epics.createTask("Ремонт", "Обновить ремонт");
        subTasks.createTask("Покупки", "Купить краску", epics, 3);
        subTasks.createTask("Покупки", "Купить кисточку", epics, 3);
        epics.createTask("Купить машину", "Новый кроссовер");
        subTasks.createTask("Объявления", "Посмотреть объявления", epics, 6);
    }
}
