package task;

import java.time.Duration;
import java.time.LocalTime;

public class SubTask extends Task {
    final private int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public SubTask(int id, Type type, Status status, String name, String description, int epicId) {
        super(id, type, status, name, description);
        this.epicId = epicId;
    }

    public SubTask(int id, Type type, Status status, String name, String description, int epicId, LocalTime startTime, Duration duration) {
        super(id, type, status, name, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + status + "," + name + "," + description + "," + epicId + "," + startTime +"," + duration;
    }
}
