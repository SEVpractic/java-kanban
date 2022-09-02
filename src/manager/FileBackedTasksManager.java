package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static TaskManager loadFromFile(File file) {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        List<String> lines = new ArrayList<>();

        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            lines = bufferedReader.lines().filter(str -> str != null).toList();
        } catch (IOException e) {
            System.out.println("Ошибка загрузки, файл не существует");
        }

        tasksManager.fillTaskManager(lines);
        tasksManager.fillHistory(InMemoryHistoryManager.historyFromString(lines.get(lines.size() - 1)));

        return tasksManager;
    }

    private void fillTaskManager(List<String> lines) {
        lines.stream().skip(1).limit(lines.size() - 2).forEach(this::fromString);
    }

    private void fillHistory(List<Integer> history) {
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

    private void fromString(String value) {
        String[] line = value.split(",");

        try {
            if (line.length < 5) {
                throw new IllegalArgumentException();
            }

            final String name = line[2];
            final String description = line[4];
            final int idNumber = Integer.parseInt(line[0]);
            final Status status = Status.valueOf(line[3]);
            final TasksType type = TasksType.valueOf(line[1]);
            final int epicsID;
            final Duration duration = (line[5].equals("null")) ? null : Duration.parse(line[5]);
            final LocalDateTime startTime = (line[5].equals("null")) ? null : LocalDateTime.parse(line[6]);

            if (type.equals(TasksType.TASK)) {
                addTasks(new Task(name, description, idNumber, status, duration, startTime));
            } else if (type.equals(TasksType.EPIC)) {
                addEpics(new Epic(name, description, idNumber, status, duration, startTime, new ArrayList<>()));
            } else if (type.equals(TasksType.SUBTASK)){
                if (line.length < 6) {
                    throw new IllegalArgumentException();
                } else if (epics.containsKey(Integer.parseInt(line[7]))) {
                    epicsID = Integer.parseInt(line[7]);
                } else {
                    throw new IllegalArgumentException();
                }
                addSubtasks(new Subtask(name, description, idNumber, status, duration, startTime, epicsID));
            }

            if (idNumberReserv < idNumber) {
                idNumberReserv = idNumber;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка загрузки, не удалось создать объект");
        }
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
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
            System.out.println(e.getMessage());
        }

        return tasks;
    }

    @Override
    public void deliteAllTasks() {
        super.deliteAllTasks();

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task getTaskByID(int idNumber) {
        historyManager.add(tasks.get(idNumber));

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }

        return tasks.get(idNumber);
    }

    @Override
    public void addTasks(Task task) {
        super.addTasks(task);

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTasks(Task task) {
        super.updateTasks(task);

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deliteTaskByID(int idNumber) {
        super.deliteTaskByID(idNumber);

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        historyManager.add(epics.values().toArray(new Task[0]));

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }

        return epics;
    }

    @Override
    public void deliteAllEpics() {
        super.deliteAllEpics();

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Epic getEpicByID(int idNumber) {
        historyManager.add(epics.get(idNumber));

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }

        return epics.get(idNumber);
    }

    @Override
    public void addEpics(Epic epic) {
        super.addEpics(epic);

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deliteEpicByID(int idNumber) {
        super.deliteEpicByID(idNumber);

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        historyManager.add(subtasks.values().toArray(new Task[0]));

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }

        return subtasks;
    }

    @Override
    public void deliteAllSubtasks() {
        super.deliteAllSubtasks();

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Subtask getSubtaskByID(int idNumber) {
        historyManager.add(subtasks.get(idNumber));

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }

        return subtasks.get(idNumber);
    }

    @Override
    public void addSubtasks(Subtask subtask) {
        super.addSubtasks(subtask);

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        super.updateSubtasks(subtask);

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deliteSubtaskByID(int idNumber) {
        super.deliteSubtaskByID(idNumber);

        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }
}
