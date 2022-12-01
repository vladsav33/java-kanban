import manager.Manager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        manager.makeTest();

        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    manager.showAllObjects();
                    break;
                case 2:
                    manager.deleteAllObjects();
                    break;
                case 3:
                    manager.showObjectById();
                    break;
                case 4:
                    manager.createObject();
                    break;
                case 5:
                    manager.updateObject();
                    break;
                case 6:
                    manager.removeObjectById();
                    break;
                case 7:
                    manager.showEpicById();
                    break;
                case 0:
                    return;
            }
        }
    }

    public static void printMenu() {
        System.out.println("1. Показать все задачи");
        System.out.println("2. Удалить все задачи");
        System.out.println("3. Показать задачу по идентификатору");
        System.out.println("4. Создать новую задачу");
        System.out.println("5. Обновить задачу");
        System.out.println("6. Удалить задачу по идентификатору");
        System.out.println("7. Показать все подзадачи эпика");
        System.out.println("0. Выход");
        System.out.println("----------------------------------------------------");
        System.out.println("Выберите необходимое действие");
    }
}
