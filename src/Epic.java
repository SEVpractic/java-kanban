import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> includeSubtasksIDs;

    public Epic(String name, String description, String status) {
        super(name, description, status);
        includeSubtasksIDs = new ArrayList<>();
    }

    public void setIncludeSubtasksIDs(Subtask subtask){
        includeSubtasksIDs.add(subtask.getIdNumber());
    }

    public ArrayList<Integer> getIncludeSubtasksIDs() {
        return includeSubtasksIDs;
    }
}
