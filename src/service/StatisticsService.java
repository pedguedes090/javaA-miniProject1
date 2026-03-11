package service;

import model.Order;
import model.OrderItem;
import model.OrderStatus;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service cung cấp các chức năng thống kê sử dụng Stream API.
 * - Thống kê doanh thu theo ngày (chỉ tính đơn PAID)
 * - Thống kê doanh thu theo tháng (chỉ tính đơn PAID)
 * - Liệt kê món bán chạy nhất (theo số lượng) (chỉ tính đơn PAID)
 */
public class StatisticsService {

    public StatisticsService() {
    }

    /**
     * Thống kê doanh thu theo ngày (LocalDate) từ danh sách đơn hàng.
     * Chỉ tính những đơn ở trạng thái PAID.
     * @param orders danh sách đơn hàng (có thể null hoặc rỗng)
     * @return Bản đồ LocalDate -> tổng doanh thu trong ngày (sử dụng Order.getFinalTotal())
     */
    public Map<LocalDate, Double> revenueByDay(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyMap();
        }

        return orders.stream()
                .filter(Objects::nonNull)
                .filter(o -> o.getStatus() == OrderStatus.PAID)
                .collect(Collectors.groupingBy(
                        o -> o.getCreatedAt().toLocalDate(),
                        Collectors.summingDouble(Order::getFinalTotal)
                ));
    }

    /**
     * Thống kê doanh thu theo tháng (YearMonth) từ danh sách đơn hàng.
     * Chỉ tính những đơn ở trạng thái PAID.
     * @param orders danh sách đơn hàng (có thể null hoặc rỗng)
     * @return Bản đồ YearMonth -> tổng doanh thu trong tháng
     */
    public Map<YearMonth, Double> revenueByMonth(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyMap();
        }

        return orders.stream()
                .filter(Objects::nonNull)
                .filter(o -> o.getStatus() == OrderStatus.PAID)
                .collect(Collectors.groupingBy(
                        o -> YearMonth.from(o.getCreatedAt()),
                        Collectors.summingDouble(Order::getFinalTotal)
                ));
    }

    /**
     * Lấy danh sách món bán chạy nhất, sắp xếp giảm dần theo số lượng bán.
     * Chỉ tính các OrderItem thuộc đơn PAID.
     * @param orders danh sách đơn hàng
     * @param topN số lượng mục top cần trả về (nếu <=0 sẽ trả về rỗng)
     * @return danh sách ItemSales đã được sắp xếp giảm dần theo quantity
     */
    public List<ItemSales> getTopSellingItems(List<Order> orders, int topN) {
        if (orders == null || orders.isEmpty() || topN <= 0) {
            return Collections.emptyList();
        }

        // Lấy tất cả OrderItem từ các đơn PAID
        List<OrderItem> paidItems = orders.stream()
                .filter(Objects::nonNull)
                .filter(o -> o.getStatus() == OrderStatus.PAID)
                .flatMap(o -> o.getItems().stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (paidItems.isEmpty()) {
            return Collections.emptyList();
        }

        // Map id -> total quantity
        Map<String, Integer> qtyById = paidItems.stream()
                .collect(Collectors.groupingBy(
                        it -> it.getMenuItem().getId(),
                        Collectors.summingInt(OrderItem::getQuantity)
                ));

        // Map id -> total revenue
        Map<String, Double> revenueById = paidItems.stream()
                .collect(Collectors.groupingBy(
                        it -> it.getMenuItem().getId(),
                        Collectors.summingDouble(OrderItem::getLineTotal)
                ));

        // Map id -> name (take any)
        Map<String, String> nameById = paidItems.stream()
                .collect(Collectors.toMap(
                        it -> it.getMenuItem().getId(),
                        it -> it.getMenuItem().getName(),
                        (a, b) -> a
                ));

        // Build ItemSales list
        List<ItemSales> all = new ArrayList<>();
        for (Map.Entry<String, Integer> e : qtyById.entrySet()) {
            String id = e.getKey();
            int qty = e.getValue();
            double rev = revenueById.getOrDefault(id, 0.0);
            String name = nameById.getOrDefault(id, "");
            all.add(new ItemSales(id, name, qty, rev));
        }

        // Sort by quantity desc then revenue desc then name
        all.sort(Comparator.comparingInt(ItemSales::getQuantity).reversed()
                .thenComparingDouble(ItemSales::getRevenue).reversed()
                .thenComparing(ItemSales::getName));

        if (topN >= all.size()) {
            return all;
        }

        return all.subList(0, topN);
    }

    /**
     * Mặc định trả về top 5
     */
    public List<ItemSales> getTopSellingItems(List<Order> orders) {
        return getTopSellingItems(orders, 5);
    }

    // Helper POJO for result
    public static class ItemSales {
        private final String id;
        private final String name;
        private final int quantity;
        private final double revenue;

        public ItemSales(String id, String name, int quantity, double revenue) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.revenue = revenue;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getRevenue() {
            return revenue;
        }

        @Override
        public String toString() {
            return "ItemSales{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", quantity=" + quantity +
                    ", revenue=" + revenue +
                    '}';
        }
    }
}
