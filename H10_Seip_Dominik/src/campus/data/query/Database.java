package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface Database {
    void createTable(
        String tableName, boolean ignoreIfExists, Attribute... attributes);

    Table from(String tableName);

    default Table into(String tableName) {
        return from(tableName);
    }
}
