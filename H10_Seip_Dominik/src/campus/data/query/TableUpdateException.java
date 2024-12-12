package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class TableUpdateException extends RuntimeException {
    private static final long serialVersionUID = -9184874348019248109L;

    public TableUpdateException(String message) {
        super(message);
    }

    public TableUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
