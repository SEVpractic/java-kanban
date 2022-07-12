import manager.Managers;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        System.out.println("первая итерация задач:");

        Task task1 = new Task("Первая задача", "Описание первой задачи",
                Managers.getDefault().generateIdNumber(), Status.NEW);
        Managers.getDefault().addTasks(task1);

        Task task2 = new Task("Вторая задача", "Описание второй задачи",
                Managers.getDefault().generateIdNumber(), Status.NEW);
        Managers.getDefault().addTasks(task2);

        Epic epic1 = new Epic("Первый эпик", "Описание первого эпика",
                Managers.getDefault().generateIdNumber(), Status.NEW, new ArrayList<>());
        Managers.getDefault().addEpics(epic1);

        Subtask subtask11 = new Subtask("Первая подзадача первого эпика",
                "Описание первой подзадачи первого эпика", Managers.getDefault().generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        Managers.getDefault().addSubtasks(subtask11);

        Subtask subtask12 = new Subtask("Вторая подзадача первого эпика",
                "Описание второй подзадачи первого эпика", Managers.getDefault().generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        Managers.getDefault().addSubtasks(subtask12);

        Epic epic2 = new Epic("Второй эпик", "описание второго эпика",
                Managers.getDefault().generateIdNumber(), Status.NEW, new ArrayList<>());
        Managers.getDefault().addEpics(epic2);

        Subtask subtask21 = new Subtask("Первая подзадача второго эпика",
                "Описание первой подзадачи второго эпика", Managers.getDefault().generateIdNumber(),
                Status.NEW, epic2.getIdNumber());
        Managers.getDefault().addSubtasks(subtask21);

        System.out.println(Managers.getDefault().getTasks().toString());
        System.out.println(Managers.getDefault().getEpics().toString());
        System.out.println(Managers.getDefault().getSubtasks().toString() + "\n");

        System.out.println("запрос ID: c 1 по 7");
        Managers.getDefault().getTaskByID(1);
        Managers.getDefault().getTaskByID(2);
        Managers.getDefault().getEpicByID(3);
        Managers.getDefault().getSubtaskByID(4);
        Managers.getDefault().getSubtaskByID(5);
        Managers.getDefault().getEpicByID(6);
        Managers.getDefault().getSubtaskByID(7);
        System.out.println("история 10 последних запросов:");
        System.out.println(Managers.getDefaultHistory().getHistory().toString() + "\n");

        System.out.println("вторая итерация задач:");
        Task task1v2 = new Task("Первая задача", "Описание первой задачи c первым изменением",
                task1.getIdNumber(), Status.DONE);
        Managers.getDefault().updateTasks(task1v2);

        Subtask subtask11v2 = new Subtask("Первая подзадача первого эпика",
                "Описание первой подзадачи первого эпика с первым изменением",
                subtask11.getIdNumber(), Status.IN_PROGRESS, epic1.getIdNumber());
        Managers.getDefault().updateSubtasks(subtask11v2);

        Subtask subtask21v2 = new Subtask("Первая подзадача второго эпика",
                "Описание первой подзадачи второго эпика с изменением",
                subtask21.getIdNumber(), Status.DONE, epic2.getIdNumber());
        Managers.getDefault().updateSubtasks(subtask21v2);

        System.out.println(Managers.getDefault().getTasks().toString());
        System.out.println(Managers.getDefault().getEpics().toString());
        System.out.println(Managers.getDefault().getSubtasks().toString() + "\n");

        System.out.println("третья итерация задач:");
        Managers.getDefault().deliteTaskByID(task1v2.getIdNumber());
        Managers.getDefault().deliteEpicByID(epic2.getIdNumber());

        System.out.println(Managers.getDefault().getTasks().toString());
        System.out.println(Managers.getDefault().getEpics().toString());
        System.out.println(Managers.getDefault().getSubtasks().toString() + "\n");

        System.out.println("запрос ID: c 1 по 7");
        Managers.getDefault().getTaskByID(1);
        Managers.getDefault().getTaskByID(2);
        Managers.getDefault().getEpicByID(3);
        Managers.getDefault().getSubtaskByID(4);
        Managers.getDefault().getSubtaskByID(5);
        Managers.getDefault().getEpicByID(6);
        Managers.getDefault().getSubtaskByID(7);
        System.out.println("история 10 последних запросов:");
        System.out.println(Managers.getDefaultHistory().getHistory().toString());
    }
}
