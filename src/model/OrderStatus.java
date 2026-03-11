package model;

// Trạng thái của đơn hàng
public enum OrderStatus {
    PENDING,   // chờ thanh toán
    PAID,      // đã thanh toán
    CANCELLED  // đã hủy
}