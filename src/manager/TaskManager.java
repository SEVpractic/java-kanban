package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.HashMap;
import java.util.TreeSet;

public interface TaskManager {

    public int generateIdNumber();

    public HistoryManager getHistory();

    public TreeSet<Task> getPrioritizedTasks();

    public void addPrioritizedTasks(Task task);

    public void updatePrioritizedTasks(Task task);

    public void deletePrioritizedTasks(Task task);

    public void deletePrioritizedTasks(Task[] tasks);

    public boolean isTimeValid(Task task);

    // Методы по коллекции задач
    public HashMap<Integer, Task> getTasks();

    public void deleteAllTasks();

    public Task getTaskByID(int idNumber);

    public void addTasks(Task task);

    public void updateTasks(Task task);

    public void deleteTaskByID(int idNumber);

    // Методы по коллекции эпиков
    public HashMap<Integer, Epic> getEpics();

    public void deleteAllEpics();

    public Epic getEpicByID(int idNumber);

    public void addEpics(Epic epic);

    public void updateEpics(Epic epic);

    public void checkEpicsStatus(int epicsID);

    public void checkEpicsStartTime(int epicsID);

    public void checkEpicsDuration(int epicsID);

    public void checkEpicsEndTime(int epicsID);

    public void deleteEpicByID(int idNumber);

    public HashMap<Integer, Subtask> getSubtasksByEpicsID(int idNumber);

    // методы по коллекции подзадач
    public HashMap<Integer, Subtask> getSubtasks();

    public void deleteAllSubtasks();

    public Subtask getSubtaskByID(int idNumber);

    public void addSubtasks(Subtask subtask);

    public void updateSubtasks(Subtask subtask);

    public void deleteSubtaskByID(int idNumber);

    public void addSubtaskToEpic(Subtask subtask);

    public void removeSubtaskFromEpic(Subtask subtask);
}
