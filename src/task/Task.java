package task;

import java.util.Objects;

public class Task {
    private final String name;
    private final String description;
    private final int idNumber;
    private final Status status;

    public Task(String name, String description, int idNumber, Status status) {
        this.name = name;
        this.description = description;
        this.idNumber = idNumber;
        this.status = status;
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
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", idNumber=" + idNumber +
                ", status=" + status +
                '}';
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
