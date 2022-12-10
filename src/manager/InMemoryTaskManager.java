package manager;

import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    static int id;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, SubTask> subTasks;
    InMemoryHistoryManager historyManager;

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        historyManager = new InMemoryHistoryManager();
    }

    public static int nextId() {
        return ++id;
    }

    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Задача: " + task);
        historyManager.add(task);
        return task;
    }

    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Эпик: " + epic);
        historyManager.add(epic);
        return epic;
    }

    public SubTask getSubtask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Подзадача: " + subTask);
        historyManager.add(subTask);
        return subTask;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteAllSubtasks() {
        for (SubTask subTask : subTasks.values()) {
            epics.get(subTask.getEpicId()).removeSubTasks();
        }
        subTasks.clear();
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
            for (int subTaskId : epic.getSubTasks()) {
                subTasks.remove(subTaskId);
                removeSubtaskById(subTaskId);
            }
            System.out.println("Задача с идентификатором " + id + " удалена");
            epics.remove(id);
        }
    }

    public void removeSubtaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            System.out.println("Задача с идентификатором " + id + " удалена");
            epics.get(subTask.getEpicId()).removeSubtask(id);
            updateSubtask(subTask);
        }
    }

    public ArrayList<Task> showAllTasks() {
        ArrayList<Task> result = new ArrayList<>(tasks.values());
        for (Task task : result) {
            System.out.println("Идентификатор: " + task.getId() + " Задача: " + task);
        }
        return result;
    }

    public ArrayList<Epic> showAllEpics() {
        ArrayList<Epic> result = new ArrayList<>(epics.values());
        Task task;
        for (Epic epic : result) {
            System.out.println("Идентификатор: " + epic.getId() + " Эпик: " + epic);
        }
        return result;
    }

    public ArrayList<SubTask> showAllSubtasks() {
        ArrayList<SubTask> result = new ArrayList<>(subTasks.values());
        for (SubTask subTask : result) {
            System.out.println("Идентификатор: " + subTask.getId() + " Подзадача: " + subTask);
        }
        return result;
    }

    public Task createTask(String name, String description) {
        Task task = new Task(name, description);
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(String name, String description) {
        Epic epic = new Epic(name, description);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask createSubtask(String name, String description, int idEpic) {
        SubTask subTask = new SubTask(name, description, idEpic);
        subTasks.put(subTask.getId(), subTask);
        epics.get(idEpic).addSubtask(subTask.getId());
        updateStatus(idEpic);
        return subTask;
    }

    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public Task updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Task updateSubtask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateStatus(subTask.getEpicId());
        return subTask;
    }


    public ArrayList<SubTask> getAllEpicById(Epic epic) {
        ArrayList<SubTask> listSubTasks = new ArrayList<>();
        for (int idSubTask : epic.getSubTasks()) {
            listSubTasks.add(subTasks.get(idSubTask));
            System.out.println(subTasks.get(idSubTask));
        }
        return listSubTasks;
    }

    public void updateStatus(int epicId) {
        ArrayList<Integer> listSubTasks = epics.get(epicId).getSubTasks();
        if (listSubTasks == null || listSubTasks.isEmpty()) {
            epics.get(epicId).setStatus(Status.NEW);
            return;
        }
        Status status = subTasks.get(listSubTasks.get(0)).getStatus();
        for (int idSubTask : listSubTasks) {
            if (status != subTasks.get(idSubTask).getStatus()) {
                status = Status.IN_PROGRESS;
                break;
            }
        }
        epics.get(epicId).setStatus(status);
    }

    public void showHistory() {
        historyManager.getHistory();
    }
}
