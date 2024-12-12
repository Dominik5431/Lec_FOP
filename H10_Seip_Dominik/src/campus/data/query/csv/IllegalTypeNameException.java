package campus.data.query.csv;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class IllegalTypeNameException extends RuntimeException {
    private static final long serialVersionUID = -2656378135590187607L;

    public IllegalTypeNameException(String typeName) {
        super(String.format("Illegal symbol %s in attribute name %s",
            typeName.replaceAll("\\w", "").charAt(0),
            typeName));
    }
}
