package task;

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
        return "task.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", idNumber=" + idNumber +
                ", status='" + status + '\'' +
                '}';
    }
}
