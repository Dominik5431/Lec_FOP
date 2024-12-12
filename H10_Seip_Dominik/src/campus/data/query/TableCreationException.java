package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class TableCreationException extends RuntimeException {
    private static final long serialVersionUID = 9187403963238866146L;

    public TableCreationException(String message) {
        super(message);
    }

    public TableCreationException(Throwable cause) {
        super(cause);
    }
}
