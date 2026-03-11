package service;

import model.Dessert;
import model.MenuItem;
import model.Order;
import model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsServiceTest {

    private StatisticsService statisticsService;
    private List<Order> mockOrders;

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsService();
        mockOrders = new ArrayList<>();

        MenuItem itemA = new Dessert("M01", "Bánh Flan", 15000.0, 100);
        MenuItem itemB = new Dessert("M02", "Trà Sữa", 30000.0, 100);
        MenuItem itemC = new Dessert("M03", "Cà Phê", 20000.0, 100);

        // --- ĐƠN HÀNG 1: Ngày 15/03/2026 ---
        Order order1 = new Order("ORD-001");
        order1.setStatus(OrderStatus.PAID);
        order1.addItem(itemA, 2); // 30k
        order1.addItem(itemB, 1); // 30k
        // Tổng: 60k
        setOrderCreationDate(order1, LocalDateTime.of(2026, 3, 15, 10, 0));
        mockOrders.add(order1);

        // --- ĐƠN HÀNG 2: CÙNG NGÀY 15/03/2026 ---
        Order order2 = new Order("ORD-002");
        order2.setStatus(OrderStatus.PAID);
        order2.addItem(itemA, 3); // 45k
        // Tổng: 45k
        setOrderCreationDate(order2, LocalDateTime.of(2026, 3, 15, 15, 30));
        mockOrders.add(order2);

        // --- ĐƠN HÀNG 3: Ngày 16/03/2026 (BỊ HỦY -> Phải bỏ qua) ---
        Order order3 = new Order("ORD-003");
        order3.setStatus(OrderStatus.CANCELLED);
        order3.addItem(itemC, 5); // 100k
        setOrderCreationDate(order3, LocalDateTime.of(2026, 3, 16, 9, 0));
        mockOrders.add(order3);

        // --- ĐƠN HÀNG 4: THÁNG MỚI 05/04/2026 ---
        Order order4 = new Order("ORD-004");
        order4.setStatus(OrderStatus.PAID);
        order4.addItem(itemB, 4); // 120k
        setOrderCreationDate(order4, LocalDateTime.of(2026, 4, 5, 20, 0));
        mockOrders.add(order4);
    }


    private void setOrderCreationDate(Order order, LocalDateTime dateTime) {
        try {
            Field field = Order.class.getDeclaredField("createdAt");
            field.setAccessible(true);
            field.set(order, dateTime);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi Reflection khi cố gắng đổi ngày giờ đơn hàng: " + e.getMessage());
        }
    }

    // ==========================================
    // 1. TEST THỐNG KÊ THEO NGÀY
    // ==========================================
    @Test
    void testRevenueByDay_NullOrEmpty() {
        assertTrue(statisticsService.revenueByDay(null).isEmpty());
        assertTrue(statisticsService.revenueByDay(new ArrayList<>()).isEmpty());
    }

    @Test
    void testRevenueByDay_Success() {
        Map<LocalDate, Double> result = statisticsService.revenueByDay(mockOrders);

        // Ngày 15/03/2026: Đơn 1 (60k) + Đơn 2 (45k) = 105k
        LocalDate march15 = LocalDate.of(2026, 3, 15);
        assertEquals(105000.0, result.get(march15), 0.001);

        // Ngày 16/03/2026: Đơn 3 (CANCELLED) -> Không được có trong map
        LocalDate march16 = LocalDate.of(2026, 3, 16);
        assertNull(result.get(march16));

        // Ngày 05/04/2026: Đơn 4 (120k)
        LocalDate april5 = LocalDate.of(2026, 4, 5);
        assertEquals(120000.0, result.get(april5), 0.001);
    }

    // ==========================================
    // 2. TEST THỐNG KÊ THEO THÁNG
    // ==========================================
    @Test
    void testRevenueByMonth_NullOrEmpty() {
        assertTrue(statisticsService.revenueByMonth(null).isEmpty());
        assertTrue(statisticsService.revenueByMonth(new ArrayList<>()).isEmpty());
    }

    @Test
    void testRevenueByMonth_Success() {
        Map<YearMonth, Double> result = statisticsService.revenueByMonth(mockOrders);

        // Tháng 03/2026: 105k
        YearMonth march = YearMonth.of(2026, 3);
        assertEquals(105000.0, result.get(march), 0.001);

        // Tháng 04/2026: 120k
        YearMonth april = YearMonth.of(2026, 4);
        assertEquals(120000.0, result.get(april), 0.001);
    }

    // ==========================================
    // 3. TEST TOP MÓN BÁN CHẠY
    // ==========================================
    @Test
    void testGetTopSellingItems_NullOrEmpty() {
        assertTrue(statisticsService.getTopSellingItems(null).isEmpty());
        assertTrue(statisticsService.getTopSellingItems(mockOrders, 0).isEmpty());
    }

    @Test
    void testGetTopSellingItems_Success() {
        /*
         * Tổng kết số lượng bán ra:
         * - Bánh Flan: 2 + 3 = 5 cái (Doanh thu: 75k)
         * - Trà Sữa: 1 + 4 = 5 ly (Doanh thu: 150k)
         * -> Cùng Qty = 5, nhưng Trà Sữa doanh thu cao hơn nên phải đứng trước.
         */
        List<StatisticsService.ItemSales> topItems = statisticsService.getTopSellingItems(mockOrders, 5);

        assertEquals(2, topItems.size()); // Chỉ có 2 món hợp lệ, bỏ qua Cà Phê (bị hủy)

        // Top 1: Trà Sữa
        assertEquals("M02", topItems.get(0).getId());
        assertEquals("Trà Sữa", topItems.get(0).getName());
        assertEquals(5, topItems.get(0).getQuantity());
        assertEquals(150000.0, topItems.get(0).getRevenue(), 0.001);

        // Top 2: Bánh Flan
        assertEquals("M01", topItems.get(1).getId());
        assertEquals(5, topItems.get(1).getQuantity());
        assertEquals(75000.0, topItems.get(1).getRevenue(), 0.001);
    }
}