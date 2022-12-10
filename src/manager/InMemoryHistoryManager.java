package manager;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> history;
    public InMemoryHistoryManager() {
        history = new ArrayList<>(10);
    }
    public void add(Task task) {
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }

    public ArrayList<Task> getHistory() {
        for (Task item : history) {
            System.out.print(item.getId() + " ");
        }
        System.out.print("\n");
        return history;
    }
}
