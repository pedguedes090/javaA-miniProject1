package exception;

public class InvalidOrderIdException extends Exception {

    public InvalidOrderIdException(String message) {
        super(message);
    }

    public InvalidOrderIdException(String message, Throwable cause) {
        super(message, cause);
    }
}

