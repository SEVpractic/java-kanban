import manager.Manager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task1 = new Task("Первая задача", "Описание первой задачи", manager.generateIdNumber(),
                Status.NEW);
        manager.addTasks(task1);

        Task task2 = new Task("Вторая задача", "Описание второй задачи", manager.generateIdNumber(),
                Status.NEW);
        manager.addTasks(task2);

        Epic epic1 = new Epic("Первый эпик", "Описание первого эпика", manager.generateIdNumber(),
                Status.NEW, new ArrayList<>());
        manager.addEpics(epic1);

        Subtask subtask11 = new Subtask("Первая подзадача первого эпика",
                "Описание первой подзадачи первого эпика", manager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        manager.addSubtasks(subtask11);

        Subtask subtask12 = new Subtask("Вторая подзадача первого эпика",
                "Описание второй подзадачи первого эпика", manager.generateIdNumber(),
                Status.NEW, epic1.getIdNumber());
        manager.addSubtasks(subtask12);

        Epic epic2 = new Epic("Второй эпик", "описание второго эпика", manager.generateIdNumber(),
                Status.NEW, new ArrayList<>());
        manager.addEpics(epic2);

        Subtask subtask21 = new Subtask("Первая подзадача второго эпика",
                "Описание первой подзадачи второго эпика", manager.generateIdNumber(),
                Status.NEW, epic2.getIdNumber());
        manager.addSubtasks(subtask21);

        System.out.println(manager.getTasks().toString());
        System.out.println(manager.getEpics().toString());
        System.out.println(manager.getSubtasks().toString() + "\n");

        Task task1v2 = new Task("Первая задача", "Описание первой задачи c первым изменением",
                task1.getIdNumber(), Status.DONE);
        manager.updateTasks(task1v2);

        Subtask subtask11v2 = new Subtask("Первая подзадача первого эпика",
                "Описание первой подзадачи первого эпика с первым изменением",
                subtask11.getIdNumber(), Status.IN_PROGRESS, epic1.getIdNumber());
        manager.updateSubtasks(subtask11v2);

        Subtask subtask21v2 = new Subtask("Первая подзадача второго эпика",
                "Описание первой подзадачи второго эпика с изменением",
                subtask21.getIdNumber(), Status.DONE, epic2.getIdNumber());
        manager.updateSubtasks(subtask21v2);

        System.out.println(manager.getTasks().toString());
        System.out.println(manager.getEpics().toString());
        System.out.println(manager.getSubtasks().toString() + "\n");

        manager.deliteTaskByID(task1v2.getIdNumber());
        manager.deliteEpicByID(epic2.getIdNumber());

        System.out.println(manager.getTasks().toString());
        System.out.println(manager.getEpics().toString());
        System.out.println(manager.getSubtasks().toString() + "\n");
    }
}
