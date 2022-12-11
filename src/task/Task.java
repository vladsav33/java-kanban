package task;

import manager.InMemoryTaskManager;

public class Task {
    final protected int id;
    final protected String name;
    final protected String description;
    protected Status status;

    public Task(String name, String description) {
        this.id = InMemoryTaskManager.nextId();
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + " - " + description + " - " + status;
    }
}
