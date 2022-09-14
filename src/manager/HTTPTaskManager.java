package manager;

import server.KVTaskClient;

import java.util.Arrays;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    KVTaskClient client;

    public HTTPTaskManager(String source) {
        super(source);
        client = new KVTaskClient(source);
    }

    public static TaskManager loadFromServer(String source) {
        HTTPTaskManager tasksManager = new HTTPTaskManager(source);
        List<String> lines = Arrays.stream(tasksManager.client.load().split("\n")).toList();

        tasksManager.fillTaskManager(lines);
        tasksManager.fillHistory(InMemoryHistoryManager.historyFromString(lines.get(lines.size() - 1)));

        return tasksManager;
    }

    @Override
    protected void save() {
        client.put(toString());
    }
}
