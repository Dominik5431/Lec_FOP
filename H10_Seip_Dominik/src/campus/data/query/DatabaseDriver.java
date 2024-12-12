package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface DatabaseDriver {
    Database connect(String address, boolean create);

    default Database connect(String address) {
        return connect(address, false);
    }
}
