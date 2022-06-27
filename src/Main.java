public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        String name;
        String description;
        int idNumber;
        String status;

        name = "Купить хлеб";
        description = "Купить свежайших булочек для бутербродов";
        idNumber = manager.generateIdNumber();
        status = "NEW";
        Task task1 = new Task(name, description, idNumber, status);
        manager.setTasks(task1);

        name = "Заправить машину";
        description = "Заправить машину 95 бензином перед длительной поездкой за хлебом";
        idNumber = manager.generateIdNumber();
        status = "NEW";
        Task task2 = new Task(name, description, idNumber, status);
        manager.setTasks(task2);

        System.out.println(manager.getTasks().toString());

        name = "Купить хлеб";
        description = "Купить свежайших булочек для бутербродов";
        idNumber = task1.getIdNumber();
        status = "IN_PROGRESS";
        task1 = new Task(name, description, idNumber, status);
        manager.setTasks(task1);

        name = "Заправить машину";
        description = "Заправить машину 95 бензином перед длительной поездкой за хлебом";
        idNumber = task2.getIdNumber();
        status = "DONE";
        task2 = new Task(name, description, idNumber, status);
        manager.setTasks(task2);

        System.out.println(manager.getTasks().toString());

        idNumber = task2.getIdNumber();
        manager.deliteTaskByID(idNumber);
        System.out.println(manager.getTasks().toString());
    }
}
