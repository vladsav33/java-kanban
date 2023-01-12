package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    final private List<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
        this.type = Type.EPIC;
    }

    public Epic(int id, Type type, Status status, String name, String description) {
        super(id, type, status, name, description);
        subTasks = new ArrayList<>();
    }

//    public void addSubtask(int subtaskId) {
//        subTasks.add(subtaskId);
//    }
//
//    public void removeSubtask(int subtaskId) {
//        subTasks.remove((Integer) subtaskId);
//    }
//
//    public List<Integer> getSubTasks() {
//        return subTasks;
//    }
//
//    public void removeSubTasks() {
//        subTasks.clear();
//    }
}
