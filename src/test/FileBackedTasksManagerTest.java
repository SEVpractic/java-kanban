package test;

import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest {
    File file;

    @BeforeEach
    void fillTaskManager() {
        file = new File("src/test.csv");
        taskManager = Managers.getFileBackedManager(file);

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
                new ArrayList<>());
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
                taskManager.generateIdNumber(), Status.NEW, new ArrayList<>());
        taskManager.addEpics(epic2);
    }

    @AfterEach
    void deliteFile() {
        file.delete();
    }

    @Test
    void Save_to_file_correct() {
        String expectedContent =
                        "id,type,name,status,description,duration,startTime,epic\n" +
                        "1,TASK,Task1,NEW,Description task1,PT1H,2022-09-01T00:00\n" +
                        "2,TASK,Task2,NEW,Description task2,PT1H,2022-09-02T00:00\n" +
                        "3,EPIC,Epic1,NEW,Description epic1,PT2H30M,2022-09-04T00:00\n" +
                        "7,EPIC,Epic2,NEW,Description epic2,null,null\n" +
                        "4,SUBTASK,Subtask1,NEW,Description subtask1,PT1H,2022-09-04T00:00,3\n" +
                        "5,SUBTASK,Subtask2,NEW,Description subtask2,PT30M,2022-09-05T00:00,3\n" +
                        "6,SUBTASK,Subtask3,NEW,Description subtask3,PT1H,2022-09-06T00:00,3\n\n";

        String actualContent = taskManager.toString();

        assertEquals(expectedContent, actualContent,
                "Не корректное содержание файла экземпляра класса FileBackedTasksManager с пустой историей");

        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        expectedContent =
                "id,type,name,status,description,duration,startTime,epic\n" +
                        "1,TASK,Task1,NEW,Description task1,PT1H,2022-09-01T00:00\n" +
                        "2,TASK,Task2,NEW,Description task2,PT1H,2022-09-02T00:00\n" +
                        "3,EPIC,Epic1,NEW,Description epic1,PT2H30M,2022-09-04T00:00\n" +
                        "7,EPIC,Epic2,NEW,Description epic2,null,null\n" +
                        "4,SUBTASK,Subtask1,NEW,Description subtask1,PT1H,2022-09-04T00:00,3\n" +
                        "5,SUBTASK,Subtask2,NEW,Description subtask2,PT30M,2022-09-05T00:00,3\n" +
                        "6,SUBTASK,Subtask3,NEW,Description subtask3,PT1H,2022-09-06T00:00,3\n\n" +
                        "1,2,3,4,5,6,7";

        actualContent = taskManager.toString();

        assertEquals(expectedContent, actualContent,
                "Не корректное содержание файла экземпляра класса FileBackedTasksManager с заполненной историей");
    }

    @Test
    void Load_from_file_correct() {
        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        TaskManager taskManagerNew = FileBackedTasksManager.loadFromFile(file);

        String expectedManager = taskManager.toString();
        String actualManager = taskManagerNew.toString();

        assertEquals(expectedManager, actualManager,
                "Содержимое экземпляра класса FileBackedTasksManager не корректно восстанавливается из файла");
    }
}
