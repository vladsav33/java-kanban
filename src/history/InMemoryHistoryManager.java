package history;

import manager.Node;
import task.Task;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> history;
    private final static Map<Integer, Node> mapTask = new HashMap<>();

    public InMemoryHistoryManager() {
        history = new CustomLinkedList<>();
    }

    public void add(Task task) {
        history.linkLast(task);
    }

    public void remove(int id) {
        Node node = mapTask.get(id);
        if (node != null) {
            history.removeNode(node);
            mapTask.remove(id);
        }
    }

    public List<Task> getHistory() {
        List<Task> listHistory = history.getTasks();
        return listHistory;
    }

    public class CustomLinkedList<T> {
        private Node head;
        private Node tail;

        private void removeNode(Node node) {
            if (node.getPrev() != null) {
                node.getPrev().setNext(node.getNext());
            }
            if (node.getNext() != null) {
                node.getNext().setPrev(node.getPrev());
            }
            if (node == head) {
                head = node.getNext();
            }
            if (node == tail) {
                tail = node.getPrev();
            }
        }
        private void linkLast(Task task) {
            Node newNode = new Node(task, null, null);
            if (mapTask.containsKey(task.getId())) {
                removeNode(mapTask.get(task.getId()));
                mapTask.remove(task.getId());
            }
            mapTask.put(task.getId(), newNode);
            if (tail != null) {
                tail.setNext(newNode);
                newNode.setPrev(tail);
            } else {
                head = newNode;
            }
            tail = newNode;
        }
        private List<Task> getTasks() {
            List<Task> taskList = new ArrayList<>();
            Node current = head;
            while (current != null) {
                taskList.add(current.getTask());
                current = current.getNext();
            }
            return taskList;
        }
    }
}
