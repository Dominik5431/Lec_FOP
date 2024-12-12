package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class TableFetchException extends RuntimeException {
    private static final long serialVersionUID = -9184874348019248109L;

    public TableFetchException(String message) {
        super(message);
    }

    public TableFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
