package task;

import java.time.LocalTime;

public class Epic extends Task {
    LocalTime endTime;

    @Override
    public String toString() {
        return id + "," + type + "," + status + "," + name + "," + description + "," + startTime + "," + endTime;
    }

    @Override
    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Epic(String name, String description) {
        super(name, description);
        this.type = Type.EPIC;
    }

    public Epic(int id, Type type, Status status, String name, String description) {
        super(id, type, status, name, description);
    }

    public Epic(int id, Type type, Status status, String name, String description, LocalTime startTime, LocalTime endTime) {
        super(id, type, status, name, description);
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
