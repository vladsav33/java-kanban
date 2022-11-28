import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Epics extends Tasks {
    private HashMap<Integer, ArrayList<Integer>> epics;

    public Epics() {
        epics = new HashMap<>();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        epics.clear();
    }

    public HashMap<Integer, Task> showAllTasks(SubTasks subTasks) {
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            System.out.println("Идентификатор: " + entry.getKey() + " Зпик: " + entry.getValue());
            ArrayList<Integer> listSubTasks = epics.get(entry.getKey());
            for (Integer id : listSubTasks) {
                subTasks.showTaskById(id);
            }
        }
        return tasks;
    }

    public Task showTaskById(int id, SubTasks subTasks) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Эпик: " + task);
        ArrayList<Integer> listSubTasks = epics.get(id);
        if (listSubTasks == null) {
            return task;
        }
        for (Integer idSubTask : listSubTasks) {
            subTasks.showTaskById(idSubTask);
        }
        return task;
    }

    public void removeTaskById(int id, SubTasks subTasks) {
        super.removeTaskById(id);
        ArrayList<Integer> listSubTasks = epics.get(id);
        if (listSubTasks == null) {
            return;
        }
        for (Integer idSubTask : listSubTasks) {
            subTasks.tasks.remove(idSubTask);
        }
        epics.remove(id);
    }

    @Override
    public Task createTask(String name, String description) {
        Task task = super.createTask(name, description);
        epics.put(Manager.lastId(), new ArrayList<>());
        return task;
    }

    public boolean hasKey(int id) {
        return epics.containsKey(id);
    }

    public void addSubTask(int epicId, int subTaskId) {
        ArrayList<Integer> listSubTasks = epics.get(epicId);
        listSubTasks.add(subTaskId);
    }

    @Override
    public Task updateTask(int id) {
        Scanner scanner = new Scanner(System.in);

        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        System.out.println("Идентификатор: " + id + " Эпик: " + task);
        System.out.println("Введите название задачи:");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        task = new Task(name, description);
        tasks.put(id, task);
        return task;
    }

    public void updateStatus(int id, SubTasks subTasks) {
        Task task = tasks.get(id);
        System.out.println("Идентификатор: " + id + " Эпик: " + task);
        ArrayList<Integer> listSubTasks = epics.get(id);
        if (listSubTasks == null || listSubTasks.isEmpty()) {
            return;
        }
        Status status = subTasks.getStatus(listSubTasks.get(0));
        for (Integer idSubTask : listSubTasks) {
            if (status != subTasks.getStatus(idSubTask)) {
                status = Status.IN_PROGRESS;
                break;
            }
        }
        task.status = status;
    }

    public ArrayList<Integer> getSubTasks(int id) {
        return epics.get(id);
    }

    public void updateSubTasks(int id, ArrayList<Integer> list) {
        epics.put(id, list);
    }
}
