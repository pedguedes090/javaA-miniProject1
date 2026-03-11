package model;

// Món ăn trong đơn hàng
public class OrderItem {

    private MenuItem menuItem; // món ăn
    private int quantity;      // số lượng

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    // tính tiền món
    public double getTotalPrice() {
        return menuItem.calculatePrice() * quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }
}