package test;

import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {
    T taskManager;

    @Test
    void Tasks_creation_correct() {
        HashMap<Integer, Task> expectedTasks = new HashMap<>();
        expectedTasks.put(1, new Task("Task1", "Description task1", 1, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 01, 00, 00, 00)));
        expectedTasks.put(2, new Task("Task2", "Description task2", 2, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 02, 00, 00, 00)));

        HashMap<Integer, Task> actualTasks = taskManager.getTasks();

        assertNotNull(taskManager.getTaskByID(1), "Задача не найдена.");
        assertEquals(expectedTasks.get(1), taskManager.getTaskByID(1), "Задачи не совпадают.");

        assertNotNull(actualTasks, "Задачи на возвращаются.");
        assertEquals(2, actualTasks.size(), "Неверное количество задач в списке.");
        assertEquals(expectedTasks, actualTasks, "Списки задач не совпадают.");
    }

    @Test
    void Epics_creation_correct() {
        HashMap<Integer, Epic> expectedEpics = new HashMap<>();
        expectedEpics.put(3, new Epic("Epic1", "Description epic1", 3,
                Status.NEW, Duration.ofMinutes(150),
                LocalDateTime.of(2022, 9, 04, 00, 00, 00),
                new ArrayList<>(Arrays.asList(4, 5, 6)),
                LocalDateTime.of(2022, 9, 04, 00, 00, 00)));
        expectedEpics.put(7, new Epic("Epic2", "Description epic2", 7,
                Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2022, 9, 07, 00, 00, 00),
                new ArrayList<>(),
                LocalDateTime.of(2022, 9, 07, 00, 00, 00)));

        HashMap<Integer, Epic> actualEpics = taskManager.getEpics();

        assertNotNull(taskManager.getEpicByID(3), "Эпик не найден.");
        assertEquals(expectedEpics.get(3), taskManager.getEpicByID(3), "Эпики не совпадают.");

        assertNotNull(actualEpics, "Эпики не возвращаются.");
        assertEquals(2, actualEpics.size(), "Неверное количество эпиков в списке.");
        assertEquals(expectedEpics, actualEpics, "Списки эпиков не совпадают.");
    }

    @Test
    void Subtasks_creation_correct() {
        HashMap<Integer, Subtask> expectedSubtasks = new HashMap<>();
        expectedSubtasks.put(4, new Subtask("Subtask1", "Description subtask1",
                4, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 04, 00, 00, 00),3));
        expectedSubtasks.put(5, new Subtask("Subtask2", "Description subtask2",
                5, Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2022, 9, 05, 00, 00, 00),3));
        expectedSubtasks.put(6, new Subtask("Subtask3", "Description subtask3",
                6, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 06, 00, 00, 00),3));

        HashMap<Integer, Subtask> actualSubtasks = taskManager.getSubtasks();

        assertNotNull(taskManager.getSubtaskByID(4), "Подзадача не найдена.");
        assertEquals(expectedSubtasks.get(4), taskManager.getSubtaskByID(4), "Подзадачи не совпадают.");
        assertEquals(3, taskManager.getSubtaskByID(4).getEpicsID(), "В сабтаске сохранен неверный ID эпика");

        assertNotNull(actualSubtasks, "Подзадачи на возвращаются.");
        assertEquals(3, actualSubtasks.size(), "Неверное количество подзадач в списке.");
        assertEquals(expectedSubtasks, actualSubtasks, "Списки подзадач не совпадают.");
    }

    @Test
    void Tasks_deletion_correct() {
        HashMap<Integer, Task> expectedTasks = new HashMap<>();
        expectedTasks.put(2, new Task("Task2", "Description task2",2,
                Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 02, 00, 00, 00)));

        taskManager.deleteTaskByID(1);
        Task actualTask = taskManager.getTaskByID(1);
        HashMap<Integer, Task> actualTasks = taskManager.getTasks();

        assertNull(actualTask, "Задача удаляется не корректно.");
        assertEquals(1, actualTasks.size(), "Неверное количество задач в списке.");
        assertEquals(expectedTasks, actualTasks, "Списки задач не совпадают.");
    }

    @Test
    void Epics_deletion_correct() {
        HashMap<Integer, Epic> expectedEpics = new HashMap<>();
        expectedEpics.put(7, new Epic("Epic2", "Description epic2", 7,
                Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2022, 9, 07, 00, 00, 00),
                new ArrayList<>(), LocalDateTime.of(2022, 9, 07, 00, 00, 00)));

        taskManager.deleteEpicByID(3);
        Task actualEpic = taskManager.getEpicByID(3);
        HashMap<Integer, Epic> actualEpics = taskManager.getEpics();

        assertNull(actualEpic, "Эпик удаляется не корректно.");
        assertEquals(1, actualEpics.size(), "Неверное количество эпиков в списке.");
        assertEquals(expectedEpics, actualEpics, "Списки эпиков не совпадают.");
    }

    @Test
    void Subtasks_deletion_correct() {
        HashMap<Integer, Subtask> expectedSubtasks = new HashMap<>();
        expectedSubtasks.put(5, new Subtask("Subtask2", "Description subtask2",
                5, Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2022, 9, 05, 00, 00, 00),3));
        expectedSubtasks.put(6, new Subtask("Subtask3", "Description subtask3",
                6, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 06, 00, 00, 00),3));

        taskManager.deleteSubtaskByID(4);
        Task actualSubtask = taskManager.getSubtaskByID(4);
        HashMap<Integer, Subtask> actualSubtasks = taskManager.getSubtasks();

        assertNull(actualSubtask, "Подзадача удаляется не корректно.");
        assertEquals(2, actualSubtasks.size(), "Неверное количество подзадач в списке.");
        assertEquals(expectedSubtasks, actualSubtasks, "Списки подзадач не совпадают.");
    }

    @Test
    void Subtasks_And_Epic_deletion_correct() {
        taskManager.deleteEpicByID(3);
        HashMap<Integer, Subtask> actualSubtasks = taskManager.getSubtasks();

        assertEquals(0, actualSubtasks.size(), "При удалении эпика подзадачи удаляются не корректно.");
    }

    @Test
    void Tasks_status_change_correct() {
        Task task = new Task("Task1", "Description task1", 1,
                Status.IN_PROGRESS, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 01, 00, 00, 00));
        taskManager.updateTasks(task);

        Status expectedStatus = Status.IN_PROGRESS;
        Status actualStatus = taskManager.getTaskByID(1).getStatus();

        assertNotNull(taskManager.getTaskByID(1), "Задача не найдена.");
        assertEquals(expectedStatus, actualStatus, "Статусы задачи не совпадают.");
    }

    @Test
    void Epics_status_change_correct() {
        Epic epic = new Epic("Epic1", "Description epic1", 3,
                Status.IN_PROGRESS, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 03, 00, 00, 00),
                new ArrayList<>(Arrays.asList(4, 5, 6)),
                LocalDateTime.of(2022, 9, 03, 00, 00, 00));
        taskManager.updateEpics(epic);

        Status expectedStatus = Status.IN_PROGRESS;
        Status actualStatus = taskManager.getEpicByID(3).getStatus();

        assertNotNull(taskManager.getEpicByID(3), "Эпик не найден.");
        assertEquals(expectedStatus, actualStatus, "Статусы эпиков не совпадают.");
    }

    @Test
    void Subtasks_status_change_correct() {
        Subtask subtask = new Subtask("Subtask1", "Description subtask1",
                4, Status.IN_PROGRESS, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 04, 00, 00, 00),3);
        taskManager.addSubtasks(subtask);

        Status expectedStatus = Status.IN_PROGRESS;
        Status actualStatus = taskManager.getSubtaskByID(4).getStatus();

        assertNotNull(taskManager.getSubtaskByID(4), "Сабтаск не найден.");
        assertEquals(expectedStatus, actualStatus, "Статусы сабтасков не совпадают.");
    }

    @Test
    public void Epics_status_IN_PROGRESS_change_correct() {
        Subtask subtask = new Subtask("Subtask1", "Description subtask1",
                4, Status.IN_PROGRESS, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 04, 00, 00, 00),3);
        taskManager.updateSubtasks(subtask);

        Status expectedStatus = Status.IN_PROGRESS;
        Status actualStatus = taskManager.getEpicByID(3).getStatus();

        assertNotNull(taskManager.getEpicByID(3), "Эпик не найден.");
        assertNotNull(taskManager.getSubtaskByID(4), "Сабтаск не найден.");
        assertEquals(expectedStatus, actualStatus, "Статусы эпиков не совпадают.");
    }

    @Test
    void Epics_status_DONE_change_correct() {
        Subtask subtask = new Subtask("Subtask1", "Description subtask1",
                4, Status.DONE, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 04, 00, 00, 00),3);
        taskManager.updateSubtasks(subtask);

        subtask = new Subtask("Subtask2", "Description subtask2",
                5, Status.DONE,Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 05, 00, 00, 00),3);
        taskManager.updateSubtasks(subtask);

        subtask = new Subtask("Subtask3", "Description subtask3",
                6, Status.DONE, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 05, 00, 00, 00),3);
        taskManager.updateSubtasks(subtask);

        Status expectedStatus = Status.DONE;
        Status actualStatus = taskManager.getEpicByID(3).getStatus();

        assertNotNull(taskManager.getEpicByID(3), "Эпик не найден.");
        assertEquals(expectedStatus, actualStatus, "Статусы эпиков не совпадают.");
    }

    @Test
    void Epics_status_DONE_change_correct_2() {
        Subtask subtask = new Subtask("Subtask1", "Description subtask1",
                4, Status.DONE, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 04, 00, 00, 00),3);
        taskManager.updateSubtasks(subtask);

        taskManager.deleteSubtaskByID(5);
        taskManager.deleteSubtaskByID(6);

        Status expectedStatus = Status.DONE;
        Status actualStatus = taskManager.getEpicByID(3).getStatus();

        assertNotNull(taskManager.getEpicByID(3), "Эпик не найден.");
        assertEquals(expectedStatus, actualStatus, "Статусы эпиков не совпадают.");
    }

    @Test
    void Browsing_history_is_in_order() {
        List<Task> actualHistory = taskManager.getHistory().getInMemoryHistory();
        assertNotNull(actualHistory, "История не пустая.");
        assertEquals(0, actualHistory.size(), "История не пустая.");

        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        List<Integer> actualIdNumberHistory = taskManager.getHistory().getInMemoryHistory().stream()
                .map(Task::getIdNumber).collect(Collectors.toList());
        List<Integer> expectedIdNumberHistory = List.of(1, 2, 3, 4, 5, 6, 7);

        assertNotNull(actualHistory, "История не пустая.");
        assertEquals(expectedIdNumberHistory.size(), actualIdNumberHistory.size(),
                "Размер истории не совпадает.");
        assertEquals(expectedIdNumberHistory, actualIdNumberHistory, "Порядок истории не верный.");
    }

    @Test
    void Browsing_history_does_not_contain_duplicates() {
        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        taskManager.getTaskByID(1);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(3);

        List<Integer> actualHistory = taskManager.getHistory().getInMemoryHistory().stream()
                .map(Task::getIdNumber).collect(Collectors.toList());
        List<Integer> expectedHistory = List.of(2, 7, 1, 4, 5, 6, 3);

        assertNotNull(actualHistory, "История не пустая.");
        assertEquals(expectedHistory.size(), actualHistory.size(), "История не пустая.");
        assertEquals(expectedHistory, actualHistory, "История содержит дубликаты.");
    }

    @Test
    void Browsing_history_does_not_contain_deleted_items() {
        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        taskManager.deleteTaskByID(1);
        taskManager.deleteEpicByID(3);

        List<Integer> actualHistory = taskManager.getHistory().getInMemoryHistory().stream()
                .map(Task::getIdNumber).collect(Collectors.toList());
        List<Integer> expectedHistory = List.of(2, 7);

        assertNotNull(actualHistory, "История не пустая.");
        assertEquals(expectedHistory.size(), actualHistory.size(), "История не пустая.");
        assertEquals(expectedHistory, actualHistory, "История содержит удаленные позиции.");
    }

    @Test
    void Browsing_history_does_not_contain_deleted_items2() {
        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();

        List<Integer> actualHistory = taskManager.getHistory().getInMemoryHistory().stream()
                .map(Task::getIdNumber).collect(Collectors.toList());
        List<Integer> expectedHistory = List.of(3, 7);

        assertNotNull(actualHistory, "История не пустая.");
        assertEquals(expectedHistory.size(), actualHistory.size(), "История не пустая.");
        assertEquals(expectedHistory, actualHistory, "История содержит удаленные позиции.");
    }

    @Test
    void Epics_duration_change_correct() {
        Duration expectedEpicsDuration = Duration.ofMinutes(150);
        Duration actualEpicsDuration = taskManager.getEpicByID(3).getDuration();

        assertEquals(expectedEpicsDuration, actualEpicsDuration, "Длительность эпика рассчитана не верно");

        LocalDateTime expectedEpicsStartTime = LocalDateTime.of(2022, 9, 04, 0, 00, 00);
        LocalDateTime actualEpicsStartTime = taskManager.getEpicByID(3).getStartTime();

        assertEquals(expectedEpicsStartTime, actualEpicsStartTime, "Начало эпика рассчитано не верно");

        LocalDateTime expectedEpicsEndTime = LocalDateTime.of(2022, 9, 06, 1, 00, 00);
        LocalDateTime actualEpicsEndTime = taskManager.getEpicByID(3).getEndTime();

        assertEquals(expectedEpicsEndTime, actualEpicsEndTime, "Окончание эпика рассчитано не верно");
    }

    @Test
    void Tasks_prioritization_correct() {
        List<Integer> expectedPrioritizedTasks = List.of(1, 2, 4, 5, 6);
        List<Integer> actualPrioritizedTasks = taskManager.getPrioritizedTasks()
                .stream().map(Task::getIdNumber).collect(Collectors.toList());

        assertEquals(expectedPrioritizedTasks, actualPrioritizedTasks, "Сортировка по времени не верна");

        assertThrows(IllegalArgumentException.class,
                () -> taskManager.isTimeValid(new Subtask("NewSubtask1", "Description task",
                        taskManager.generateIdNumber(), Status.NEW, null, null, 3)),
                "Создание задачи с null не вызывает исключение");
    }

    @Test
    void Tasks_prioritization_correct_after_tasks_deletion() {
        taskManager.deleteTaskByID(1);

        List<Integer> expectedPrioritizedTasksID = List.of(2, 4, 5, 6);
        List<Integer> actualPrioritizedTasksID = taskManager.getPrioritizedTasks()
                .stream().map(Task::getIdNumber).collect(Collectors.toList());

        assertEquals(expectedPrioritizedTasksID, actualPrioritizedTasksID,
                "Сортировка по времени не верна после удаления");

        taskManager.deleteAllTasks();
        expectedPrioritizedTasksID = List.of(4, 5, 6);
        actualPrioritizedTasksID = taskManager.getPrioritizedTasks()
                .stream().map(Task::getIdNumber).collect(Collectors.toList());

        assertEquals(expectedPrioritizedTasksID, actualPrioritizedTasksID,
                "Сортировка по времени не верна после удаления");

        taskManager.deleteAllSubtasks();
        expectedPrioritizedTasksID = List.of();
        actualPrioritizedTasksID = taskManager.getPrioritizedTasks()
                .stream().map(Task::getIdNumber).collect(Collectors.toList());

        assertEquals(expectedPrioritizedTasksID, actualPrioritizedTasksID,
                "Сортировка по времени не верна после удаления");
    }

    @Test
    void Time_validation_defence_correct() {
        assertThrows(IllegalArgumentException.class,
                () -> taskManager.addTasks(new Task("NewTask1", "Description task",
                        taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(60),
                        LocalDateTime.of(2022, 9, 01, 00, 30, 00))),
                "Валидация по времени не корректно проверяет пересечение (начало нового - внутри имеющегося)");

        assertThrows(IllegalArgumentException.class,
                () -> taskManager.addTasks(new Task("NewTask2", "Description task",
                        taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(60),
                        LocalDateTime.of(2022, 9, 01, 23, 30, 00))),
                "Валидация по времени не корректно проверяет пересечение (длительность нового - внутри имеющегося)");
    }
}