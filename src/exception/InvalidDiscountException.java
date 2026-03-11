package exception;

/**
 * Exception khi mã giảm giá không hợp lệ
 * - Mã không tồn tại
 * - Mã đã hết hạn
 * - Phần trăm giảm không hợp lệ
 */
public class InvalidDiscountException extends Exception {
    public InvalidDiscountException(String message) {
        super(message);
    }

    public InvalidDiscountException(String message, Throwable cause) {
        super(message, cause);
    }
}