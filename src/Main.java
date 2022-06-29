public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        String name;
        String description;
        String status;

        name = "Первая задача";
        description = "Описание первой задачи";
        status = "NEW";
        Task task1 = new Task(name, description, status);
        manager.setTasks(task1);

        name = "Вторая задача";
        description = "Описание второй задачи";
        status = "NEW";
        Task task2 = new Task(name, description, status);
        manager.setTasks(task2);

        name = "Первый эпик";
        description = "Описание первого эпика";
        status = "NEW";
        Epic epic1 = new Epic(name, description, status);
        manager.setEpics(epic1);

        name = "Первая подзадача первого эпика";
        description = "Описание первой подзадачи первого эпика";
        status = "NEW";
        Subtask subtask11 = new Subtask(name, description, status, epic1);
        manager.setSubtasks(subtask11);

        name = "Вторая подзадача первого эпика";
        description = "Описание второй подзадачи первого эпика";
        status = "NEW";
        Subtask subtask12 = new Subtask(name, description, status, epic1);
        manager.setSubtasks(subtask12);

        name = "Второй эпик";
        description = "описание второго эпика";
        status = "NEW";
        Epic epic2 = new Epic(name, description, status);
        manager.setEpics(epic2);

        name = "Первая подзадача второго эпика";
        description = "Описание первой подзадачи второго эпика";
        status = "NEW";
        Subtask subtask21 = new Subtask(name, description, status, epic2);
        manager.setSubtasks(subtask21);

        System.out.println(manager.getTasks().toString());
        System.out.println(manager.getEpics().toString());
        System.out.println(manager.getSubtasks().toString() + "\n");

        name = "Первая задача";
        description = "Описание первой задачи c первым изменением";
        status = "DONE";
        Task task1v2 = new Task(name, description, status);
        manager.updateTasks(task1v2, task1);

        name = "Первая подзадача первого эпика";
        description = "Описание первой подзадачи первого эпика с первым изменением";
        status = "IN_PROGRESS";
        Subtask subtask11v2 = new Subtask(name, description, status, epic1);
        manager.updateSubtasks(subtask11v2, subtask11);

        name = "Первая подзадача второго эпика";
        description = "Описание первой подзадачи второго эпика с изменением";
        status = "DONE";
        Subtask subtask21v2 = new Subtask(name, description, status, epic2);
        manager.updateSubtasks(subtask21v2, subtask21);

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
