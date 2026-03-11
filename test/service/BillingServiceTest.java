package service;

import exception.EmptyOrderException;
import exception.InvalidDiscountException;
import model.Dessert;
import model.Discount;
import model.Order;
import model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BillingServiceTest {

    private BillingService billingService;

    @BeforeEach
    void setUp() {
        billingService = new BillingService();
    }

    @Test
    void testCalculateServiceFee() {
        double fee = billingService.calculateServiceFee(1000.0);
        assertEquals(50.0, fee, 0.001);
    }

    @Test
    void testCalculateSubtotal_EmptyItems_ThrowsException() {
        Order emptyOrder = new Order("ORD-01");
        assertThrows(EmptyOrderException.class, () -> billingService.calculateSubtotal(emptyOrder));
    }

    @Test
    void testCalculateDiscountAmount_InvalidDiscount_ThrowsException() {
        // Mã giảm 150% là không hợp lệ
        Discount invalidDiscount = new Discount("SAI_MA", 150.0, true);
        assertThrows(InvalidDiscountException.class, () -> {
            billingService.calculateDiscountAmount(1000.0, invalidDiscount);
        });
    }

    @Test
    void testCalculateDiscountAmount_ExpiredDiscount_ThrowsException() {
        // Mã giảm giá đã hết hạn (active = false)
        Discount expiredDiscount = new Discount("HET_HAN", 10.0, false);
        assertThrows(InvalidDiscountException.class, () -> {
            billingService.calculateDiscountAmount(1000.0, expiredDiscount);
        });
    }

    @Test
    void testCalculateTotal_Success() throws EmptyOrderException {
        Order validOrder = new Order("ORD-02");
        // Bánh kem giá 50k x 2 cái = 100k
        validOrder.addItem(new OrderItem(new Dessert("D01", "Bánh kem", 50000.0, 10), 2));

        // Tổng tiền món: 100,000. Phí dịch vụ 5%: 5,000. Tổng cộng: 105,000.
        double total = billingService.calculateTotal(validOrder);
        assertEquals(105000.0, total, 0.001);
    }

    @Test
    void testCalculateTotalWithDiscount_Success() throws EmptyOrderException, InvalidDiscountException {
        Order validOrder = new Order("ORD-03");
        // Bánh ngọt 100k x 1 cái = 100k
        validOrder.addItem(new OrderItem(new Dessert("D02", "Bánh ngọt", 100000.0, 10), 1));

        // Giảm giá 20% hợp lệ
        Discount validDiscount = new Discount("GIAM20", 20.0, true);

        // Toán học: Subtotal = 100,000. Giảm 20% = 20,000 -> Còn 80,000.
        // Phí dịch vụ 5% của 80,000 = 4,000.
        // Tổng cuối cùng = 84,000.
        double total = billingService.calculateTotalWithDiscount(validOrder, validDiscount);
        assertEquals(84000.0, total, 0.001);
    }

    @Test
    void testGenerateBill_Success() throws EmptyOrderException, InvalidDiscountException {
        Order validOrder = new Order("ORD-VIP");
        validOrder.addItem(new OrderItem(new Dessert("D03", "Kem", 200000.0, 10), 1));
        Discount validDiscount = new Discount("VIP50", 50.0, true);

        String bill = billingService.generateBill(validOrder, validDiscount);

        assertNotNull(bill);
        assertTrue(bill.contains("ORD-VIP"));
        assertTrue(bill.contains("200,000 VND")); // Tổng tiền
        assertTrue(bill.contains("VIP50")); // Có in ra mã giảm giá
        assertTrue(bill.contains("100,000 VND")); // Số tiền được giảm
    }
}