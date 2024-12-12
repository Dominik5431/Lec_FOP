package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class NoSuchTypeException extends RuntimeException {
    private static final long serialVersionUID = -6556662120258442372L;

    public NoSuchTypeException(String type) {
        super("Type " + type + " does not exist");
    }

    public NoSuchTypeException(Class<?> type) {
        this(type.toString());
    }
}
