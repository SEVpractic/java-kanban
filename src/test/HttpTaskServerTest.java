package test;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpTaskServerTest {
    TaskManager taskManager;
    HttpTaskServer httpTaskServer;
    KVServer kvServer;
    static Gson gson;

    @BeforeAll
    static void buildGson() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new TypeAdapter<Duration>() {
                    @Override
                    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
                        jsonWriter.value(duration.toMinutes());
                    }

                    @Override
                    public Duration read(JsonReader jsonReader) throws IOException {
                        return Duration.ofMinutes(jsonReader.nextInt());
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                    private static final DateTimeFormatter formatter =
                            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

                    @Override
                    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.format(formatter));
                    }

                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString(), formatter);
                    }
                })
                .create();
    }

    @BeforeEach
    void fillTaskManager() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        taskManager = Managers.getDefault("http://localhost:8078");

        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

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
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void client_return_correct_task() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String actualTask = "";

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                actualTask = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getTaskByID(1), gson.fromJson(actualTask, Task.class),
                "Возвращается некорректная задача");
    }

    @Test
    void client_return_correct_subtask() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String actualSubtask = "";

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                actualSubtask = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getSubtaskByID(4), gson.fromJson(actualSubtask, Subtask.class),
                "Возвращается некорректная подзадача");
    }

    @Test
    void client_return_correct_epic() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String actualEpic = "";

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                actualEpic = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getEpicByID(3), gson.fromJson(actualEpic, Epic.class),
                "Возвращается некорректный эпик");
    }

    @Test
    void client_return_correct_tasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String actualTasks = "";

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                actualTasks = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(gson.toJson(taskManager.getTasks()), actualTasks,
                "Возвращается некорректный список задач");
    }

    @Test
    void client_return_correct_subtasks() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String actualSubtasks = "";

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                actualSubtasks = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(gson.toJson(taskManager.getSubtasks()), actualSubtasks,
                "Возвращается некорректный список подзадач");
    }

    @Test
    void client_return_correct_epics() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String actualEpics = "";

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                actualEpics = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(gson.toJson(taskManager.getEpics()), actualEpics,
                "Возвращается некорректный список эпиков");
    }

    @Test
    void client_delete_tasks_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getTasks().size(), 0,
                "Возвращается некорректный список эпиков");
    }

    @Test
    void client_delete_subtasks_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getSubtasks().size(), 0,
                "Возвращается некорректный список эпиков");
    }

    @Test
    void client_delete_epics_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epics/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getEpics().size(), 0,
                "Возвращается некорректный список эпиков");
    }

    @Test
    void client_delete_task_by_ID_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getTaskByID(1), null,
                "Возвращается некорректный список эпиков");
    }

    @Test
    void client_delete_subtask_by_ID_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getSubtaskByID(4), null,
                "Возвращается некорректный список эпиков");
    }

    @Test
    void client_delete_epic_by_ID_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epics/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getEpicByID(3), null,
                "Возвращается некорректный список эпиков");
    }

    @Test
    void client_update_task_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");

        String value =
                "{\n" +
                "\"name\": \"Task1\",\n" +
                "\"description\": \"Description task1\",\n" +
                "\"idNumber\": 1,\n" +
                "\"status\": \"DONE\",\n" +
                "\"type\": \"TASK\",\n" +
                "\"duration\": 60,\n" +
                "\"startTime\": \"01-09-2022 00:00\"\n" +
	            "}";

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getTaskByID(1).getStatus(), Status.DONE,
                "Клиент не верно обновляет задачу");
    }

    @Test
    void client_add_task_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");

        String value =
                "{\n" +
                        "\"name\": \"Task3\",\n" +
                        "\"description\": \"Description task3\",\n" +
                        "\"idNumber\": 8,\n" +
                        "\"status\": \"NEW\",\n" +
                        "\"type\": \"TASK\",\n" +
                        "\"duration\": 60,\n" +
                        "\"startTime\": \"01-09-2023 00:00\"\n" +
                        "}";

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        Task task = new Task("Task3", "Description task3",
                8, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2023, 9, 01, 00, 00, 00));

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getTaskByID(8), task,
                "Клиент не верно добавляет задачу");
    }

    @Test
    void client_update_subtask_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");

        String value =
                "{\n" +
                        "\"epicsID\": 3,\n" +
                        "\"name\": \"Subtask1\",\n" +
                        "\"description\": \"Description subtask1\",\n" +
                        "\"idNumber\": 4,\n" +
                        "\"status\": \"DONE\",\n" +
                        "\"type\": \"SUBTASK\",\n" +
                        "\"duration\": 60,\n" +
                        "\"startTime\": \"04-09-2022 00:00\"\n" +
                        "}";

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getSubtaskByID(4).getStatus(), Status.DONE,
                "Клиент не верно обновляет подзадачу");
    }

    @Test
    void client_add_subtask_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");

        String value =
                "{\n" +
                        "\"epicsID\": 3,\n" +
                        "\"name\": \"Subtask4\",\n" +
                        "\"description\": \"Description subtask4\",\n" +
                        "\"idNumber\": 8,\n" +
                        "\"status\": \"NEW\",\n" +
                        "\"type\": \"SUBTASK\",\n" +
                        "\"duration\": 60,\n" +
                        "\"startTime\": \"04-09-2023 00:00\"\n" +
                        "}";

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        Subtask subtask = new Subtask("Subtask4", "Description subtask4",
                8, Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2023, 9, 04, 00, 00, 00),
                3);

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getSubtaskByID(8), subtask,
                "Клиент не верно добавляет подзадачу");
    }

    @Test
    void client_update_epic_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");

        String value =
                "{\n" +
                        "\"includeSubtasksIDs\": [" +
                        "4," +
                        "5," +
                        "6," +
                        "]," +
                        "\"endTime\": \"06-09-2022 01:00\"," +
                        "\"name\": \"Epic1\",\n" +
                        "\"description\": \"Description epic1\",\n" +
                        "\"idNumber\": 3,\n" +
                        "\"status\": \"DONE\",\n" +
                        "\"type\": \"EPIC\",\n" +
                        "\"duration\": 150,\n" +
                        "\"startTime\": \"04-09-2022 00:00\"\n" +
                        "}";

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getEpicByID(3).getStatus(), Status.DONE,
                "Клиент не верно обновляет подзадачу");
    }

    @Test
    void client_add_epic_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");

        String value =
                "{\n" +
                        "\"includeSubtasksIDs\": [" +
                        "]," +
                        "\"endTime\": \"07-09-2023 00:00\"," +
                        "\"name\": \"Epic3\",\n" +
                        "\"description\": \"Description epic3\",\n" +
                        "\"idNumber\": 8,\n" +
                        "\"status\": \"NEW\",\n" +
                        "\"type\": \"EPIC\",\n" +
                        "\"duration\": 0,\n" +
                        "\"startTime\": \"07-09-2023 00:00\"\n" +
                        "}";

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(value);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        Epic epic = new Epic("Epic3", "Description epic3",
                8, Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2023, 9, 07, 00, 00, 00),
                new ArrayList<>(), LocalDateTime.of(2022, 9, 07, 00, 00, 00));

        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(taskManager.getEpicByID(8), epic,
                "Клиент не верно добавляет подзадачу");
    }

    @Test
    void client_return_subtasks_by_epic_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String actualSubtasks = "";


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                actualSubtasks = response.body();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(gson.toJson(taskManager.getSubtasks()), actualSubtasks,
                "Возвращается некорректный список подзадач из под эпика");
    }

    @Test
    void client_return_history_correct() {
        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        List<Integer> actualHistory = new ArrayList<>();
        List<Integer> expectedHistory = List.of(1, 2, 3, 4, 5, 6, 7);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                jsonArray.forEach((element) -> actualHistory.add(gson.fromJson(element, Task.class).getIdNumber()));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(expectedHistory, actualHistory, "Клиент возвращает историю не корректно");
    }

    @Test
    void client_return_prioritization_correct() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        List<Integer> expectedPrioritizedTasks = List.of(1, 2, 4, 5, 6);
        List<Integer> actualPrioritizedTasks = new ArrayList<>();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                jsonArray.forEach((element) -> actualPrioritizedTasks.add(gson.fromJson(element, Task.class).getIdNumber()));
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(expectedPrioritizedTasks, actualPrioritizedTasks, "Клиент возвращает не верный список prioritization");
    }
}
