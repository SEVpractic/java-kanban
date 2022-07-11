package manager;

public class Managers {
    private static final HistoryManager historyManager = new InMemoryHistoryManager();
    private static final TaskManager taskManager = new InMemoryTaskManager();

    private Managers() {
    }

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
