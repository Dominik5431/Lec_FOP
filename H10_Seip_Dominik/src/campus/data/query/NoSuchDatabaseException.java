package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class NoSuchDatabaseException extends RuntimeException {
    private static final long serialVersionUID = 3513556151121986169L;

    public NoSuchDatabaseException(String name) {
        super("Database " + name + " does not exist");
    }
}
