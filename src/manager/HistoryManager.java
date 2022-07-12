package manager;

import task.Task;
import java.util.LinkedList;

public interface HistoryManager {
    public static final LinkedList<Task> history = new LinkedList<>();
    public void add(Task task);

    public LinkedList<Task> getHistory();
}
