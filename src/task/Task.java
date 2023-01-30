package task;

import manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalTime;

public class Task {
    protected final int id;
    protected final String name;
    protected final String description;
    protected Status status;
    protected Type type;

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    protected LocalTime startTime;
    protected Duration duration;

    public Task(String name, String description) {
        this.id = InMemoryTaskManager.nextId();
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.type = Type.TASK;
    }

    public Task(int id, Type type, Status status, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task(int id, Type type, Status status, String name, String description, LocalTime startTime, Duration duration) {
        this(id, type, status, name, description);
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.status = status;
//        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
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
        return id + "," + type + "," + status + "," + name + "," + description + "," + startTime + "," + duration;
    }

    Task fromString(String value) {
        String[] elements = value.split(",");
        switch (Type.valueOf(elements[1])) {

            case TASK:
                Task task = new Task(Integer.parseInt(elements[0]), Type.valueOf(elements[1]), Status.valueOf(elements[2]),
                        elements[3], elements[4]);
                return task;
            case EPIC:
                Epic epic = new Epic(Integer.parseInt(elements[0]), Type.valueOf(elements[1]), Status.valueOf(elements[2]),
                        elements[3], elements[4]);
                return epic;
            case SUBTASK:
                SubTask subTask = new SubTask(Integer.parseInt(elements[0]), Type.valueOf(elements[1]), Status.valueOf(elements[2]),
                        elements[3], elements[4], Integer.parseInt(elements[5]));
                return subTask;
        }
        return null;
    }
}
