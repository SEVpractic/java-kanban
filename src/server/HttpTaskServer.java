package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;

    private final Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = buildGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task", this::handleTasksRequest);
        server.createContext("/tasks/subtask", this::handleSubtasksRequest);
        server.createContext("/tasks/epic", this::handleEpicsRequest);
        server.createContext("/tasks/subtask/epic", this::handleSubtasksByEpic);
        server.createContext("/tasks/history", this::handleHistoryRequest);
        server.createContext("/tasks", this::handlePrioritizedTasksRequest);
    }

    private void handlePrioritizedTasksRequest(HttpExchange h) throws IOException{
        try (h) {
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, gson.toJson(taskManager.getPrioritizedTasks()));
            } else {
                h.sendResponseHeaders(405, 0);
            }
        }
    }

    private void handleHistoryRequest(HttpExchange h) throws IOException{
        try (h) {
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, gson.toJson(taskManager.getHistory().getInMemoryHistory()));
            } else {
                h.sendResponseHeaders(405, 0);
            }
        }
    }

    private void handleSubtasksByEpic(HttpExchange h) throws IOException {
        try (h) {
            if ("GET".equals(h.getRequestMethod())) {
                String id = h.getRequestURI().getRawQuery().substring("id=".length());
                if (id.isEmpty()) {
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                sendText(h, gson.toJson(taskManager.getSubtasksByEpicsID(Integer.parseInt(id))));
            } else {
                h.sendResponseHeaders(405, 0);
            }
        }
    }

    private void handleEpicsRequest(HttpExchange h) throws IOException {
        try (h) {
            switch (h.getRequestMethod()) {
                case "GET":
                    if (h.getRequestURI().getRawQuery() == null) {
                        sendText(h, gson.toJson(taskManager.getEpics()));
                    } else {
                        int id = Integer.parseInt(h.getRequestURI().getRawQuery().substring("id=".length()));
                        sendText(h, gson.toJson(taskManager.getEpicByID(id)));
                    }
                    break;

                case "POST":
                    Epic epic = gson.fromJson(readText(h), Epic.class);
                    if (taskManager.getEpics().containsKey(epic.getIdNumber())) {
                        taskManager.updateEpics(epic);
                    } else {
                        taskManager.addEpics(epic);
                    }

                    h.sendResponseHeaders(200, 0);
                    break;

                case "DELETE":
                    if (h.getRequestURI().getRawQuery() == null) {
                        taskManager.deleteAllEpics();
                        h.sendResponseHeaders(200, 0);
                    } else {
                        int id = Integer.parseInt(h.getRequestURI().getRawQuery().substring("id=".length()));
                        taskManager.deleteEpicByID(id);
                        h.sendResponseHeaders(200, 0);
                    }
                    break;
            }
        }
    }

    private void handleSubtasksRequest(HttpExchange h) throws IOException {
        try (h) {
            switch (h.getRequestMethod()) {
                case "GET":
                    if (h.getRequestURI().getRawQuery() == null) {
                        sendText(h, gson.toJson(taskManager.getSubtasks()));
                    } else {
                        int id = Integer.parseInt(h.getRequestURI().getRawQuery().substring("id=".length()));
                        sendText(h, gson.toJson(taskManager.getSubtaskByID(id)));
                    }
                    break;

                case "POST":
                    Subtask subtask = gson.fromJson(readText(h), Subtask.class);
                    if (taskManager.getSubtasks().containsKey(subtask.getIdNumber())) {
                        taskManager.updateSubtasks(subtask);
                    } else {
                        taskManager.addSubtasks(subtask);
                    }
                    h.sendResponseHeaders(200, 0);
                    break;

                case "DELETE":
                    if (h.getRequestURI().getRawQuery() == null) {
                        taskManager.deleteAllSubtasks();
                        h.sendResponseHeaders(200, 0);
                    } else {
                        int id = Integer.parseInt(h.getRequestURI().getRawQuery().substring("id=".length()));
                        taskManager.deleteSubtaskByID(id);
                        h.sendResponseHeaders(200, 0);
                    }
                    break;
            }
        }
    }

    private void handleTasksRequest(HttpExchange h) throws IOException {
        try (h) {
            switch (h.getRequestMethod()) {
                case "GET":
                    if (h.getRequestURI().getRawQuery() == null) {
                        sendText(h, gson.toJson(taskManager.getTasks()));
                    } else {
                        int id = Integer.parseInt(h.getRequestURI().getRawQuery().substring("id=".length()));
                        sendText(h, gson.toJson(taskManager.getTaskByID(id)));
                    }
                    break;

                case "POST":
                    Task task = gson.fromJson(readText(h), Task.class);
                    if (taskManager.getTasks().containsKey(task.getIdNumber())) {
                        taskManager.updateTasks(task);
                    } else {
                        taskManager.addTasks(task);
                    }

                    h.sendResponseHeaders(200, 0);
                    break;

                case "DELETE":
                    if (h.getRequestURI().getRawQuery() == null) {
                        taskManager.deleteAllTasks();
                        h.sendResponseHeaders(200, 0);
                    } else {
                        int id = Integer.parseInt(h.getRequestURI().getRawQuery().substring("id=".length()));
                        taskManager.deleteTaskByID(id);
                        h.sendResponseHeaders(200, 0);
                    }
                    break;
            }
        }
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private Gson buildGson() {
        return new GsonBuilder()
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
}
