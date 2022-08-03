package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;
    private int idNumberReserv;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        this.idNumberReserv = 0;
    }

    @Override
    public int generateIdNumber() {
        idNumberReserv++;
        return idNumberReserv;
    }

    @Override
    public HistoryManager getHistory() {
        return historyManager;
    }

    // Методы по коллекции задач
    @Override
    public HashMap<Integer, Task> getTasks() {
        historyManager.add(tasks.values().toArray(new Task[0]));
        return tasks;
    }

    @Override
    public void deliteAllTasks() {
        historyManager.remove(tasks.keySet().toArray(new Integer[0]));
        tasks.clear();
    }

    @Override
    public Task getTaskByID(int idNumber) {
        historyManager.add(tasks.get(idNumber));
        return tasks.get(idNumber);
    }

    @Override
    public void addTasks(Task task) {
        if ((task.getName() != null) && (task.getDescription() != null)) {
            tasks.put(task.getIdNumber(), task);
        }
    }

    @Override
    public void updateTasks(Task task) {
        if ((task.getName() != null) && (task.getDescription() != null)) {
            tasks.put(task.getIdNumber(), task);
        }
    }

    @Override
    public void deliteTaskByID(int idNumber) {
        tasks.remove(idNumber);
        historyManager.remove(idNumber);
    }

    // Методы по коллекции эпиков
    @Override
    public HashMap<Integer, Epic> getEpics() {
        historyManager.add(epics.values().toArray(new Task[0]));
        return epics;
    }

    @Override
    public void deliteAllEpics() {
        historyManager.remove(epics.keySet().toArray(new Integer[0]));
        epics.clear();
        deliteAllSubtasks();
    }

    @Override
    public Epic getEpicByID(int idNumber) {
        historyManager.add(epics.get(idNumber));
        return epics.get(idNumber);
    }

    @Override
    public void addEpics(Epic epic) {
        if ((epic.getName() != null) && (epic.getDescription() != null)) {
            epics.put(epic.getIdNumber(), epic);
        }
    }

    @Override
    public void updateEpics(Epic epic) {
        if ((epic.getName() != null) && (epic.getDescription() != null)) {
            epics.put(epic.getIdNumber(), epic);
        }
    }

    @Override
    public void checkEpicsStatus (int epicsID) {
        ArrayList<Integer> includeSubtasksIDs;
        int amountOfDone = 0;
        Status status;

        status = epics.get(epicsID).getStatus();
        includeSubtasksIDs = epics.get(epicsID).getIncludeSubtasksIDs();
        for (Integer subtasksID : includeSubtasksIDs) {
            switch (subtasks.get(subtasksID).getStatus()) {
                case IN_PROGRESS:
                    status = Status.IN_PROGRESS;
                    break;
                case DONE:
                    status = Status.IN_PROGRESS;
                    amountOfDone++;
                    break;
            }
        }
        if (amountOfDone == includeSubtasksIDs.size()) {
            status = Status.DONE;
        }
        if (status != epics.get(epicsID).getStatus()) {
            Epic newEpic = new Epic(epics.get(epicsID).getName(), epics.get(epicsID).getDescription(),
                    epicsID, status, includeSubtasksIDs);
            updateEpics(newEpic);
        }
    }

    @Override
    public void deliteEpicByID(int idNumber) {
        ArrayList<Integer> includeSubtasksIDs;
        ArrayList<Integer> subtasksToDelite = new ArrayList<>();

        includeSubtasksIDs = epics.get(idNumber).getIncludeSubtasksIDs();
        for (Integer id : includeSubtasksIDs) {
            subtasksToDelite.add(id);
        }
        for (Integer id : subtasksToDelite) {
            deliteSubtaskByID(id);
        }
        epics.remove(idNumber);
        historyManager.remove(idNumber);
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasksByEpicsID(int idNumber) {
        ArrayList<Integer> includeSubtasksIDs;
        HashMap<Integer, Subtask> includeSubtasks = new HashMap<>();

        includeSubtasksIDs = epics.get(idNumber).getIncludeSubtasksIDs();
        for (Integer id : includeSubtasksIDs) {
            includeSubtasks.put(id, getSubtaskByID(id));
        }
        return includeSubtasks;
    }

    // методы по коллекции подзадач
    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        historyManager.add(subtasks.values().toArray(new Task[0]));
        return subtasks;
    }

    @Override
    public void deliteAllSubtasks() {
        historyManager.remove(subtasks.keySet().toArray(new Integer[0]));
        subtasks.clear();
    }

    @Override
    public Subtask getSubtaskByID(int idNumber) {
        historyManager.add(subtasks.get(idNumber));
        return subtasks.get(idNumber);
    }

    @Override
    public void addSubtasks(Subtask subtask) {

        if ((subtask.getName() != null) && (subtask.getDescription() != null)) {
            subtasks.put(subtask.getIdNumber(), subtask);
        }
        addSubtaskToEpic(subtask);
        checkEpicsStatus(subtask.getEpicsID());
    }

    @Override
    public void updateSubtasks(Subtask subtask) {

        if ((subtask.getName() != null) && (subtask.getDescription() != null)){
            subtasks.put(subtask.getIdNumber(), subtask);
        }
        checkEpicsStatus(subtask.getEpicsID());
    }

    @Override
    public void deliteSubtaskByID(int idNumber) {
        int epicsID = subtasks.get(idNumber).getEpicsID();

        removeSubtaskFromEpic(subtasks.get(idNumber));
        subtasks.remove(idNumber);
        historyManager.remove(idNumber);
        checkEpicsStatus(epicsID);
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask) {
        ArrayList<Integer> includeSubtasksIDs;
        int epicsID = subtask.getEpicsID();

        includeSubtasksIDs = epics.get(epicsID).getIncludeSubtasksIDs();
        includeSubtasksIDs.add(subtask.getIdNumber());
        Epic epic = new Epic(epics.get(epicsID).getName(), epics.get(epicsID).getDescription(),
                epicsID, epics.get(epicsID).getStatus(), includeSubtasksIDs);
        epics.put(epicsID, epic);
    }

    @Override
    public void removeSubtaskFromEpic(Subtask subtask) {
        ArrayList<Integer> includeSubtasksIDs;
        int epicsID = subtask.getEpicsID();
        int index;

        includeSubtasksIDs = epics.get(epicsID).getIncludeSubtasksIDs();
        index = includeSubtasksIDs.indexOf(subtask.getIdNumber());
        includeSubtasksIDs.remove(index);
        Epic epic = new Epic(epics.get(epicsID).getName(), epics.get(epicsID).getDescription(),
                epicsID, epics.get(epicsID).getStatus(), includeSubtasksIDs);
        epics.put(epicsID, epic);
    }
}