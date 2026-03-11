package model;

// Lớp Dessert kế thừa MenuItem
public class Dessert extends MenuItem {

    public Dessert(String id, String name, double basePrice, int stock) {
        super(id, name, basePrice, stock);
    }

    // Dessert giữ nguyên giá
    @Override
    public double calculatePrice() {
        return getBasePrice();
    }

    @Override
    public String toString() {
        return "Dessert -> " + super.toString() +
                ", Final Price: " + calculatePrice();
    }
}