package test;

import manager.FileBackedTasksManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                        "7,EPIC,Epic2,NEW,Description epic2,PT0S,2022-09-07T00:00\n" +
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
                        "7,EPIC,Epic2,NEW,Description epic2,PT0S,2022-09-07T00:00\n" +
                        "4,SUBTASK,Subtask1,NEW,Description subtask1,PT1H,2022-09-04T00:00,3\n" +
                        "5,SUBTASK,Subtask2,NEW,Description subtask2,PT30M,2022-09-05T00:00,3\n" +
                        "6,SUBTASK,Subtask3,NEW,Description subtask3,PT1H,2022-09-06T00:00,3\n\n" +
                        "1,2,3,4,5,6,7";

        actualContent = taskManager.toString();

        assertEquals(expectedContent, actualContent,
                "Не корректное содержание файла экземпляра класса FileBackedTasksManager с заполненной историей");

        assertThrows(RuntimeException.class, () -> {
            file.setReadOnly();
            taskManager.addTasks(new Task("Task", "Description task",
                    taskManager.generateIdNumber(), Status.NEW, Duration.ofMinutes(60),
                    LocalDateTime.of(2022, 9, 10, 00, 00, 00)));
        },
                "Не выбрасывается исключение при ошибке сохранения");
    }

    @Test
    void Load_from_file_correct() {
        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        assertEquals(taskManager.toString(), FileBackedTasksManager.loadFromFile(file).toString(),
                "Содержимое экземпляра класса FileBackedTasksManager не корректно восстанавливается из файла");

        file.delete();
        assertThrows(RuntimeException.class,
                () -> FileBackedTasksManager.loadFromFile(file),
                "Не выбрасывается исключение при создании экземпляра из отсутствующего файла");

        File file1 = new File("src/test1.csv");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file1, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,name,status,description,duration,startTime,epic\n aa,bb,cc,dd");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThrows(IllegalArgumentException.class,
                () -> FileBackedTasksManager.loadFromFile(file1),
                "Не выбрасывается исключение при создании экземпляра из неверно заполненного файла");
        file1.delete();
    }
}
