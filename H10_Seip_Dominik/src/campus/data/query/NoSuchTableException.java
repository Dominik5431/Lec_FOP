package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class NoSuchTableException extends RuntimeException {
    private static final long serialVersionUID = 814601063739565626L;

    public NoSuchTableException(String tableName) {
        super(String.format("Table %s does not exist", tableName));
    }
}
