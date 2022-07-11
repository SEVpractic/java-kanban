package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.HashMap;

public interface TaskManager {

    int generateIdNumber();

    // Методы по коллекции задач
    HashMap<Integer, Task> getTasks();

    void deliteAllTasks();

    Task getTaskByID(int idNumber);

    void addTasks(Task task);

    void updateTasks(Task task);

    void deliteTaskByID(int idNumber);

    // Методы по коллекции эпиков
    HashMap<Integer, Epic> getEpics();

    void deliteAllEpics();

    Task getEpicByID(int idNumber);

    void addEpics(Epic epic);

    void updateEpics(Epic epic);

    void checkEpicsStatus (int epicsID);

    void deliteEpicByID(int idNumber);

    HashMap<Integer, Subtask> getSubtasksByEpicsID(int idNumber);

    // методы по коллекции подзадач
    HashMap<Integer, Subtask> getSubtasks();

    void deliteAllSubtasks();

    Subtask getSubtaskByID(int idNumber);

    void addSubtasks(Subtask subtask);

    void updateSubtasks(Subtask subtask);

    void deliteSubtaskByID(int idNumber);

    void addSubtaskToEpic(Subtask subtask);

    void removeSubtaskFromEpic(Subtask subtask);
}
