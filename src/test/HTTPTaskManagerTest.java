package test;

import manager.HTTPTaskManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest extends TaskManagerTest {
    KVServer server;
    HttpClient httpClient;

    @BeforeEach
    void fillTaskManager() throws IOException {
        server = new KVServer();
        server.start();

        taskManager = Managers.getDefault("http://localhost:8078");

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
    void stopServer() {
        server.stop();
    }

    @Test
    void Save_to_server_correct() {
        String expectedContent =
                        "id,type,name,status,description,duration,startTime,epic\n" +
                        "1,TASK,Task1,NEW,Description task1,PT1H,2022-09-01T00:00\n" +
                        "2,TASK,Task2,NEW,Description task2,PT1H,2022-09-02T00:00\n" +
                        "3,EPIC,Epic1,NEW,Description epic1,PT2H30M,2022-09-04T00:00\n" +
                        "7,EPIC,Epic2,NEW,Description epic2,PT0S,2022-09-07T00:00\n" +
                        "4,SUBTASK,Subtask1,NEW,Description subtask1,PT1H,2022-09-04T00:00,3\n" +
                        "5,SUBTASK,Subtask2,NEW,Description subtask2,PT30M,2022-09-05T00:00,3\n" +
                        "6,SUBTASK,Subtask3,NEW,Description subtask3,PT1H,2022-09-06T00:00,3\n\n";

        String actualContent = "";
        String key = "";

        httpClient = HttpClient.newHttpClient();
        URI URL = URI.create("http://localhost:8078/register");
        HttpRequest request = HttpRequest.newBuilder().uri(URL).GET().build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                key = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        httpClient = HttpClient.newHttpClient();
        URL = URI.create("http://localhost:8078/load/?API_TOKEN=" + key);
        request = HttpRequest.newBuilder().uri(URL).GET().build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                actualContent = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(expectedContent, actualContent,
                "Не корректное содержание value экземпляра класса HTTPTaskManager с пустой историей");

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

        httpClient = HttpClient.newHttpClient();
        URL = URI.create("http://localhost:8078/load/?API_TOKEN=" + key);
        request = HttpRequest.newBuilder().uri(URL).GET().build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                actualContent = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(expectedContent, actualContent,
                "Не корректное содержание value экземпляра класса HTTPTaskManager с заполненной историей");
    }

    @Test
    void Load_from_server_correct() {
        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        assertEquals(taskManager.toString(), HTTPTaskManager.loadFromServer("http://localhost:8078").toString(),
                "Содержимое экземпляра класса HTTPTaskManager не корректно восстанавливается c сервера");
    }
}
