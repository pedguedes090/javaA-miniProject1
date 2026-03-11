package service;

import exception.EmptyOrderException;
import exception.InvalidDiscountException;
import model.Discount;
import model.Order;

public class BillingService {

    // Phí dịch vụ mặc định: 5%
    private static final double DEFAULT_SERVICE_FEE_PERCENT = 5.0;

    private double serviceFeePercent;

    public BillingService() {
        this.serviceFeePercent = DEFAULT_SERVICE_FEE_PERCENT;
    }

    public BillingService(double serviceFeePercent) {
        this.serviceFeePercent = serviceFeePercent;
    }

    /**
     * Tính tổng tiền các món trong đơn hàng (subtotal)
     * @param order đơn hàng
     * @return tổng tiền các món
     * @throws EmptyOrderException nếu đơn hàng rỗng
     */
    public double calculateSubtotal(Order order) throws EmptyOrderException {
        validateOrder(order);
        return order.calculateTotal();
    }

    /**
     * Tính số tiền được giảm
     * @param subtotal tổng tiền trước giảm
     * @param discount mã giảm giá
     * @return số tiền được giảm
     * @throws InvalidDiscountException nếu mã giảm giá không hợp lệ
     */
    public double calculateDiscountAmount(double subtotal, Discount discount) 
            throws InvalidDiscountException {
        validateDiscount(discount);
        return discount.calculateDiscountAmount(subtotal);
    }

    /**
     * Tính phí dịch vụ
     * Phí dịch vụ tính trên tổng tiền SAU khi đã giảm giá
     * @param amountAfterDiscount số tiền sau giảm giá
     * @return phí dịch vụ
     */
    public double calculateServiceFee(double amountAfterDiscount) {
        return amountAfterDiscount * serviceFeePercent / 100;
    }

    /**
     * Tính tổng tiền cuối cùng của đơn hàng (không áp dụng giảm giá)
     * Công thức: subtotal + service fee
     * @param order đơn hàng
     * @return tổng tiền cuối cùng
     * @throws EmptyOrderException nếu đơn hàng rỗng
     */
    public double calculateTotal(Order order) throws EmptyOrderException {
        double subtotal = calculateSubtotal(order);
        double serviceFee = calculateServiceFee(subtotal);
        return subtotal + serviceFee;
    }

    /**
     * Tính tổng tiền cuối cùng có áp dụng mã giảm giá
     * Công thức: (subtotal - discount) + service fee
     * @param order đơn hàng
     * @param discount mã giảm giá
     * @return tổng tiền cuối cùng
     * @throws EmptyOrderException nếu đơn hàng rỗng
     * @throws InvalidDiscountException nếu mã giảm giá không hợp lệ
     */
    public double calculateTotalWithDiscount(Order order, Discount discount)
            throws EmptyOrderException, InvalidDiscountException {
        double subtotal = calculateSubtotal(order);
        double discountAmount = calculateDiscountAmount(subtotal, discount);
        double amountAfterDiscount = subtotal - discountAmount;
        double serviceFee = calculateServiceFee(amountAfterDiscount);
        return amountAfterDiscount + serviceFee;
    }

    /**
     * Tạo hóa đơn chi tiết
     * @param order đơn hàng
     * @param discount mã giảm giá (có thể null)
     * @return chuỗi hóa đơn chi tiết
     */
    public String generateBill(Order order, Discount discount)
            throws EmptyOrderException, InvalidDiscountException {
        StringBuilder bill = new StringBuilder();
        bill.append("=============== HÓA ĐƠN ===============\n");
        bill.append("Mã đơn: ").append(order.getId()).append("\n");
        bill.append("--------------------------------------\n");

        double subtotal = calculateSubtotal(order);
        bill.append("Tổng tiền món: ").append(formatMoney(subtotal)).append("\n");

        double discountAmount = 0;
        if (discount != null) {
            discountAmount = calculateDiscountAmount(subtotal, discount);
            bill.append("Giảm giá (").append(discount.getCode()).append(" - ")
                    .append(discount.getPercentage()).append("%): -")
                    .append(formatMoney(discountAmount)).append("\n");
        }

        double amountAfterDiscount = subtotal - discountAmount;
        double serviceFee = calculateServiceFee(amountAfterDiscount);
        bill.append("Phí dịch vụ (").append(serviceFeePercent).append("%): ")
                .append(formatMoney(serviceFee)).append("\n");

        bill.append("--------------------------------------\n");
        double total = amountAfterDiscount + serviceFee;
        bill.append("TỔNG CỘNG: ").append(formatMoney(total)).append("\n");
        bill.append("======================================\n");

        return bill.toString();
    }

    // Validation Methods

    
    // Kiểm tra đơn hàng có hợp lệ không
    
    private void validateOrder(Order order) throws EmptyOrderException {
        if (order == null) {
            throw new EmptyOrderException("Đơn hàng không được null");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new EmptyOrderException("Đơn hàng không có món nào");
        }
    }

    // Kiểm tra mã giảm giá có hợp lệ không
    private void validateDiscount(Discount discount) throws InvalidDiscountException {
        if (discount == null) {
            throw new InvalidDiscountException("Mã giảm giá không được null");
        }
        if (!discount.isValid()) {
            if (!discount.isActive()) {
                throw new InvalidDiscountException("Mã giảm giá đã hết hạn: " + discount.getCode());
            }
            if (discount.getPercentage() < 0 || discount.getPercentage() > 100) {
                throw new InvalidDiscountException("Phần trăm giảm giá không hợp lệ: " + discount.getPercentage());
            }
            throw new InvalidDiscountException("Mã giảm giá không hợp lệ: " + discount.getCode());
        }
    }

    // Helper Methods 

    private String formatMoney(double amount) {
        return String.format("%,.0f VND", amount);
    }

    // Getters và Setters
    public double getServiceFeePercent() {
        return serviceFeePercent;
    }

    public void setServiceFeePercent(double serviceFeePercent) {
        this.serviceFeePercent = serviceFeePercent;
    }
}