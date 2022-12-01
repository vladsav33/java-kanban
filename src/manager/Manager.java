package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import task.*;

public class Manager {
    static int id;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, SubTask> subTasks;
    Scanner scanner;

    public Manager() {
        id = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public static int nextId() {
        return ++id;
    }

    public Task showObjectById() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();

        Task task = showTaskById(id);
        if (task == null) {
            task = showEpicById(id);
        }
        if (task == null) {
            task = showSubtaskById(id);
        }
        return task;
    }

    public Task showTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Задача: " + task);
        return task;
    }

    public Epic showEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Эпик: " + epic);
        if (epic.subTasks != null) {
            for (Integer i : epic.subTasks) {
                showSubtaskById(i);
            }
        }
        return epic;
    }

    public SubTask showSubtaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Подзадача: " + subTask);
        return subTask;
    }


    public void deleteAllObjects() {
        deleteAllTasks();
        deleteAllEpics();
        deleteAllSubtasks();
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subTasks.clear();
    }


    public void removeObjectById() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();

        removeTaskById(id);
        removeEpicById(id);
        removeSubtaskById(id);
    }

    public void removeTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            System.out.println("Задача с идентификатором " + id + " удалена");
        }
    }

    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (int i : epic.subTasks) {
                removeSubtaskById(i);
            }
            System.out.println("Задача с идентификатором " + id + " удалена");
            epics.remove(id);
        }
    }

    public void removeSubtaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            System.out.println("Задача с идентификатором " + id + " удалена");
            epics.get(subTask.epicId).removeSubtask(id);
            updateSubtask(subTask);
        }
    }

    public void showAllObjects() {
        showAllTasks();
        showAllEpics();
    }

    public ArrayList<Task> showAllTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            result.add(entry.getValue());
            System.out.println("Идентификатор: " + entry.getKey() + " Задача: " + entry.getValue());
        }
        return result;
    }

    public ArrayList<Task> showAllEpics() {
        ArrayList<Task> result = new ArrayList<>();
        Task task;
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            result.add(entry.getValue());
            System.out.println("Идентификатор: " + entry.getKey() + " Эпик: " + entry.getValue());
            if (entry.getValue().subTasks != null) {
                for (Integer i : entry.getValue().subTasks) {
                    task = showSubtaskById(i);
                    result.add(task);
                }
            }
        }
        return result;
    }

    public ArrayList<Task> showAllSubtasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
            result.add(entry.getValue());
            System.out.println("Идентификатор: " + entry.getKey() + " Подзадача: " + entry.getValue());
        }
        return result;
    }


    public void createObject() {
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
                createTask(name, description);
                break;
            case 2:
                createEpic(name, description);
                break;
            case 3:
                System.out.println("Введите номер эпика для этой подзадачи:");
                int idEpic = scanner.nextInt();
                if (epics.get(idEpic) == null) {
                    System.out.println("Такого эпика нет");
                    return;
                }
                createSubtask(name, description, idEpic);
                break;
            default:
                System.out.println("Неправильный тип задачи");
        }
    }

    public Task createTask(String name, String description) {
        int id = nextId();
        Task task = new Task(id, name, description);
        tasks.put(id, task);
        return task;
    }

    public Epic createEpic(String name, String description) {
        int id = nextId();
        Epic epic = new Epic(id, name, description);
        epics.put(id, epic);
        return epic;
    }

    public SubTask createSubtask(String name, String description, int idEpic) {
        int id = nextId();
        SubTask subTask = new SubTask(id, name, description, idEpic);
        subTasks.put(id, subTask);
        epics.get(idEpic).addSubtask(id);
        updateStatus(idEpic);
        return subTask;
    }


    public void updateObject() {
        System.out.println("Введите идентификатор задачи");
        int id = Integer.parseInt(scanner.nextLine());
        Task task = tasks.get(id);
        Epic epic = null;
        SubTask subTask = null;
        Status status = Status.NEW;
        int choice = 1;

        if (task == null) {
            epic = epics.get(id);
            choice = 2;
        }
        if (epic == null) {
            subTask = subTasks.get(id);
            choice = 3;
        }
        if (subTask == null) {
            System.out.println("Задача с таким идентификатором не найдена");
            return;
        }

        switch (choice) {
            case 1:
                System.out.println("Идентификатор: " + id + " Задача: " + task);
                break;
            case 2:
                System.out.println("Идентификатор: " + id + " Эпик: " + epic);
                break;
            case 3:
                System.out.println("Идентификатор: " + id + " Подзадача: " + subTask);
        }

        System.out.println("Введите название задачи:");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();

        if (choice == 1 || choice == 3) {
            System.out.println("Введите статус задачи:");
            System.out.println("1. NEW");
            System.out.println("2. IN PROGRESS");
            System.out.println("3. DONE");
            int newStatus = Integer.parseInt(scanner.nextLine());

            switch (newStatus) {
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
                    return;
            }
        }

        switch (choice) {
            case 1:
                task = new Task(id, name, description, status);
                updateTask(task);
                break;
            case 2:
                epic = new Epic(id, name, description);
                updateEpic(epic);
                break;
            case 3:
                subTask = subTasks.get(id);
                int epicId = subTask.epicId;
                subTask = new SubTask(id, name, description, status, epicId);
                updateSubtask(subTask);
        }
    }
    public Task updateTask(Task task) {
        tasks.put(task.id, task);
        return task;
    }

    public Task updateEpic(Epic epic) {
        epics.put(epic.id, epic);
        return epic;
    }

    public Task updateSubtask(SubTask subTask) {
        subTasks.put(subTask.id, subTask);
        updateStatus(subTask.epicId);
        return subTask;
    }


    public Epic showEpicById() {
        System.out.println("Введите идентификатор эпика:");
        int id = scanner.nextInt();
        Epic epic = showEpicById(id);
        return epic;
    }

    public void makeTest() {
        createTask("Уборка", "Подмести");
        createTask("Уборка", "Помыть пол");
        createEpic("Ремонт", "Обновить ремонт");
        createSubtask("Покупки", "Купить краску", 3);
        createSubtask("Покупки", "Купить кисточку", 3);
        createEpic("Купить машину", "Новый кроссовер");
        createSubtask("Объявления", "Посмотреть объявления", 6);
    }

    public void updateStatus(int epicId) {
        ArrayList<Integer> listSubTasks = epics.get(epicId).subTasks;
        if (listSubTasks == null || listSubTasks.isEmpty()) {
            return;
        }
        Status status = subTasks.get(listSubTasks.get(0)).getStatus();
        for (int idSubTask : listSubTasks) {
            if (status != subTasks.get(idSubTask).getStatus()) {
                status = Status.IN_PROGRESS;
                break;
            }
        }
        epics.get(epicId).status = status;
    }
}
