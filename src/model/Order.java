package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private String orderId;
    private String customerName;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private double discount;
    private double serviceFee;

    public Order(String orderId) {
        this(orderId, "Guest");
    }

    public Order(String orderId, String customerName) {
        this(orderId, customerName, 0.0, 0.0);
    }

    public Order(String orderId, String customerName, double discount, double serviceFee) {
        setOrderId(orderId);
        setCustomerName(customerName);
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        setDiscount(discount);
        setServiceFee(serviceFee);
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        items.add(item);
    }

    public void addItem(MenuItem menuItem, int quantity) {
        addItem(new OrderItem(menuItem, quantity));
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    public double calculateTotal() {
        return items.stream()
                .mapToDouble(OrderItem::getLineTotal)
                .sum();
    }

    public double getFinalTotal() {
        return Math.max(0.0, calculateTotal() - discount + serviceFee);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order id cannot be blank");
        }
        this.orderId = orderId.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        this.customerName = customerName.trim();
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Order status cannot be null");
        }
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        if (discount < 0) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }
        this.discount = discount;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        if (serviceFee < 0) {
            throw new IllegalArgumentException("Service fee cannot be negative");
        }
        this.serviceFee = serviceFee;
    }
}