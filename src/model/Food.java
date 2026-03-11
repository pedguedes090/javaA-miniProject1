package model;

// Lớp Food kế thừa từ MenuItem
public class Food extends MenuItem {

    // Constructor
    public Food(String id, String name, double basePrice, int stock) {
        super(id, name, basePrice, stock);
    }

    // Food giữ nguyên giá gốc
    @Override
    public double calculatePrice() {
        return getBasePrice();
    }

    @Override
    public String toString() {
        return "Food -> " + super.toString() +
                ", Final Price: " + calculatePrice();
    }
}