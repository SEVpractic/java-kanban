package task;

import java.util.Objects;

public class Task {
    protected final String name;
    protected final String description;
    protected final int idNumber;
    protected final Status status;
    protected final TasksType type;

    public Task(String name, String description, int idNumber, Status status) {
        this.name = name;
        this.description = description;
        this.idNumber = idNumber;
        this.status = status;
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

    @Override
    public String toString() {

        return String.format("%s,%s,%s,%s,%s",
                idNumber, type, name, status, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return idNumber == task.idNumber && Objects.equals(name, task.name)
                && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, idNumber, status);
    }
}
