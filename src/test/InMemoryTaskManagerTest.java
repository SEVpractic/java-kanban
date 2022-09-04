package test;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void fillTaskManager() {
        taskManager = Managers.getDefault();

        Task task1 = new Task("Task1", "Description task1",
                taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 01, 00, 00, 00));
        taskManager.addTasks(task1);

        Task task2 = new Task("Task2", "Description task2",
                taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 02, 00, 00, 00));
        taskManager.addTasks(task2);

        Epic epic1 = new Epic("Epic1", "Description epic1",
                taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2022, 9, 03, 00, 00, 00),
                new ArrayList<>(), LocalDateTime.of(2022, 9, 03, 00, 00, 00));
        taskManager.addEpics(epic1);

        Subtask subtask11 = new Subtask("Subtask1", "Description subtask1",
                taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 04, 00, 00, 00),
                epic1.getIdNumber());
        taskManager.addSubtasks(subtask11);

        Subtask subtask12 = new Subtask("Subtask2", "Description subtask2",
                taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2022, 9, 05, 00, 00, 00),
                epic1.getIdNumber());
        taskManager.addSubtasks(subtask12);

        Subtask subtask13 = new Subtask("Subtask3", "Description subtask3",
                taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2022, 9, 06, 00, 00, 00),
                epic1.getIdNumber());
        taskManager.addSubtasks(subtask13);

        Epic epic2 = new Epic("Epic2", "Description epic2",
                taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2022, 9, 07, 00, 00, 00),
                new ArrayList<>(), LocalDateTime.of(2022, 9, 07, 00, 00, 00));
        taskManager.addEpics(epic2);
    }


}