package manager;

public class Managers {

    public static TaskManager getDefault(String source) {
        return new HTTPTaskManager(source);
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedManager(String source) {
        return new FileBackedTasksManager(source);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
