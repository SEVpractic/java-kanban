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

        System.out.println("первая итерация задач:");

        Task task1 = new Task("Первая задача", "Описание первой задачи",
                taskManager.generateIdNumber(), Status.NEW);
        taskManager.addTasks(task1);

        Task task2 = new Task("Вторая задача", "Описание второй задачи",
                taskManager.generateIdNumber(), Status.NEW);
        taskManager.addTasks(task2);

        Epic epic1 = new Epic("Первый эпик", "Описание первого эпика",
                taskManager.generateIdNumber(), Status.NEW, new ArrayList<>());
        taskManager.addEpics(epic1);

        Subtask subtask11 = new Subtask("Первая подзадача первого эпика",
                "Описание первой подзадачи первого эпика", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask11);

        Subtask subtask12 = new Subtask("Вторая подзадача первого эпика",
                "Описание второй подзадачи первого эпика", taskManager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        taskManager.addSubtasks(subtask12);

        Epic epic2 = new Epic("Второй эпик", "описание второго эпика",
                taskManager.generateIdNumber(), Status.NEW, new ArrayList<>());
        taskManager.addEpics(epic2);

        Subtask subtask21 = new Subtask("Первая подзадача второго эпика",
                "Описание первой подзадачи второго эпика", taskManager.generateIdNumber(),
                Status.NEW, epic2.getIdNumber());
        taskManager.addSubtasks(subtask21);

        System.out.println(taskManager.getTasks().toString());
        System.out.println(taskManager.getEpics().toString());
        System.out.println(taskManager.getSubtasks().toString() + "\n");

        System.out.println("запрос ID: c 1 по 7");
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(2);
        taskManager.getEpicByID(3);
        taskManager.getSubtaskByID(4);
        taskManager.getSubtaskByID(5);
        taskManager.getEpicByID(6);
        taskManager.getSubtaskByID(7);
        System.out.println("история 10 последних запросов:");
        System.out.println(taskManager.getHistory().getHistory().toString() + "\n");

        System.out.println("вторая итерация задач:");
        Task task1v2 = new Task("Первая задача", "Описание первой задачи c первым изменением",
                task1.getIdNumber(), Status.DONE);
        taskManager.updateTasks(task1v2);

        Subtask subtask11v2 = new Subtask("Первая подзадача первого эпика",
                "Описание первой подзадачи первого эпика с первым изменением",
                subtask11.getIdNumber(), Status.IN_PROGRESS, epic1.getIdNumber());
        taskManager.updateSubtasks(subtask11v2);

        Subtask subtask21v2 = new Subtask("Первая подзадача второго эпика",
                "Описание первой подзадачи второго эпика с изменением",
                subtask21.getIdNumber(), Status.DONE, epic2.getIdNumber());
        taskManager.updateSubtasks(subtask21v2);

        System.out.println(taskManager.getTasks().toString());
        System.out.println(taskManager.getEpics().toString());
        System.out.println(taskManager.getSubtasks().toString() + "\n");

        System.out.println("третья итерация задач:");
        taskManager.deliteTaskByID(task1v2.getIdNumber());
        taskManager.deliteEpicByID(epic2.getIdNumber());

        System.out.println(taskManager.getTasks().toString());
        System.out.println(taskManager.getEpics().toString());
        System.out.println(taskManager.getSubtasks().toString() + "\n");

        System.out.println("запрос ID: c 1 по 7");
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(2);
        taskManager.getEpicByID(3);
        taskManager.getSubtaskByID(4);
        taskManager.getSubtaskByID(5);
        taskManager.getEpicByID(6);
        taskManager.getSubtaskByID(7);
        System.out.println("история 10 последних запросов:");
        System.out.println(taskManager.getHistory().getHistory().toString());
    }
}
