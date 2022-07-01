package task;

public class Subtask extends Task {
    private final int epicsID;

    public Subtask(String name, String description, int idNumber, Status status, int epicsID) {
        super(name, description, idNumber, status);
        this.epicsID = epicsID;
    }

    public int getEpicsID() {
        return epicsID;
    }
}
