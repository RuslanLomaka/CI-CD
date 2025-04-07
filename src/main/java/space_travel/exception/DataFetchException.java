package space_travel.exception;

public class DataFetchException extends RuntimeException {
    public DataFetchException(String message, Throwable cause) {
        super(message, cause);
    }
    public DataFetchException(String message) {
        super(message);
    }
}
