package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected final String source;

    public FileBackedTasksManager(String source) {
        this.source = source;
    }

    public static TaskManager loadFromFile(String source) {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(source);
        List<String> lines;

        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(source, StandardCharsets.UTF_8))) {
            lines = bufferedReader.lines().filter(Objects::nonNull).toList();
        } catch (IOException e) {
            throw new RuntimeException("Файл для восстановления отсутствует или не читается");
        }

        tasksManager.fillTaskManager(lines);
        tasksManager.fillHistory(InMemoryHistoryManager.historyFromString(lines.get(lines.size() - 1)));

        return tasksManager;
    }

    protected void fillTaskManager(List<String> lines) {
        lines.stream().skip(1).limit(lines.size() - 3).forEach(this::fromString);
    }

    protected void fillHistory(List<Integer> history) {
        for (Integer id : history) {
            if (tasks.containsKey(id)) {
                getTaskByID(id);
            } else if (epics.containsKey(id)) {
                getEpicByID(id);
            } else if (subtasks.containsKey(id)) {
                getSubtaskByID(id);
            }
        }
    }

    protected void fromString(String value) {
        String[] line = value.split(",");
        if (line.length < 7) {
            throw new IllegalArgumentException("Ошибка загрузки, не удалось создать объект (length < 7)");
        }
        try {
            final String name = line[2];
            final String description = line[4];
            final int idNumber = Integer.parseInt(line[0]);
            final Status status = Status.valueOf(line[3]);
            final TasksType type = TasksType.valueOf(line[1]);
            final int epicsID;
            final Duration duration = Duration.parse(line[5]);
            final LocalDateTime startTime = LocalDateTime.parse(line[6]);

            if (type.equals(TasksType.TASK)) {
                addTasks(new Task(name, description, idNumber, status, duration, startTime));
            } else if (type.equals(TasksType.EPIC)) {
                addEpics(new Epic(name, description, idNumber, status, duration, startTime, new ArrayList<>(), null));
            } else if (type.equals(TasksType.SUBTASK)) {
                if (line.length < 8) {
                    throw new IllegalArgumentException();
                } else if (epics.containsKey(Integer.parseInt(line[7]))) {
                    epicsID = Integer.parseInt(line[7]);
                } else {
                    throw new IllegalArgumentException("Ошибка загрузки, не удалось создать эпик (length < 8)");
                }
                addSubtasks(new Subtask(name, description, idNumber, status, duration, startTime, epicsID));
            }

            if (idNumberReserve < idNumber) {
                idNumberReserve = idNumber;
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ошибка загрузки, не удалось создать объект, не верные входные данные");
        }
    }

    protected void save() throws ManagerSaveException {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(source, StandardCharsets.UTF_8))) {
            bufferedWriter.write(toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    @Override
    public String toString() {
        return String.format("id,type,name,status,description,duration,startTime,epic\n%s%s%s\n%s",
                tasksToString(), epicsToString(), subtasksToString(),
                InMemoryHistoryManager.historyToString(historyManager));
    }

    private String tasksToString() {
        StringBuilder resultString = new StringBuilder();

        for (Task task : tasks.values()) {
            resultString.append(task.toString());
            resultString.append("\n");
        }

        return resultString.toString();
    }

    private String epicsToString() {
        StringBuilder resultString = new StringBuilder();

        for (Task epic : epics.values()) {
            resultString.append(epic.toString());
            resultString.append("\n");
        }

        return resultString.toString();
    }

    private String subtasksToString() {
        StringBuilder resultString = new StringBuilder();

        for (Task subtask : subtasks.values()) {
            resultString.append(subtask.toString());
            resultString.append("\n");
        }

        return resultString.toString();
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        historyManager.add(tasks.values().toArray(new Task[0]));

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        return tasks;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task getTaskByID(int idNumber) {
        historyManager.add(tasks.get(idNumber));

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        return tasks.get(idNumber);
    }

    @Override
    public void addTasks(Task task) {
        super.addTasks(task);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTasks(Task task) {
        super.updateTasks(task);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTaskByID(int idNumber) {
        super.deleteTaskByID(idNumber);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        historyManager.add(epics.values().toArray(new Task[0]));

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        return epics;
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Epic getEpicByID(int idNumber) {
        historyManager.add(epics.get(idNumber));

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        return epics.get(idNumber);
    }

    @Override
    public void addEpics(Epic epic) {
        super.addEpics(epic);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteEpicByID(int idNumber) {
        super.deleteEpicByID(idNumber);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        historyManager.add(subtasks.values().toArray(new Task[0]));

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        return subtasks;
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Subtask getSubtaskByID(int idNumber) {
        historyManager.add(subtasks.get(idNumber));

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }

        return subtasks.get(idNumber);
    }

    @Override
    public void addSubtasks(Subtask subtask) {
        super.addSubtasks(subtask);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        super.updateSubtasks(subtask);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSubtaskByID(int idNumber) {
        super.deleteSubtaskByID(idNumber);

        try {
            save();
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }
}
