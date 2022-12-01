package task;

public class Task {
    public int id;
    public String name;
    public String description;
    public Status status;

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(int id, String name, String description, Status status) {
        this(id, name, description);
        this.status = status;
    }

    @Override
    public String toString() {
        return name + " - " + description + " - " + status;
    }
}
