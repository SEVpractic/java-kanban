package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            String line;
            do {
                line = bufferedReader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            } while (line != null);
        } catch (IOException e) {
            System.out.println("Ошибка загрузки, файл не существует");
        }

        tasksManager.fillTaskManager(lines);
        tasksManager.fillHistory(InMemoryHistoryManager.historyFromString(lines.get(lines.size() - 1)));

        return tasksManager;
    }

    private void fillTaskManager(ArrayList<String> lines) {
        for (int i = 1; i < (lines.size() - 2); i++) {
            fromString(lines.get(i));
        }
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
        return String.format("id,type,name,status,description,epic\n%s%s%s\n%s",
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
