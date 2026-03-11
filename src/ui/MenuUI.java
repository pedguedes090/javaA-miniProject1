 package ui;

import exception.InvalidInputException;
import exception.InvalidPriceException;
import exception.ItemNotFoundException;
import model.Dessert;
import model.Drink;
import model.Food;
import model.MenuItem;
import service.MenuService;
import service.SearchService;
import util.IdGenerator;
import util.InputHelper;
import java.util.List;

public class MenuUI {
    private MenuService menuService;
    private SearchService searchService = new SearchService();
    public MenuUI(MenuService menuService) {
        this.menuService = menuService;
    }

    public void menu() {
        while (true) {
            System.out.println("""
                    ===== MENU MANAGEMENT =====
                    1. Add item
                    2. Remove item
                    3. Search item
                    4. Show menu
                    0. Back
                    """);
            int choice = InputHelper.inputInt("Your choice: ");
            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    removeItem();
                    break;
                case 3:
                    searchItem();
                    break;
                case 4:
                    menuService.displayMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void addItem() {
        try {
            System.out.println("""
                Choose item type
                1. Food
                2. Drink
                3. Dessert
                """);
            int type = InputHelper.inputInt("Your choice: ");
            String id = IdGenerator.generatorMenuId();
            String name = InputHelper.inputString("Enter name: ");
            double price = InputHelper.inputDouble("Enter base price: ");
            int stock = InputHelper.inputInt("Enter stock: ");
            MenuItem item = null;
            switch (type) {
                case 1:
                    item = new Food(id, name, price, stock);
                    break;
                case 2:
                    String size = InputHelper.inputString("Enter size (S/M/L): ");
                    item = new Drink(id, name, price, stock, size);
                    break;
                case 3:
                    item = new Dessert(id, name, price, stock);
                    break;
                default:
                    System.out.println("Invalid type");
                    return;
            }
            menuService.addItem(item);
            System.out.println("Item added successfully!");
        } catch (InvalidInputException | InvalidPriceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeItem() {
        try {
            String name = InputHelper.inputString("Enter item name to remove: ");
            menuService.removeItem(name);
            System.out.println("Item removed successfully!");
        } catch (InvalidInputException | ItemNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchItem() {

        System.out.println("""
            ===== SEARCH MENU =====
            1. Search by name
            2. Search by price range
            """);

        int choice = InputHelper.inputInt("Your choice: ");

        List<MenuItem> result = null;

        switch (choice) {

            case 1:

                String keyword = InputHelper.inputString("Enter name: ");

                result = searchService.searchByName(
                        menuService.getMenu(),
                        keyword
                );

                break;

            case 2:

                double min = InputHelper.inputDouble("Enter min price: ");
                double max = InputHelper.inputDouble("Enter max price: ");

                result = searchService.searchByPriceRange(
                        menuService.getMenu(),
                        min,
                        max
                );

                break;

            default:
                System.out.println("Invalid choice");
                return;
        }

        if (result == null || result.isEmpty()) {
            System.out.println("No item found");
            return;
        }

        System.out.println("=== Search Result ===");

        for (MenuItem item : result) {
            System.out.println(item);
        }
    }
}

