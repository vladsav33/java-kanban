package task;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subTasks;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        subTasks = new ArrayList<>();
    }

    public void addSubtask(int subtaskId) {
        subTasks.add(subtaskId);
    }

    public void removeSubtask(int subtaskId) {
        subTasks.remove((Integer) subtaskId);
    }
}
