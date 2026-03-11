package ui;

import exception.InvalidOrderIdException;
import service.MenuService;
import service.OrderService;
import util.InputHelper;
import util.SampleData;

public class MainMenu {

    public static void main(String[] args) throws InvalidOrderIdException {

        MenuService menuService = new MenuService();
        SampleData.load(menuService);
        OrderService orderService = new OrderService(menuService);

        MenuUI menuUI = new MenuUI(menuService);
        OrderUI orderUI = new OrderUI(orderService,menuService);
        StatisticsUI statisticsUI = new StatisticsUI(orderService);

        while (true) {

            System.out.println("""
                    ===== RESTAURANT MANAGEMENT =====
                    1. Manage Menu
                    2. Manage Orders
                    3. Statistics
                    0. Exit
                    """);

            int choice = InputHelper.inputInt("Your choice: ");

            switch (choice) {

                case 1:
                    menuUI.menu();
                    break;

                case 2:
                    orderUI.OrderMenu();
                    break;

                case 3:
                    statisticsUI.menu();
                    break;

                case 0:
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}