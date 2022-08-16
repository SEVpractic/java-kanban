package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("src/test.csv");
        TaskManager taskManager = Managers.getFileBackedManager(file);

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

        taskManager.getTasks();
        taskManager.getEpicByID(3);
        taskManager.getSubtasksByEpicsID(3);
        taskManager.getEpicByID(7);

        TaskManager taskManager1 = FileBackedTasksManager.loadFromFile(file);

        String expectedManager = taskManager.toString();
        String actualManager = taskManager1.toString();

        if (!expectedManager.equals(actualManager)) {
            throw new AssertionError("Метод работает неверно!");
        }
    }

    public static TaskManager loadFromFile(File file) {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()){
                lines.add(bufferedReader.readLine());
            }
        } catch (IOException e) {
            System.out.println("Ошибка загрузки, файл не существует");
        }

        tasksManager.fillTaskManager(lines);

        return tasksManager;
    }

    private void fillTaskManager(ArrayList<String> lines) {
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isEmpty() && (lines.size() > (i + 1))) {
                if (!lines.get(i + 1).isEmpty()) {
                    fillHistoryManager(lines.get(i + 1));
                }
                break;
            } else {
                fromString(lines.get(i));
            }
        }
    }

    private void fillHistoryManager(String line) {
        String[] IDs = line.split(",");

        try {
            for (String id : IDs) {
                if (tasks.containsKey(Integer.parseInt(id))) {
                    getTaskByID(Integer.parseInt(id));
                } else if (epics.containsKey(Integer.parseInt(id))) {
                    getEpicByID(Integer.parseInt(id));
                } else if (subtasks.containsKey(Integer.parseInt(id))) {
                    getSubtaskByID(Integer.parseInt(id));
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка загрузки, не удалось создать историю");
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

            if (type.equals(TasksType.TASK)) {
                addTasks(new Task(name, description, idNumber, status));
            } else if (type.equals(TasksType.EPIC)) {
                addEpics(new Epic(name, description, idNumber, status, new ArrayList<>()));
            } else if (type.equals(TasksType.SUBTASK)){
                if (line.length < 6) {
                    throw new IllegalArgumentException();
                }
                if (epics.containsKey(Integer.parseInt(line[5]))) {
                    epicsID = Integer.parseInt(line[5]);
                } else {
                    throw new IllegalArgumentException();
                }
                addSubtasks(new Subtask(name, description, idNumber, status, epicsID));
            }

            if (idNumberReserv < idNumber) {
                idNumberReserv = idNumber;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка загрузки, не удалось создать объект");
        }
    }

    private void save() {

        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            if (!file.exists()) {
                throw new ManagerSaveException("Ошибка сохранения, файл не существует");
            }
            bufferedWriter.write(toString());
        } catch (ManagerSaveException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("id,type,name,status,description,epic\n%s%s%s\n%s",
                tasksToString(), epicsToString(), subtasksToString(), historyManager);
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
        save();
        return tasks;
    }

    @Override
    public void deliteAllTasks() {
        super.deliteAllTasks();
        save();
    }

    @Override
    public Task getTaskByID(int idNumber) {
        historyManager.add(tasks.get(idNumber));
        save();
        return tasks.get(idNumber);
    }

    @Override
    public void addTasks(Task task) {
        super.addTasks(task);
        save();
    }

    @Override
    public void updateTasks(Task task) {
        super.updateTasks(task);
        save();
    }

    @Override
    public void deliteTaskByID(int idNumber) {
        super.deliteTaskByID(idNumber);
        save();
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        historyManager.add(epics.values().toArray(new Task[0]));
        save();
        return epics;
    }

    @Override
    public void deliteAllEpics() {
        super.deliteAllEpics();
        save();
    }

    @Override
    public Epic getEpicByID(int idNumber) {
        historyManager.add(epics.get(idNumber));
        save();
        return epics.get(idNumber);
    }

    @Override
    public void addEpics(Epic epic) {
        super.addEpics(epic);
        save();
    }

    @Override
    public void deliteEpicByID(int idNumber) {
        super.deliteEpicByID(idNumber);
        save();
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        historyManager.add(subtasks.values().toArray(new Task[0]));
        save();
        return subtasks;
    }

    @Override
    public void deliteAllSubtasks() {
        super.deliteAllSubtasks();
        save();
    }

    @Override
    public Subtask getSubtaskByID(int idNumber) {
        historyManager.add(subtasks.get(idNumber));
        save();
        return subtasks.get(idNumber);
    }

    @Override
    public void addSubtasks(Subtask subtask) {
        super.addSubtasks(subtask);
        save();
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        super.updateSubtasks(subtask);
        save();
    }

    @Override
    public void deliteSubtaskByID(int idNumber) {
        super.deliteSubtaskByID(idNumber);
        save();
    }
}
