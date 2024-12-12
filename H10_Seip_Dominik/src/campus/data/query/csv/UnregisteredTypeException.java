package campus.data.query.csv;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class UnregisteredTypeException extends RuntimeException {
    private static final long serialVersionUID = 8429757135062592563L;

    public UnregisteredTypeException(Class<?> type) {
        super("Type " + type.getName() + " is not registered in type map");
    }

    public UnregisteredTypeException(String typeName) {
        super("No type with name " + typeName + " registered in type map");
    }
}
