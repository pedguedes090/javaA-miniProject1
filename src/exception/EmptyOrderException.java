package exception;

// Exception khi đơn hàng rỗng (không có món nào)
public class EmptyOrderException extends Exception {

    public EmptyOrderException(String message) {
        super(message);
    }

    public EmptyOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}