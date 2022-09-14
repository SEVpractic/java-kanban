package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> includeSubtasksIDs;
    private final LocalDateTime endTime; // время завершения задачи

    public Epic(String name, String description, int idNumber, Status status, Duration duration,
                LocalDateTime startTime, ArrayList<Integer> includeSubtasksIDs, LocalDateTime endTime) {
        super(name, description, idNumber, status, duration, startTime);
        this.includeSubtasksIDs = includeSubtasksIDs;
        this.endTime = endTime;
    }

    public ArrayList<Integer> getIncludeSubtasksIDs() {
        return includeSubtasksIDs;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                idNumber, type, name, status, description, duration, startTime);
    }
}
