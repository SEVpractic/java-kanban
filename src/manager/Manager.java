package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int idNumberReserv;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        idNumberReserv = 0;
    }

    public int generateIdNumber() {
        idNumberReserv++;
        return idNumberReserv;
    }

    // Методы по коллекции задач
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void deliteAllTasks() {
        tasks.clear();
    }

    public Task getTaskByID(int idNumber) {
        return tasks.get(idNumber);
    }

    public void addTasks(Task task) {
        if ((task.getName() != null) && (task.getDescription() != null)) {
            tasks.put(task.getIdNumber(), task);
        }
    }

    public void updateTasks(Task task) {
        if ((task.getName() != null) && (task.getDescription() != null)) {
            tasks.put(task.getIdNumber(), task);
        }
    }

    public void deliteTaskByID(int idNumber) {
        tasks.remove(idNumber);
    }

    // Методы по коллекции эпиков
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deliteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Task getEpicByID(int idNumber) {
        return epics.get(idNumber);
    }

    public void addEpics(Epic epic) {
        if ((epic.getName() != null) && (epic.getDescription() != null)) {
            epics.put(epic.getIdNumber(), epic);
        }
    }

    public void updateEpics(Epic epic) {
        if ((epic.getName() != null) && (epic.getDescription() != null)) {
            epics.put(epic.getIdNumber(), epic);
        }
    }

    public void checkEpicsStatus (int epicsID) {
        ArrayList<Integer> includeSubtasksIDs;
        int amountOfDone = 0;
        Status status;

        status = epics.get(epicsID).getStatus();
        includeSubtasksIDs = epics.get(epicsID).getIncludeSubtasksIDs();
        for (Integer subtasksID : includeSubtasksIDs) {
            switch (getSubtaskByID(subtasksID).getStatus()) {
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
            Epic newEpic = new Epic(getEpicByID(epicsID).getName(), getEpicByID(epicsID).getDescription(),
                    epicsID, status, includeSubtasksIDs);
            updateEpics(newEpic);
        }
    }

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
    }

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
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void deliteAllSubtasks() {
        subtasks.clear();
    }

    public Subtask getSubtaskByID(int idNumber) {
        return subtasks.get(idNumber);
    }

    public void addSubtasks(Subtask subtask) {
        int epicID;

        if ((subtask.getName() != null) && (subtask.getDescription() != null)) {
            subtasks.put(subtask.getIdNumber(), subtask);
        }
        epicID = subtask.getEpicsID();
        addSubtaskToEpic(subtask);
        checkEpicsStatus(epicID);
    }

    public void updateSubtasks(Subtask subtask) {

        if ((subtask.getName() != null) && (subtask.getDescription() != null)){
            subtasks.put(subtask.getIdNumber(), subtask);
        }
        checkEpicsStatus(subtask.getEpicsID());
    }

    public void deliteSubtaskByID(int idNumber) {
        int epicsID = subtasks.get(idNumber).getEpicsID();

        removeSubtaskFromEpic(subtasks.get(idNumber));
        subtasks.remove(idNumber);
        checkEpicsStatus(epicsID);
    }

    public void addSubtaskToEpic(Subtask subtask) {
        ArrayList<Integer> includeSubtasksIDs;
        int epicsID = subtask.getEpicsID();

        includeSubtasksIDs = epics.get(epicsID).getIncludeSubtasksIDs();
        includeSubtasksIDs.add(subtask.getIdNumber());
        Epic epic = new Epic(epics.get(epicsID).getName(), epics.get(epicsID).getDescription(),
                epicsID, epics.get(epicsID).getStatus(), includeSubtasksIDs);
        epics.put(epicsID, epic);
    }

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