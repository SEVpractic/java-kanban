package manager;

import task.Task;
import java.util.LinkedList;

public interface HistoryManager {

    public void add(Task task);

    public LinkedList<Task> getHistory();
}
