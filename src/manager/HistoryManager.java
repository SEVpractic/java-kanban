package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {

    public void add(Task task);

    public void add(Task[] tasks);

    public void remove(int idNumber);

    public void remove(Integer[] idNumbers);

    public List<Task> getInMemoryHistory();
}
