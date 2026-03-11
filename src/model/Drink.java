package model;

// Lớp Drink kế thừa MenuItem
public class Drink extends MenuItem {

    private String size; // S, M, L

    public Drink(String id, String name, double basePrice, int stock, String size) {
        super(id, name, basePrice, stock);
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    // Override cách tính giá theo size
    @Override
    public double calculatePrice() {

        double price = getBasePrice();

        switch (size.toUpperCase()) {
            case "M":
                price += 5000;
                break;
            case "L":
                price += 10000;
                break;
            default:
                break;
        }

        return price;
    }

    @Override
    public String toString() {
        return "Drink -> " + super.toString() +
                ", Size: " + size +
                ", Final Price: " + calculatePrice();
    }
}