package ui;

import model.Order;
import service.OrderService;
import service.StatisticsService;
import service.StatisticsService.ItemSales;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class StatisticsUI {

    private OrderService orderService;
    private StatisticsService statisticsService;

    public StatisticsUI(OrderService orderService) {
        this.orderService = orderService;
        this.statisticsService = new StatisticsService();
    }

    public void menu() {

        while (true) {

            System.out.println("""
                    ===== STATISTICS =====
                    1. Revenue by day
                    2. Revenue by month
                    3. Top selling items
                    0. Back
                    """);

            int choice = util.InputHelper.inputInt("Your choice: ");

            switch (choice) {

                case 1:
                    showRevenueByDay();
                    break;

                case 2:
                    showRevenueByMonth();
                    break;

                case 3:
                    showTopSelling();
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void showRevenueByDay() {

        List<Order> orders = orderService.getOrders();

        Map<LocalDate, Double> result =
                statisticsService.revenueByDay(orders);

        if (result.isEmpty()) {
            System.out.println("No data");
            return;
        }

        System.out.println("=== Revenue By Day ===");

        for (LocalDate date : result.keySet()) {
            System.out.println(date + " : " + result.get(date));
        }
    }

    private void showRevenueByMonth() {

        List<Order> orders = orderService.getOrders();
        Map<YearMonth, Double> result =
                statisticsService.revenueByMonth(orders);

        if (result.isEmpty()) {
            System.out.println("No data");
            return;
        }

        System.out.println("=== Revenue By Month ===");

        for (YearMonth month : result.keySet()) {
            System.out.println(month + " : " + result.get(month));
        }
    }

    private void showTopSelling() {

        List<Order> orders = orderService.getOrders();

        List<ItemSales> list =
                statisticsService.getTopSellingItems(orders);

        if (list.isEmpty()) {
            System.out.println("No data");
            return;
        }

        System.out.println("=== Top Selling Items ===");

        for (ItemSales item : list) {

            System.out.println(
                    item.getName()
                            + " | Quantity: " + item.getQuantity()
                            + " | Revenue: " + item.getRevenue()
            );
        }
    }
}