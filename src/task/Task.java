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
    public LocalTime startTime;
    protected transient Duration duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!name.equals(task.name)) return false;
        if (!description.equals(task.description)) return false;
        if (status != task.status) return false;
        if (type != task.type) return false;
        if (startTime != null ? !startTime.equals(task.startTime) : task.startTime != null) return false;
        return duration != null ? duration.equals(task.duration) : task.duration == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }

    public Task(String name, String description) {
        this.id = InMemoryTaskManager.nextId();
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.type = Type.TASK;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
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
}
