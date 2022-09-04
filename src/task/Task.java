package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected final String name;
    protected final String description;
    protected final int idNumber;
    protected final Status status;
    protected final TasksType type;
    protected final Duration duration; // продолжительность задачи в минутах
    protected final LocalDateTime startTime; // время начала выполнения задачи

    public Task(String name, String description, int idNumber, Status status,
                Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.idNumber = idNumber;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.type = TasksType.TASK;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                idNumber, type, name, status, description, duration, startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return idNumber == task.idNumber && Objects.equals(name, task.name)
                && Objects.equals(description, task.description) && status == task.status
                && type == task.type && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, idNumber, status, type, duration, startTime);
    }
}
