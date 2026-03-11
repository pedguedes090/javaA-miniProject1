package service;

import exception.InsufficientStockException;
import exception.InvalidInputException;
import exception.InvalidOrderIdException;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.OrderStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class OrderService {

    private final List<Order> orders = new ArrayList<>();

    public OrderService(MenuService menuService) {
    }

    public Order createOrder(String orderId) throws InvalidOrderIdException {
        return createOrder(orderId, "Guest");
    }

    public Order createOrder(String orderId, String customerName) throws InvalidOrderIdException {
        String normalizedOrderId = normalizeOrderId(orderId);

        if (findOrderById(normalizedOrderId).isPresent()) {
            throw new InvalidOrderIdException("Ma don hang da ton tai: " + normalizedOrderId);
        }

        Order order = new Order(normalizedOrderId, customerName);
        orders.add(order);
        return order;
    }

    public void addItemToOrder(String orderId, MenuItem menuItem, int quantity)
            throws InvalidOrderIdException, InsufficientStockException, InvalidInputException {
        if (menuItem == null) {
            throw new InvalidInputException("Mon an khong hop le");
        }
        if (quantity <= 0) {
            throw new InvalidInputException("So luong phai lon hon 0");
        }

        Order order = getOrderById(orderId);
        ensureOrderIsMutable(order);

        if (menuItem.getStock() < quantity) {
            throw new InsufficientStockException(
                    "So luong vuot qua ton kho. Ton hien tai: " + menuItem.getStock());
        }

        order.addItem(menuItem, quantity);
        menuItem.setStock(menuItem.getStock() - quantity);
    }

    public Order getOrderById(String orderId) throws InvalidOrderIdException {
        String normalizedOrderId = normalizeOrderId(orderId);
        return findOrderById(normalizedOrderId)
                .orElseThrow(() -> new InvalidOrderIdException(
                        "Khong tim thay don hang voi ma: " + normalizedOrderId));
    }

    public Optional<Order> findOrderById(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return Optional.empty();
        }

        String normalizedOrderId = orderId.trim();
        return orders.stream()
                .filter(order -> order.getOrderId().equalsIgnoreCase(normalizedOrderId))
                .findFirst();
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus)
            throws InvalidOrderIdException, InvalidInputException {
        if (newStatus == null) {
            throw new InvalidInputException("Trang thai don hang khong hop le");
        }

        Order order = getOrderById(orderId);
        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.PAID && newStatus != OrderStatus.PAID) {
            throw new InvalidInputException("Don hang da thanh toan, khong the thay doi trang thai");
        }

        if (currentStatus == OrderStatus.CANCELLED && newStatus != OrderStatus.CANCELLED) {
            throw new InvalidInputException("Don hang da huy, khong the thay doi trang thai");
        }

        if (newStatus == OrderStatus.CANCELLED && currentStatus != OrderStatus.CANCELLED) {
            restoreStock(order);
        }

        order.setStatus(newStatus);
    }

    // Optional helper kept for menu flow: remove a line item and restore stock
    public void removeItemFromOrder(String orderId, String menuItemId)
            throws InvalidOrderIdException, InvalidInputException {
        if (menuItemId == null || menuItemId.trim().isEmpty()) {
            throw new InvalidInputException("Ma mon an khong hop le");
        }

        Order order = getOrderById(orderId);
        ensureOrderIsMutable(order);

        Iterator<OrderItem> iterator = order.getItems().iterator();
        while (iterator.hasNext()) {
            OrderItem orderItem = iterator.next();
            if (orderItem.getMenuItem().getId().equalsIgnoreCase(menuItemId.trim())) {
                MenuItem menuItem = orderItem.getMenuItem();
                menuItem.setStock(menuItem.getStock() + orderItem.getQuantity());
                iterator.remove();
                return;
            }
        }

        throw new InvalidInputException("Khong tim thay mon trong don: " + menuItemId.trim());
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    private String normalizeOrderId(String orderId) throws InvalidOrderIdException {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new InvalidOrderIdException("Ma don hang khong hop le");
        }
        return orderId.trim();
    }

    private void ensureOrderIsMutable(Order order) throws InvalidInputException {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidInputException("Chi duoc sua don hang o trang thai PENDING");
        }
    }

    private void restoreStock(Order order) {
        for (OrderItem orderItem : order.getItems()) {
            MenuItem menuItem = orderItem.getMenuItem();
            menuItem.setStock(menuItem.getStock() + orderItem.getQuantity());
        }
    }
}
