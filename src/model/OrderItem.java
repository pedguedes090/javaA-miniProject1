package model;

public class OrderItem {

    private MenuItem menuItem;
    private int quantity;
    private double unitPrice;

    public OrderItem(MenuItem menuItem, int quantity) {
        this(menuItem, quantity, menuItem != null ? menuItem.calculatePrice() : 0.0);
    }

    public OrderItem(MenuItem menuItem, int quantity, double unitPrice) {
        setMenuItem(menuItem);
        setQuantity(quantity);
        setUnitPrice(unitPrice);
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        if (menuItem == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
        }
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return unitPrice * quantity;
    }

    public double getTotalPrice() {
        return getLineTotal();
    }
}