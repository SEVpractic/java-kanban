package task;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> includeSubtasksIDs;
    private final TasksType type;

    public Epic(String name, String description, int idNumber, Status status, ArrayList<Integer> includeSubtasksIDs) {
        super(name, description, idNumber, status);
        this.type = TasksType.EPIC;
        this.includeSubtasksIDs = includeSubtasksIDs;
    }

    public ArrayList<Integer> getIncludeSubtasksIDs() {
        return includeSubtasksIDs;
    }

    @Override
    public String toString() {

        return String.format("%s,%s,%s,%s,%s",
                idNumber, type, name, status, description);
    }
}
