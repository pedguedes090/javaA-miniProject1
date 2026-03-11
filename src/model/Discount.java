package model;

public class Discount {

    private String code;           // mã giảm giá 
    private double percentage;     // phần trăm giảm (0-100)
    private boolean active;        // trạng thái còn hiệu lực

    public Discount(String code, double percentage, boolean active) {
        this.code = code;
        this.percentage = percentage;
        this.active = active;
    }

    /**
     * Kiểm tra mã giảm giá có hợp lệ không
     * - Mã không được null hoặc rỗng
     * - Phần trăm phải trong khoảng 0-100
     * - Phải còn hiệu lực
     */
    public boolean isValid() {
        return code != null 
                && !code.trim().isEmpty() 
                && percentage >= 0 
                && percentage <= 100 
                && active;
    }

    /**
     * Tính số tiền được giảm
     * @param amount số tiền gốc
     * @return số tiền được giảm
     */
    public double calculateDiscountAmount(double amount) {
        if (!isValid()) {
            return 0;
        }
        return amount * percentage / 100;
    }

    // Getters và Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "code='" + code + '\'' +
                ", percentage=" + percentage + "%" +
                ", active=" + active +
                '}';
    }
}
