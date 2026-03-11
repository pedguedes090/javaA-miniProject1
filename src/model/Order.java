package model;

import java.util.ArrayList;
import java.util.List;

// Đơn hàng của khách
public class Order {

    private String id;                // id đơn hàng
    private List<OrderItem> items;    // danh sách món
    private OrderStatus status;       // trạng thái

    public Order(String id) {
        this.id = id;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    // thêm món vào đơn
    public void addItem(OrderItem item) {
        items.add(item);
    }

    // xóa món khỏi đơn
    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    // tính tổng tiền đơn hàng (Stream API)
    public double calculateTotal() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    public String getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}