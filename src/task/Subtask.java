package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicsID;
    private final TasksType type;

    public Subtask(String name, String description, int idNumber, Status status,
                   Duration duration, LocalDateTime startTime, int epicsID) {
        super(name, description, idNumber, status, duration, startTime);
        this.type = TasksType.SUBTASK;
        this.epicsID = epicsID;
    }

    public int getEpicsID() {
        return epicsID;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                idNumber, type, name, status, description, duration, startTime, epicsID);
    }
}
