import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // набор тестов класса FileBackedTasksManager:
        Tasks_creation_correct(Managers.getFileBackedManager(new File("src/test.csv")));// проверка корректности создания коллекции задач.
        Epics_creation_correct(Managers.getFileBackedManager(new File("src/test.csv"))); // проверка корректности создания коллекции эпиков.
        Subtasks_creation_correct(Managers.getFileBackedManager(new File("src/test.csv"))); // проверка корректности создания коллекции подзадач.
        Tasks_deletion_correct(Managers.getFileBackedManager(new File("src/test.csv"))); // проверка корректности удаления задач.
        Epics_deletion_correct(Managers.getFileBackedManager(new File("src/test.csv"))); // проверка корректности удаления эпиков.
        Subtasks_deletion_correct(Managers.getFileBackedManager(new File("src/test.csv"))); // проверка корректности удаления подзадач.
        Subtasks_And_Epic_deletion_correct(Managers.getFileBackedManager(new File("src/test.csv"))); // проверка корректности удаления подзадач при удалении эпика.
        Tasks_status_change_correct(Managers.getFileBackedManager(new File("src/test.csv"))); //проверка корректности смены статуса задачи при прямом изменении.
        Epics_status_change_correct(Managers.getFileBackedManager(new File("src/test.csv"))); //проверка корректности смены статуса эпика при прямом изменении.
        Subtasks_status_change_correct(Managers.getFileBackedManager(new File("src/test.csv")));  //проверка корректности смены статуса подзадачи при прямом изменении.
        Epics_status_IN_PROGRESS_change_correct(Managers.getFileBackedManager(new File("src/test.csv"))); //проверка корректности смены статуса эпика при изменении статуса подзадачи.
        Epics_status_DONE_change_correct(Managers.getFileBackedManager(new File("src/test.csv")));  //проверка корректности смены статуса эпика при изменении статуса всех подзадач на DONE.
        Epics_status_DONE_change_correct_2(Managers.getFileBackedManager(new File("src/test.csv"))); //проверка корректности смены статуса эпика при изменении статуса одной подзадачи на DONE и удалении остальных.
        Browsing_history_is_in_order(Managers.getFileBackedManager(new File("src/test.csv"))); //проверка правильности построения истории просмотров.
        Browsing_history_does_not_contain_duplicates(Managers.getFileBackedManager(new File("src/test.csv"))); //проверка отсутствия повторов в истории.
        Browsing_history_does_not_contain_deleted_items(Managers.getFileBackedManager(new File("src/test.csv"))); //проверка отсутствия в истории удаленных элементов.

        // набор тестов класса InMemoryTaskManager:
        Tasks_creation_correct(Managers.getDefault()); // проверка корректности создания коллекции задач.
        Epics_creation_correct(Managers.getDefault()); // проверка корректности создания коллекции эпиков.
        Subtasks_creation_correct(Managers.getDefault()); // проверка корректности создания коллекции подзадач.
        Tasks_deletion_correct(Managers.getDefault()); // проверка корректности удаления задач.
        Epics_deletion_correct(Managers.getDefault()); // проверка корректности удаления эпиков.
        Subtasks_deletion_correct(Managers.getDefault()); // проверка корректности удаления подзадач.
        Subtasks_And_Epic_deletion_correct(Managers.getDefault()); // проверка корректности удаления подзадач при удалении эпика.
        Tasks_status_change_correct(Managers.getDefault()); //проверка корректности смены статуса задачи при прямом изменении.
        Epics_status_change_correct(Managers.getDefault()); //проверка корректности смены статуса эпика при прямом изменении.
        Subtasks_status_change_correct(Managers.getDefault());  //проверка корректности смены статуса подзадачи при прямом изменении.
        Epics_status_IN_PROGRESS_change_correct(Managers.getDefault()); //проверка корректности смены статуса эпика при изменении статуса подзадачи.
        Epics_status_DONE_change_correct(Managers.getDefault());  //проверка корректности смены статуса эпика при изменении статуса всех подзадач на DONE.
        Epics_status_DONE_change_correct_2(Managers.getDefault()); //проверка корректности смены статуса эпика при изменении статуса одной подзадачи на DONE и удалении остальных.
        Browsing_history_is_in_order(Managers.getDefault()); //проверка правильности построения истории просмотров.
        Browsing_history_does_not_contain_duplicates(Managers.getDefault()); //проверка отсутствия повторов в истории.
        Browsing_history_does_not_contain_deleted_items(Managers.getDefault()); //проверка отсутствия в истории удаленных элементов.
    }

    public static void Tasks_creation_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        HashMap<Integer, Task> expectedTasks = new HashMap<>();
        expectedTasks.put(1, new Task("Task1", "Description task1", 1, Status.NEW));
        expectedTasks.put(2, new Task("Task2", "Description task2",2, Status.NEW));

        HashMap<Integer, Task> actualTasks = taskManager.getTasks();

        if (!actualTasks.equals(expectedTasks)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Epics_creation_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        HashMap<Integer, Epic> expectedEpics = new HashMap<>();
        expectedEpics.put(3, new Epic("Epic1", "Description epic1", 3,
                Status.NEW, new ArrayList<>(Arrays.asList(4, 5, 6))));
        expectedEpics.put(7, new Epic("Epic2", "Description epic2", 7,
                Status.NEW, new ArrayList<>()));

        HashMap<Integer, Epic> actualEpics = taskManager.getEpics();

        if (!actualEpics.equals(expectedEpics)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Subtasks_creation_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        HashMap<Integer, Subtask> expectedSubtasks = new HashMap<>();
        expectedSubtasks.put(4, new Subtask("Subtask1", "Description subtask1",
                4, Status.NEW, 3));
        expectedSubtasks.put(5, new Subtask("Subtask2", "Description subtask2",
                5, Status.NEW, 3));
        expectedSubtasks.put(6, new Subtask("Subtask3", "Description subtask3",
                6, Status.NEW, 3));

        HashMap<Integer, Subtask> actualSubtasks = taskManager.getSubtasks();

        if (!actualSubtasks.equals(expectedSubtasks)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    private static void Tasks_deletion_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        taskManager.deliteTaskByID(1);

        Task expectedTask = null;
        Task actualTask = taskManager.getTaskByID(1);

        if (actualTask != expectedTask) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    private static void Epics_deletion_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        taskManager.deliteEpicByID(3);

        Task expectedEpic = null;
        Task actualEpic = taskManager.getEpicByID(3);

        if (actualEpic != expectedEpic) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    private static void Subtasks_deletion_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        taskManager.deliteSubtaskByID(4);

        Task expectedSubtask = null;
        Task actualSubtask = taskManager.getSubtaskByID(4);

        if (actualSubtask != expectedSubtask) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    private static void Subtasks_And_Epic_deletion_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        taskManager.deliteEpicByID(3);

        HashMap<Integer, Subtask> expectedSubtasks = new HashMap<>();
        HashMap<Integer, Subtask> actualSubtasks = taskManager.getSubtasks();

        if (!actualSubtasks.equals(expectedSubtasks)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Tasks_status_change_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        Task task = new Task("Task1", "Description task1", 1, Status.IN_PROGRESS);
        taskManager.updateTasks(task);

        Status expectedStatus = Status.IN_PROGRESS;
        Status actualStatus = taskManager.getTaskByID(1).getStatus();

        if (actualStatus != expectedStatus) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Epics_status_change_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        Epic epic = new Epic("Epic1", "Description epic1", 3,
                Status.IN_PROGRESS, new ArrayList<>(Arrays.asList(4, 5, 6)));
        taskManager.updateEpics(epic);

        Status expectedStatus = Status.IN_PROGRESS;
        Status actualStatus = taskManager.getEpicByID(3).getStatus();

        if (actualStatus != expectedStatus) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Subtasks_status_change_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        Subtask subtask = new Subtask("Subtask1", "Description subtask1",
                4, Status.IN_PROGRESS, 3);
        taskManager.addSubtasks(subtask);

        Status expectedStatus = Status.IN_PROGRESS;
        Status actualStatus = taskManager.getSubtaskByID(4).getStatus();

        if (actualStatus != expectedStatus) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Epics_status_IN_PROGRESS_change_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        Subtask subtask = new Subtask("Subtask1", "Description subtask1",
                4, Status.IN_PROGRESS, 3);
        taskManager.updateSubtasks(subtask);

        Status expectedStatus = Status.IN_PROGRESS;
        Status actualStatus = taskManager.getEpicByID(3).getStatus();

        if (actualStatus != expectedStatus) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Epics_status_DONE_change_correct(TaskManager taskManager) {
        fillTaskManager(taskManager);

        Subtask subtask = new Subtask("Subtask1", "Description subtask1",
                4, Status.DONE, 3);
        taskManager.updateSubtasks(subtask);

        subtask = new Subtask("Subtask2", "Description subtask2",
                5, Status.DONE,3);
        taskManager.updateSubtasks(subtask);

        subtask = new Subtask("Subtask3", "Description subtask3",
                6, Status.DONE, 3);
        taskManager.updateSubtasks(subtask);

        Status expectedStatus = Status.DONE;
        Status actualStatus = taskManager.getEpicByID(3).getStatus();

        if (actualStatus != expectedStatus) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Epics_status_DONE_change_correct_2(TaskManager taskManager) {
        fillTaskManager(taskManager);

        Subtask subtask = new Subtask("Subtask1", "Description subtask1",
                4, Status.DONE, 3);
        taskManager.updateSubtasks(subtask);

        taskManager.deliteSubtaskByID(5);
        taskManager.deliteSubtaskByID(6);

        Status expectedStatus = Status.DONE;
        Status actualStatus = taskManager.getEpicByID(3).getStatus();

        if (actualStatus != expectedStatus) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Browsing_history_is_in_order(TaskManager taskManager) {
        fillTaskManager(taskManager);

        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        List<Task> actualHistory = taskManager.getHistory().getInMemoryHistory();
        List<Task> expectedHistory = List.of(
                new Task("Task1", "Description task1", 1, Status.NEW),
                new Task("Task2", "Description task2",2, Status.NEW),
                new Epic("Epic1", "Description epic1", 3, Status.NEW, new ArrayList<>(Arrays.asList(4, 5, 6))),
                new Subtask("Subtask1", "Description subtask1", 4, Status.NEW, 3),
                new Subtask("Subtask2", "Description subtask2", 5, Status.NEW,3),
                new Subtask("Subtask3", "Description subtask3", 6, Status.NEW, 3),
                new Epic("Epic2", "Description epic2", 7, Status.NEW, new ArrayList<>())
                );

        if (!expectedHistory.equals(actualHistory)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Browsing_history_does_not_contain_duplicates(TaskManager taskManager) {
        fillTaskManager(taskManager);

        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        taskManager.getTaskByID(1);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(3);

        List<Task> actualHistory = taskManager.getHistory().getInMemoryHistory();
        List<Task> expectedHistory = List.of(
                new Task("Task2", "Description task2",2, Status.NEW),
                new Epic("Epic2", "Description epic2", 7, Status.NEW, new ArrayList<>()),
                new Task("Task1", "Description task1", 1, Status.NEW),
                new Subtask("Subtask1", "Description subtask1", 4, Status.NEW, 3),
                new Subtask("Subtask2", "Description subtask2", 5, Status.NEW,3),
                new Subtask("Subtask3", "Description subtask3", 6, Status.NEW, 3),
                new Epic("Epic1", "Description epic1", 3, Status.NEW, new ArrayList<>(Arrays.asList(4, 5, 6)))
                );

        if (!expectedHistory.equals(actualHistory)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void Browsing_history_does_not_contain_deleted_items(TaskManager taskManager) {
        fillTaskManager(taskManager);

        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        taskManager.deliteTaskByID(1);
        taskManager.deliteEpicByID(3);

        List<Task> actualHistory = taskManager.getHistory().getInMemoryHistory();
        List<Task> expectedHistory = List.of(
                new Task("Task2", "Description task2",2, Status.NEW),
                new Epic("Epic2", "Description epic2", 7, Status.NEW, new ArrayList<>())
                );

        if (!expectedHistory.equals(actualHistory)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static void fillTaskManager(TaskManager taskManager) {
        Task task1 = new Task("Task1", "Description task1",
                taskManager.generateIdNumber(), Status.NEW);
        taskManager.addTasks(task1);

        Task task2 = new Task("Task2", "Description task2",
                taskManager.generateIdNumber(), Status.NEW);
        taskManager.addTasks(task2);

        Epic epic1 = new Epic("Epic1", "Description epic1",
                taskManager.generateIdNumber(), Status.NEW, new ArrayList<>());
        taskManager.addEpics(epic1);

        Subtask subtask11 = new Subtask("Subtask1",
                "Description subtask1", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask11);

        Subtask subtask12 = new Subtask("Subtask2",
                "Description subtask2", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask12);

        Subtask subtask13 = new Subtask("Subtask3",
                "Description subtask3", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask13);

        Epic epic2 = new Epic("Epic2", "Description epic2",
                taskManager.generateIdNumber(), Status.NEW, new ArrayList<>());
        taskManager.addEpics(epic2);
    }
}
