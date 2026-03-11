 package ui;

import exception.InvalidInputException;
import exception.InvalidPriceException;
import exception.ItemNotFoundException;
import model.Dessert;
import model.Drink;
import model.Food;
import model.MenuItem;
import service.MenuService;
import util.IdGenerator;
import util.InputHelper;
import java.util.List;

public class MenuUI {
    private MenuService menuService;

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
        String keyword = InputHelper.inputString("Enter keyword: ");
        List<MenuItem> result = menuService.search(keyword);
        if (result.isEmpty()) {
            System.out.println("No item found");
            return;
        }
        for (MenuItem item : result) {
            System.out.println(item);
        }
    }
}

