package ui;

import model.Order;
import model.OrderItem;
import model.OrderStatus;
import service.OrderService;
import util.InputHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsUI {

    private OrderService orderService;

    public StatisticsUI(OrderService orderService) {
        this.orderService = orderService;
    }

    public void menu() {

        while (true) {

            System.out.println("""
                    ===== STATISTICS =====
                    1. Total orders
                    2. Revenue
                    3. Orders by status
                    4. Best selling items
                    0. Back
                    """);

            int choice = InputHelper.inputInt("Your choice: ");

            switch (choice) {

                case 1:
                    showTotalOrders();
                    break;

                case 2:
                    showRevenue();
                    break;

                case 3:
                    showOrdersByStatus();
                    break;

                case 4:
                    showBestSellingItems();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void showTotalOrders() {

        List<Order> orders = orderService.getOrders();

        System.out.println("Total orders: " + orders.size());
    }

    private void showRevenue() {

        double revenue = 0;

        for (Order order : orderService.getOrders()) {

            if (order.getStatus() == OrderStatus.PAID) {

                for (OrderItem item : order.getItems()) {
                    revenue += item.getLineTotal();
                }
            }
        }

        System.out.println("Total revenue: " + revenue);
    }

    private void showOrdersByStatus() {

        int pending = 0;
        int paid = 0;
        int cancelled = 0;

        for (Order order : orderService.getOrders()) {

            switch (order.getStatus()) {

                case PENDING:
                    pending++;
                    break;

                case PAID:
                    paid++;
                    break;

                case CANCELLED:
                    cancelled++;
                    break;
            }
        }

        System.out.println("Pending: " + pending);
        System.out.println("Paid: " + paid);
        System.out.println("Cancelled: " + cancelled);
    }

    private void showBestSellingItems() {

        Map<String, Integer> counter = new HashMap<>();

        for (Order order : orderService.getOrders()) {

            for (OrderItem item : order.getItems()) {

                String name = item.getMenuItem().getName();
                int quantity = item.getQuantity();

                counter.put(name, counter.getOrDefault(name, 0) + quantity);
            }
        }


        System.out.println("=== Best Selling Items ===");
        List<Map.Entry<String, Integer>> list = new ArrayList<>(counter.entrySet());

        // sort giảm dần theo quantity
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        for (String name : counter.keySet()) {
            System.out.println(name + " : " + counter.get(name));
        }
    }
}
