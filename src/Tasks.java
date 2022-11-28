import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Tasks {
    public HashMap<Integer, Task> tasks;
    public Tasks() {
        tasks = new HashMap<>();
    }

    public class Task {
        String name;
        String description;
        Status status;

        public Task(String name, String description) {
            this(name, description, Status.NEW);
        }

        public Task(String name, String description, Status status) {
            this.name = name;
            this.description = description;
            this.status = status;
        }

        @Override
        public String toString() {
            return name + " - " + description + " - " + status;
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    public Task createTask(String name, String description) {
        Task task = new Task(name, description);
        tasks.put(Manager.nextId(), task);
        System.out.println("Создана новая задача: \"" + name + "\" - \"" + description + "\"");
        return task;
    }

    public void removeTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            System.out.println("Задача с идентификатором " + id + " удалена");
        }
    }

    public Task showTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Задача: " + task.name + " - " + task.description + " - " + task.status);
        return task;
    }

    public HashMap<Integer, Task> showAllTasks() {
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            System.out.println("Идентификатор: " + entry.getKey() + " " + entry.getValue());
        }
        return tasks;
    }

    public Task updateTask(int id) {
        Scanner scanner = new Scanner(System.in);

        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Задача: " + task.name + " - " + task.description + " - " + task.status);
        System.out.println("Введите название задачи:");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        System.out.println("Введите статус задачи:");
        System.out.println("1. NEW");
        System.out.println("2. IN PROGRESS");
        System.out.println("3. DONE");
        int choice = scanner.nextInt();

        Status status;
        switch (choice) {
            case 1:
                status = Status.NEW;
                break;
            case 2:
                status = Status.IN_PROGRESS;
                break;
            case 3:
                status = Status.DONE;
                break;
            default:
                System.out.println("Некорректный статус");
                return null;
        }
        task = new Task(name, description, status);
        tasks.put(id, task);
        return task;
    }
}
