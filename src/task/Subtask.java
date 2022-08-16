package task;

public class Subtask extends Task {
    private final int epicsID;
    private final TasksType type;

    public Subtask(String name, String description, int idNumber, Status status, int epicsID) {
        super(name, description, idNumber, status);
        this.type = TasksType.SUBTASK;
        this.epicsID = epicsID;
    }

    public int getEpicsID() {
        return epicsID;
    }

    @Override
    public String toString() {

        return String.format("%s,%s,%s,%s,%s,%s",
                idNumber, type, name, status, description, epicsID);
    }
}
