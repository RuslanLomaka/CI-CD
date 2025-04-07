package space_travel.exception;

public class DataUpdateException extends RuntimeException {
    public DataUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataUpdateException(String message) {
        super(message);
    }
}