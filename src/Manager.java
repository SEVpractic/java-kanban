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

    public int generateIdNumber(){
        idNumberReserv++;
        return idNumberReserv;
    }

//  Методы по коллекции задач
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void deliteAllTasks(){
        tasks.clear();
    }

    public Task getTaskByID(int idNumber){
        return tasks.get(idNumber);
    }

    public void setTasks(Task task){
        boolean notExeption;

        task.setIdNumber(generateIdNumber());
        notExeption = (task.getName() != null) && (task.getDescription() != null)
                && (task.getStatus().equals("NEW") || task.getStatus().equals("DONE")
                || task.getStatus().equals("IN_PROGRESS"));
        if (notExeption){
            tasks.put(task.getIdNumber(), task);
        }
    }

    public void deliteTaskByID(int idNumber){
        tasks.remove(idNumber);
    }

//  Методы по коллекции эпиков
    public HashMap<Integer, Epic> getEpics() {
    return epics;
    }

    public void deliteAllEpics(){
        epics.clear();
        subtasks.clear();
    }

    public Task getEpicByID(int idNumber){
        return epics.get(idNumber);
    }

    public void setEpics(Epic epic){
        boolean notExeption;

        epic.setIdNumber(generateIdNumber());
        notExeption = (epic.getName() != null) && (epic.getDescription() != null)
                && (epic.getStatus().equals("NEW") || epic.getStatus().equals("DONE")
                || epic.getStatus().equals("IN_PROGRESS"));
        if (notExeption){
            epics.put(epic.getIdNumber(), epic);
        }
    }

    public void deliteEpicByID(int idNumber){
        ArrayList<Integer> includeSubtasksIDs;

        includeSubtasksIDs = epics.get(idNumber).getIncludeSubtasksIDs();
        for (Integer id : includeSubtasksIDs) {
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

//    методы по коллекции подзадач
    public HashMap<Integer, Subtask> getSubtasks() {
    return subtasks;
}

    public void deliteAllSubtasks(){
        subtasks.clear();
    }

    public Subtask getSubtaskByID(int idNumber){
        return subtasks.get(idNumber);
    }

    public void setSubtasks(Subtask subtask){
        int epicID;
        int doneSubtasks = 0;
        boolean notExeption;

        subtask.setIdNumber(generateIdNumber());
        notExeption = (subtask.getName() != null) && (subtask.getDescription() != null)
                && (subtask.getStatus().equals("NEW") || subtask.getStatus().equals("DONE")
                || subtask.getStatus().equals("IN_PROGRESS"));
        if (notExeption){
            subtasks.put(subtask.getIdNumber(), subtask);
        }

        epicID = subtask.getEpicsID();
        epics.get(epicID).setIncludeSubtasksIDs(subtask);

        for (Integer i : epics.get(epicID).getIncludeSubtasksIDs()) {
            if (subtasks.get(i).getStatus().equals("DONE")){
                doneSubtasks++;
            }
            if (subtasks.get(i).getStatus().equals("IN_PROGRESS")
                    || subtasks.get(i).getStatus().equals("DONE")){
                epics.get(epicID).setStatus("IN_PROGRESS");
            }
        }
        if (doneSubtasks == epics.get(epicID).getIncludeSubtasksIDs().size()){
            epics.get(epicID).setStatus("DONE");
        }
    }

    public void deliteSubtaskByID(int idNumber){
        subtasks.remove(idNumber);
    }

}

/*  Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
    Методы для каждого из типа задач(Задача/Эпик/Подзадача):
        Получение списка всех задач.
        Удаление всех задач.
        Получение по идентификатору.
        Создание. Сам объект должен передаваться в качестве параметра.
        Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        Удаление по идентификатору.
    Дополнительные методы:
        Получение списка всех подзадач определённого эпика.

    Управление статусами осуществляется по следующему правилу:
            Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией
            о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
        Для эпиков:
            если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
            если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
            во всех остальных случаях статус должен быть IN_PROGRESS.*/
