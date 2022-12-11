package manager;

import task.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    final static int HISTORY_SIZE = 10;
    final private LinkedList<Task> history;
    public InMemoryHistoryManager() {
        history = new LinkedList<>();
    }
    public void add(Task task) {
        if (history.size() == HISTORY_SIZE) {
            history.removeFirst();
        }
        history.add(task);
    }

    public List<Task> getHistory() {
        for (Task item : history) {
            System.out.print(item.getId() + " ");
        }
        System.out.print("\n");
        return history;
    }
}
