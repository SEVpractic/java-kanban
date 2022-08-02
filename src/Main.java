import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Browsing_history_is_in_order(); //проверка правильности построения истории просмотров.
        Browsing_history_does_not_contain_duplicates(); //проверка отсутствия повторов в истории.
        Browsing_history_does_not_contain_deleted_items(); //проверка отсутствия в истории удаленных элеметов.
    }

    public static void Browsing_history_is_in_order() {
        TaskManager taskManager = Managers.getDefault();

        fillTaskManager(taskManager);

        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        List<Task> expectedHistory = List.of(
                taskManager.getTaskByID(1),
                taskManager.getTaskByID(2),
                taskManager.getEpicByID(3),
                taskManager.getSubtaskByID(4),
                taskManager.getSubtaskByID(5),
                taskManager.getSubtaskByID(6),
                taskManager.getEpicByID(7)
        );
        var actualHistory = taskManager.getHistory().getInMemoryHistory();

        if (!expectedHistory.equals(actualHistory)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Browsing_history_does_not_contain_duplicates() {
        TaskManager taskManager = Managers.getDefault();

        fillTaskManager(taskManager);

        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        taskManager.getTaskByID(1);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(3);

        List<Task> expectedHistory = List.of(
                taskManager.getTaskByID(2),
                taskManager.getEpicByID(7),
                taskManager.getTaskByID(1),
                taskManager.getSubtaskByID(4),
                taskManager.getSubtaskByID(5),
                taskManager.getSubtaskByID(6),
                taskManager.getEpicByID(3)
        );
        var actualHistory = taskManager.getHistory().getInMemoryHistory();

        if (!expectedHistory.equals(actualHistory)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Browsing_history_does_not_contain_deleted_items() {
        TaskManager taskManager = Managers.getDefault();

        fillTaskManager(taskManager);

        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        taskManager.deliteTaskByID(1);
        taskManager.deliteEpicByID(3);

        List<Task> expectedHistory = List.of(taskManager.getTaskByID(2),
                taskManager.getEpicByID(7));
        var actualHistory = taskManager.getHistory().getInMemoryHistory();

        if (!expectedHistory.equals(actualHistory)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void fillTaskManager(TaskManager taskManager) {
        Task task1 = new Task("Первая задача", "ID 1",
                taskManager.generateIdNumber(), Status.NEW);
        taskManager.addTasks(task1);

        Task task2 = new Task("Вторая задача", "ID 2",
                taskManager.generateIdNumber(), Status.NEW);
        taskManager.addTasks(task2);

        Epic epic1 = new Epic("Первый эпик", "ID 3",
                taskManager.generateIdNumber(), Status.NEW, new ArrayList<>());
        taskManager.addEpics(epic1);

        Subtask subtask11 = new Subtask("Первая подзадача первого эпика",
                "ID 4", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask11);

        Subtask subtask12 = new Subtask("Вторая подзадача первого эпика",
                "ID 5", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask12);

        Subtask subtask13 = new Subtask("Третья подзадача первого эпика",
                "ID 6", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask13);

        Epic epic2 = new Epic("Второй эпик", "ID 7",
                taskManager.generateIdNumber(), Status.NEW, new ArrayList<>());
        taskManager.addEpics(epic2);
    }
}
