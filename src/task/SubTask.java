package task;

public class SubTask extends Task {
    public int epicId;

    public SubTask(int id, String name, String description, int epicId) {
        super(id, name, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Status getStatus() {
        return status;
    }
}
