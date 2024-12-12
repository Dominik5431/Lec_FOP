package campus.data.query.csv;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class TypeAlreadyRegisteredException extends RuntimeException {
    private static final long serialVersionUID = 4778517589279338030L;

    public TypeAlreadyRegisteredException(Class<?> type) {
        super("Type " + type.getName() + " is already registered in type map");
    }

    public TypeAlreadyRegisteredException(String typeName) {
        super("A type with name " + typeName + " is already registered in type map");
    }
}
