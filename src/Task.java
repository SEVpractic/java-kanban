public class Task {
    private final String name;
    private final String description;
    private int idNumber;
    private String status; //может принимать значения "NEW", "IN_PROGRESS", "DONE"

    public Task(String name, String description, String status){
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", idNumber=" + idNumber +
                ", status='" + status + '\'' +
                '}';
    }
}
