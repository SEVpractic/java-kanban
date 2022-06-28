public class Subtask extends Task{
    private final int epicsID;

    public Subtask(String name, String description, String status, int epicsID) {
        super(name, description, status);
        this.epicsID = epicsID;
    }

    public int getEpicsID() {
        return epicsID;
    }
}
