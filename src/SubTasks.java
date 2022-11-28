import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SubTasks extends Tasks {
    private HashMap<Integer, Integer> subTasks;
    Scanner scanner;

    public SubTasks() {
        subTasks = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public Task createTask(String name, String description, Epics epics) {
        System.out.println("Введите номер эпика для этой подзадачи:");
        int id = scanner.nextInt();
        if (!epics.hasKey(id)) {
            System.out.println("Такого эпика нет");
            return null;
        }
        Task task = super.createTask(name, description);
        subTasks.put(Manager.lastId(), id);
        epics.addSubTask(id, Manager.lastId());
        return task;
    }

    public Task updateTask(int id, Epics epics) {
        Task task = super.updateTask(id);
        if (task !=null) {
            epics.updateStatus(subTasks.get(id), this); // Update status of the parent epic
        }
        epics.updateStatus(id, this);
        return task;
    }

    public Task createTask(String name, String description, Epics epics, int id) {
        if (!epics.hasKey(id)) {
            System.out.println("Такого эпика нет");
            return null;
        }
        Task task = super.createTask(name, description);
        subTasks.put(Manager.lastId(), id);
        epics.addSubTask(id, Manager.lastId());
        epics.updateStatus(id, this);
        return task;
    }

    public Status getStatus(int id) {
        if (super.tasks.get(id) == null) {
            return null;
        }
        return super.tasks.get(id).status;
    }

    public void removeTaskById(int id, Epics epics) {
        super.removeTaskById(id);
        int epicId = subTasks.get(id);
        ArrayList<Integer> listSubTasks = epics.getSubTasks(epicId);
        listSubTasks.remove((Integer) id);
        epics.updateSubTasks(id, listSubTasks);
        epics.updateStatus(epicId, this);
    }
}
