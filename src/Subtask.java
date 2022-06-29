public class Subtask extends Task{
    private final int epicsID;

    public Subtask(String name, String description, String status, Epic epic) {
        super(name, description, status);
        this.epicsID = epic.getIdNumber();
    }

    public int getEpicsID() {
        return epicsID;
    }
}
