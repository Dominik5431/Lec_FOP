package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class TableAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = -598216925560093415L;

    public TableAlreadyExistsException(String tableName) {
        super("Die Tabelle " + tableName + " existiert bereits");
    }
}
