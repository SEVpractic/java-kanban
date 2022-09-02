package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import static java.util.Comparator.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, Subtask> subtasks;
    protected final HistoryManager historyManager;
    protected int idNumberReserv;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
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

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        TreeSet<Task> prioritizedTasks =
                new TreeSet<>(comparing(Task::getStartTime, nullsLast(naturalOrder()))
                        .thenComparing(Task::getIdNumber));

        prioritizedTasks.addAll(tasks.values());
        prioritizedTasks.addAll(subtasks.values());

        return prioritizedTasks;
    }

    @Override
    public boolean isTimeValid(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }

        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (prioritizedTask.getStartTime() != null && prioritizedTask.getDuration() != null) {
                if ((task.getStartTime().isAfter(prioritizedTask.getStartTime())
                        && task.getStartTime().isBefore(prioritizedTask.getEndTime()))
                        || (task.getEndTime().isAfter(prioritizedTask.getStartTime())
                        && task.getEndTime().isBefore(prioritizedTask.getEndTime()))) {
                    return false;
                }
            }
        }

        return true;
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
        if ((task.getName() != null) && (task.getDescription() != null) && isTimeValid(task)) {
                tasks.put(task.getIdNumber(), task);
        }
    }

    @Override
    public void updateTasks(Task task) {
        if ((task.getName() != null) && (task.getDescription() != null)) {
            if (task.getStartTime().isEqual(tasks.get(task.getIdNumber()).getStartTime())) {
                tasks.put(task.getIdNumber(), task);
            } else if (isTimeValid(task)) {
                tasks.put(task.getIdNumber(), task);
            }
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
    public void checkEpicsStatus(int epicsID) {
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
            Epic newEpic = new Epic(
                    epics.get(epicsID).getName(),
                    epics.get(epicsID).getDescription(),
                    epicsID,
                    status,
                    epics.get(epicsID).getDuration(),
                    epics.get(epicsID).getStartTime(),
                    includeSubtasksIDs);
            updateEpics(newEpic);
        }
    }

    @Override
    public void checkEpicsStartTime(int epicsID) {
        ArrayList<Integer> includeSubtasksIDs = epics.get(epicsID).getIncludeSubtasksIDs();
        LocalDateTime startTime = LocalDateTime.MAX;

        for (Integer includeSubtasksID : includeSubtasksIDs) {
            if (subtasks.get(includeSubtasksID).getStartTime() != null) {
                if (startTime.isAfter(subtasks.get(includeSubtasksID).getStartTime())) {
                    startTime = subtasks.get(includeSubtasksID).getStartTime();
                }
            }
        }

        Epic newEpic = new Epic(
                epics.get(epicsID).getName(),
                epics.get(epicsID).getDescription(),
                epicsID,
                epics.get(epicsID).getStatus(),
                epics.get(epicsID).getDuration(),
                startTime,
                includeSubtasksIDs);
        updateEpics(newEpic);
    }

    @Override
    public void checkEpicsDuration(int epicsID) {
        ArrayList<Integer> includeSubtasksIDs = epics.get(epicsID).getIncludeSubtasksIDs();;
        Duration duration = Duration.ZERO;

        for (Integer includeSubtasksID : includeSubtasksIDs) {
            if (subtasks.get(includeSubtasksID).getDuration() != null) {
                duration = duration.plus(subtasks.get(includeSubtasksID).getDuration());
            }
        }

        Epic newEpic = new Epic(
                epics.get(epicsID).getName(),
                epics.get(epicsID).getDescription(),
                epicsID,
                epics.get(epicsID).getStatus(),
                duration,
                epics.get(epicsID).getStartTime(),
                includeSubtasksIDs);
        updateEpics(newEpic);
    }

    @Override
    public void checkEpicsEndTime(int epicsID) {
        ArrayList<Integer> includeSubtasksIDs = epics.get(epicsID).getIncludeSubtasksIDs();
        LocalDateTime endTime = LocalDateTime.MIN;

        for (Integer includeSubtasksID : includeSubtasksIDs) {
            if (subtasks.get(includeSubtasksID).getEndTime() != null) {
                if (endTime.isBefore(subtasks.get(includeSubtasksID).getEndTime())) {
                    endTime = subtasks.get(includeSubtasksID).getEndTime();
                }
            }
        }

        Epic newEpic = new Epic(
                epics.get(epicsID).getName(),
                epics.get(epicsID).getDescription(),
                epicsID,
                epics.get(epicsID).getStatus(),
                epics.get(epicsID).getDuration(),
                epics.get(epicsID).getStartTime(),
                includeSubtasksIDs,
                endTime);
        updateEpics(newEpic);
    }

    @Override
    public void deliteEpicByID(int idNumber) {
        ArrayList<Integer> subtasksToDelite = new ArrayList<>(epics.get(idNumber).getIncludeSubtasksIDs());

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

        if ((subtask.getName() != null) && (subtask.getDescription() != null) && isTimeValid(subtask)) {
            subtasks.put(subtask.getIdNumber(), subtask);
        }
        addSubtaskToEpic(subtask);
        checkEpicsStatus(subtask.getEpicsID());
        checkEpicsDuration(subtask.getEpicsID());
        checkEpicsStartTime(subtask.getEpicsID());
        checkEpicsEndTime(subtask.getEpicsID());
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        if ((subtask.getName() != null) && (subtask.getDescription() != null)) {
            if (subtask.getStartTime().isEqual(subtasks.get(subtask.getIdNumber()).getStartTime())) {
                subtasks.put(subtask.getIdNumber(), subtask);
            } else if (isTimeValid(subtask)) {
                subtasks.put(subtask.getIdNumber(), subtask);
            }
        }

        checkEpicsStatus(subtask.getEpicsID());
        checkEpicsDuration(subtask.getEpicsID());
        checkEpicsStartTime(subtask.getEpicsID());
        checkEpicsEndTime(subtask.getEpicsID());
    }

    @Override
    public void deliteSubtaskByID(int idNumber) {
        int epicsID = subtasks.get(idNumber).getEpicsID();

        removeSubtaskFromEpic(subtasks.get(idNumber));
        subtasks.remove(idNumber);
        historyManager.remove(idNumber);
        checkEpicsStatus(epicsID);
        checkEpicsDuration(epicsID);
        checkEpicsStartTime(epicsID);
        checkEpicsEndTime(epicsID);
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask) {
        ArrayList<Integer> includeSubtasksIDs;
        int epicsID = subtask.getEpicsID();

        includeSubtasksIDs = epics.get(epicsID).getIncludeSubtasksIDs();
        includeSubtasksIDs.add(subtask.getIdNumber());
        Epic epic = new Epic(
                epics.get(epicsID).getName(),
                epics.get(epicsID).getDescription(),
                epicsID,
                epics.get(epicsID).getStatus(),
                epics.get(epicsID).getDuration(),
                epics.get(epicsID).getStartTime(),
                includeSubtasksIDs);
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
        Epic epic = new Epic(
                epics.get(epicsID).getName(),
                epics.get(epicsID).getDescription(),
                epicsID,
                epics.get(epicsID).getStatus(),
                epics.get(epicsID).getDuration(),
                epics.get(epicsID).getStartTime(),
                includeSubtasksIDs);
        epics.put(epicsID, epic);
    }
}