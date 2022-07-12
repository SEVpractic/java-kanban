package manager;

import task.Task;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    @Override
    public void add(Task task) {
        int maxSize = 10;

        if (task != null) {
            history.addLast(task);
        }
        if (history.size() > maxSize) {
            history.removeFirst();
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }
}
