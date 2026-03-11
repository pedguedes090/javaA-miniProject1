package service;

import exception.InsufficientStockException;
import exception.InvalidInputException;
import exception.InvalidOrderIdException;
import model.Dessert;
import model.MenuItem;
import model.Order;
import model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
    }

    // ==========================================
    // 1. TEST TẠO ĐƠN HÀNG
    // ==========================================
    @Test
    void testCreateOrder_NullOrEmptyId_ThrowsException() {
        assertThrows(InvalidOrderIdException.class, () -> orderService.createOrder(null));
        assertThrows(InvalidOrderIdException.class, () -> orderService.createOrder("   "));
    }

    @Test
    void testCreateOrder_DuplicateId_ThrowsException() throws InvalidOrderIdException {
        orderService.createOrder("ORD-001");
        assertThrows(InvalidOrderIdException.class, () -> orderService.createOrder(" ORD-001 "));
    }

    @Test
    void testCreateOrder_Success() throws InvalidOrderIdException {
        Order order = orderService.createOrder("ORD-002");
        assertNotNull(order);
        assertEquals("ORD-002", order.getId());
        assertEquals(1, orderService.getOrders().size());
    }

    // ==========================================
    // 2. TEST THÊM MÓN VÀO ĐƠN
    // ==========================================
    @Test
    void testAddItem_InvalidInput_ThrowsException() throws InvalidOrderIdException {
        orderService.createOrder("ORD-003");
        MenuItem validItem = new Dessert("M01", "Bánh Flan", 15000, 10);

        // Lỗi 1: Món null
        assertThrows(InvalidInputException.class, () -> orderService.addItemToOrder("ORD-003", null, 1));
        // Lỗi 2: Số lượng <= 0
        assertThrows(InvalidInputException.class, () -> orderService.addItemToOrder("ORD-003", validItem, 0));
    }

    @Test
    void testAddItem_InsufficientStock_ThrowsException() throws Exception {
        orderService.createOrder("ORD-004");
        MenuItem itemWithStock5 = new Dessert("M02", "Chè", 20000, 5);

        // Mua 10 ly trong khi kho chỉ có 5
        assertThrows(InsufficientStockException.class, () -> {
            orderService.addItemToOrder("ORD-004", itemWithStock5, 10);
        });
    }

    @Test
    void testAddItem_Success_ReducesStock() throws Exception {
        Order order = orderService.createOrder("ORD-005");
        MenuItem item = new Dessert("M03", "Kem", 10000, 10);

        orderService.addItemToOrder("ORD-005", item, 3);

        assertEquals(1, order.getItems().size());
        assertEquals(3, order.getItems().get(0).getQuantity());
        assertEquals(7, item.getStock()); // Kho 10 trừ 3 còn 7
    }

    // ==========================================
    // 3. TEST CẬP NHẬT TRẠNG THÁI
    // ==========================================
    @Test
    void testUpdateStatus_ModifyPaidOrder_ThrowsException() throws Exception {
        orderService.createOrder("ORD-006");
        orderService.updateOrderStatus("ORD-006", OrderStatus.PAID);

        InvalidInputException ex = assertThrows(InvalidInputException.class, () -> {
            orderService.updateOrderStatus("ORD-006", OrderStatus.CANCELLED);
        });
        assertTrue(ex.getMessage().contains("da thanh toan"));
    }

    @Test
    void testUpdateStatus_ToCancelled_RestoresStock() throws Exception {
        orderService.createOrder("ORD-007");
        MenuItem item = new Dessert("M04", "Bánh mì", 15000, 10);

        orderService.addItemToOrder("ORD-007", item, 4);
        assertEquals(6, item.getStock()); // Còn 6

        orderService.updateOrderStatus("ORD-007", OrderStatus.CANCELLED);
        assertEquals(10, item.getStock()); // Hủy đơn, hoàn lại thành 10
    }

    // ==========================================
    // 4. TEST XÓA MÓN KHỎI ĐƠN
    // ==========================================
    @Test
    void testRemoveItem_Success_RestoresStock() throws Exception {
        Order order = orderService.createOrder("ORD-008");
        MenuItem item = new Dessert("M08", "Cà phê", 25000, 10);

        orderService.addItemToOrder("ORD-008", item, 5);
        assertEquals(5, item.getStock());

        orderService.removeItemFromOrder("ORD-008", "M08");

        assertTrue(order.getItems().isEmpty());
        assertEquals(10, item.getStock()); // Xóa món, hoàn lại kho
    }
}