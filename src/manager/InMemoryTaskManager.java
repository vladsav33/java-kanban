package manager;

import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static int id;
    protected static Map<Integer, Task> tasks;
    protected static Map<Integer, Epic> epics;
    protected static Map<Integer, SubTask> subTasks;
    protected static HistoryManager historyManager;

    public Set<Task> sortedSet;

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        sortedSet = new TreeSet<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                if (o1.getStartTime() == null && o2.getStartTime() == null) {
                    return o1.hashCode() - o2.hashCode();
                }
                if (o1.getStartTime() == null) {
                    return 1;
                }
                if (o2.getStartTime() == null) {
                    return -1;
                }
                if (o1.getStartTime().isBefore(o2.getStartTime())) {
                    return -1;
                }
                if (o1.getStartTime().isAfter(o2.getStartTime())) {
                    return 1;
                }
                return 0;
            }
        });
    }

    public static int nextId() {
        return ++id;
    }

    public List<Task> getPrioritizedTasks() {
        List<Task> list = new ArrayList<>(sortedSet);
        return list;
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
        for (Task task : tasks.values()) {
            sortedSet.remove(task);
        }
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        for (SubTask subTask : subTasks.values()) {
            sortedSet.remove(subTask);
        }
        subTasks.clear();
    }

    public void deleteAllSubtasks() {
        for (SubTask subTask : subTasks.values()) {
            sortedSet.remove(subTask);
        }
        subTasks.clear();
    }

    public void removeTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            sortedSet.remove(task);
            historyManager.remove(id);
        }
    }

    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (int subTaskId : subTasks.keySet()) {
                if (id == subTaskId) {
//                    subTasks.remove(subTaskId);
                    removeSubtaskById(subTaskId);
                    historyManager.remove(subTaskId);
                }
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    public void removeSubtaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            System.out.println("Задача с идентификатором " + id + " удалена");
            updateStatusAndEndTime(subTask.getEpicId());
            sortedSet.remove(subTask);
            historyManager.remove(id);
        }
    }

    public List<Task> showAllTasks() {
        List<Task> result = new ArrayList<>(tasks.values());
        for (Task task : result) {
            System.out.println("Идентификатор: " + task.getId() + " Задача: " + task);
        }
        return result;
    }

    public List<Epic> showAllEpics() {
        List<Epic> result = new ArrayList<>(epics.values());
        for (Epic epic : result) {
            System.out.println("Идентификатор: " + epic.getId() + " Эпик: " + epic);
        }
        return result;
    }

    public List<SubTask> showAllSubtasks() {
        List<SubTask> result = new ArrayList<>(subTasks.values());
        for (SubTask subTask : result) {
            System.out.println("Идентификатор: " + subTask.getId() + " Подзадача: " + subTask);
        }
        return result;
    }

    public Task createTask(String name, String description) {
        Task task = new Task(name, description);
        if (conflictTask(task)) {
            System.out.println("Обнаружен конфликт времени для задачи");
            return null;
        }
        tasks.put(task.getId(), task);
        sortedSet.add(task);
        return task;
    }

    public Epic createEpic(String name, String description) {
        Epic epic = new Epic(name, description);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask createSubtask(String name, String description, int idEpic) {
        SubTask subTask = new SubTask(name, description, idEpic);
        if (conflictTask(subTask)) {
            System.out.println("Обнаружен конфликт времени для задачи");
            return null;
        }
        subTasks.put(subTask.getId(), subTask);
        updateStatusAndEndTime(idEpic);
        sortedSet.add(subTask);
        return subTask;
    }

    public Task updateTask(Task task) {
        Task taskToReplace = tasks.get(task.getId());
        if (conflictTask(task)) {
            System.out.println("Обнаружен конфликт времени для задачи");
            return null;
        }
        tasks.put(task.getId(), task);
        sortedSet.remove(taskToReplace);
        sortedSet.add(task);
        return task;
    }

    public Task updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Task updateSubtask(SubTask subTask) {
        SubTask subTaskToReplace = subTasks.get(subTask.getId());
        if (conflictTask(subTask)) {
            System.out.println("Обнаружен конфликт времени для задачи");
            return null;
        }
        subTasks.put(subTask.getId(), subTask);
        updateStatusAndEndTime(subTask.getEpicId());
        sortedSet.remove(subTaskToReplace);
        sortedSet.add(subTask);
        return subTask;
    }

    public List<SubTask> getAllEpicById(Epic epic) {
        List<SubTask> listSubTasks = new ArrayList<>();
        for (Map.Entry<Integer, SubTask> item : subTasks.entrySet()) {
            if(item.getValue().getEpicId() == epic.getId()) {
                listSubTasks.add(item.getValue());
            }
        }
        return listSubTasks;
    }

    private void updateStatusAndEndTime(int epicId) {
        List<Integer> listSubTasks = new LinkedList<>();
        for (Map.Entry<Integer, SubTask> item : subTasks.entrySet()) {
            if(item.getValue().getEpicId() == epicId) {
                listSubTasks.add(item.getKey());
            }
        }

        if (listSubTasks == null || listSubTasks.isEmpty()) {
            Epic epic = epics.get(epicId);
            epic.setStatus(Status.NEW);
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
            return;
        }
        Status status = subTasks.get(listSubTasks.get(0)).getStatus();
        LocalTime start = subTasks.get(listSubTasks.get(0)).getStartTime();
        LocalTime finish = subTasks.get(listSubTasks.get(0)).getEndTime();
        for (int idSubTask : listSubTasks) {
            SubTask subTask = subTasks.get(idSubTask);
            if (status != subTask.getStatus()) {
                status = Status.IN_PROGRESS;
            }
            if (subTask.getStartTime() != null && subTask.getStartTime().isBefore(start)) {
                start = subTask.getStartTime();
            }
            if (subTask.getEndTime() != null && subTask.getEndTime().isAfter(finish)) {
                finish = subTask.getEndTime();
            }
        }
        epics.get(epicId).setStatus(status);
        epics.get(epicId).setStartTime(start);
        epics.get(epicId).setEndTime(finish);
        if (start != null && finish != null) {
            epics.get(epicId).setDuration(Duration.between(start, finish));
        } else {
            epics.get(epicId).setDuration(null);
        }
    }

    public void showHistory() {
        List<Task> list = historyManager.getHistory();
        for (Task item : list) {
            System.out.print(item.getId() + " ");
        }
        System.out.print("\n");
    }

    public boolean conflictTask(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null) {
            return false;
        }
        for (Task taskInList : getPrioritizedTasks()) {
            if (taskInList.getStartTime() == null || taskInList.getEndTime() == null) {
                continue;
            }
            if (taskInList.getId() == task.getId()) {
                continue;
            }
            if (task.getStartTime().isAfter(taskInList.getStartTime()) && task.getStartTime().isBefore(taskInList.getEndTime())) {
                return true;
            }
            if (task.getEndTime().isAfter(taskInList.getStartTime()) && task.getEndTime().isBefore(taskInList.getEndTime())) {
                return true;
            }
        }
        return false;
    }
}
