package util;

import model.Dessert;
import model.Drink;
import model.Food;
import service.MenuService;
public class SampleData {
    public static void load(MenuService menuService) {
        try {
            menuService.addItem(
                    new Food(IdGenerator.generatorMenuId(),
                            "Fried Rice",
                            30000,
                            20)
            );
            menuService.addItem(
                    new Food(IdGenerator.generatorMenuId(),
                            "Burger",
                            40000,
                            15)
            );
            menuService.addItem(
                    new Drink(IdGenerator.generatorMenuId(),
                            "Milk Tea",
                            25000,
                            30,
                            "M")
            );
            menuService.addItem(
                    new Drink(IdGenerator.generatorMenuId(),
                            "Coffee",
                            20000,
                            25,
                            "L")
            );
            menuService.addItem(
                    new Dessert(IdGenerator.generatorMenuId(),
                            "Cheese Cake",
                            35000,
                            10)
            );

        } catch (Exception e) {
            System.out.println("Sample data error");
        }
    }
}
