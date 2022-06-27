import java.util.HashMap;

public class Manager {
    private int idNumberReserv;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Object> epics; // TODO поменяй тип на соответствующий классу
    private HashMap<Integer, Object> subtasks; // TODO поменяй тип на соответствующий классу

    public int generateIdNumber(){
        idNumberReserv++;
        return idNumberReserv;
    }

    public Manager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        idNumberReserv = 0;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Task task){
        tasks.put(task.getIdNumber(), task);
    }

    public void deliteAllTasks(){
        tasks.clear();
    }

    public Task getTaskByID(int idNumber){
        return tasks.get(idNumber);
    }

    public void deliteTaskByID(int idNumber){
        tasks.remove(idNumber);
    }
}

/*  Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
    Методы для каждого из типа задач(Задача/Эпик/Подзадача):
        Получение списка всех задач.
        Удаление всех задач.
        Получение по идентификатору.
        Создание. Сам объект должен передаваться в качестве параметра.
        Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        Удаление по идентификатору.*/
