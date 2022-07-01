package task;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> includeSubtasksIDs;

    public Epic(String name, String description, int idNumber, Status status, ArrayList<Integer> includeSubtasksIDs) {
        super(name, description, idNumber, status);
        this.includeSubtasksIDs = includeSubtasksIDs;
    }

    public ArrayList<Integer> getIncludeSubtasksIDs() {
        return includeSubtasksIDs;
    }
}
