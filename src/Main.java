import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        System.out.println("создаем задачи");

        Task task1 = new Task("Первая задача", "Описание",
                taskManager.generateIdNumber(), Status.NEW);
        taskManager.addTasks(task1);

        Task task2 = new Task("Вторая задача", "Описание",
                taskManager.generateIdNumber(), Status.NEW);
        taskManager.addTasks(task2);

        Epic epic1 = new Epic("Первый эпик", "Описание",
                taskManager.generateIdNumber(), Status.NEW, new ArrayList<>());
        taskManager.addEpics(epic1);

        Subtask subtask11 = new Subtask("Первая подзадача первого эпика",
                "Описание", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask11);

        Subtask subtask12 = new Subtask("Вторая подзадача первого эпика",
                "Описание", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask12);

        Subtask subtask13 = new Subtask("Третья подзадача первого эпика",
                "Описание", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask13);

        Epic epic2 = new Epic("Второй эпик", "описание",
                taskManager.generateIdNumber(), Status.NEW, new ArrayList<>());
        taskManager.addEpics(epic2);

        System.out.println("запрашиваем задачи");
        System.out.println(taskManager.getTaskByID(task1.getIdNumber()));
        System.out.println(taskManager.getTaskByID(task2.getIdNumber()));
        System.out.println(taskManager.getEpicByID(epic1.getIdNumber()));
        System.out.println(taskManager.getSubtasksByEpicsID(epic1.getIdNumber()));

        System.out.println("\nзапрашиваем задачи");
        System.out.println(taskManager.getTaskByID(task1.getIdNumber()));
        System.out.println(taskManager.getEpicByID(epic1.getIdNumber()));

        System.out.println("запрашиваем историю");
        System.out.println(taskManager.getHistory().getInMemoryHistory().toString());

        System.out.println("\nудаляем 1ю задачу и первый эпик");
        taskManager.deliteTaskByID(task1.getIdNumber());
        taskManager.deliteEpicByID(epic1.getIdNumber());

        System.out.println("запрашиваем историю");
        System.out.println(taskManager.getHistory().getInMemoryHistory().toString());

        System.out.println("\nзапрашиваем задачи");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());

        System.out.println("запрашиваем историю");
        System.out.println(taskManager.getHistory().getInMemoryHistory().toString());
    }
}
