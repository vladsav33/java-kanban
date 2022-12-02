package task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
    }

    public void addSubtask(int subtaskId) {
        subTasks.add(subtaskId);
    }

    public void removeSubtask(int subtaskId) {
        subTasks.remove((Integer) subtaskId);
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void removeSubTasks() {
        subTasks.clear();
    }
}
