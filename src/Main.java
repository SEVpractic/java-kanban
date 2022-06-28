public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        String name;
        String description;
        String status;
        int idNumber;

        name = "Купить хлеб";
        description = "Купить свежайших булочек для бутербродов";
        status = "NEW";
        Task task1 = new Task(name, description, status);
        manager.setTasks(task1);

        name = "Заправить машину";
        description = "Заправить машину 95 бензином перед длительной поездкой за хлебом";
        status = "NEW";
        Task task2 = new Task(name, description, status);
        manager.setTasks(task2);

        System.out.println(manager.getTasks().toString());

        name = "Купить хлеб";
        description = "Купить свежайших булочек для бутербродов";
        status = "IN_PROGRESS";
        task1 = new Task(name, description, status);
        manager.setTasks(task1);

        name = "Заправить машину";
        description = "Заправить машину 95 бензином перед длительной поездкой за хлебом";
        status = "DONE";
        task2 = new Task(name, description, status);
        manager.setTasks(task2);

        System.out.println(manager.getTasks().toString());

        idNumber = task2.getIdNumber();
        manager.deliteTaskByID(idNumber);
        System.out.println(manager.getTasks().toString());
    }
}
