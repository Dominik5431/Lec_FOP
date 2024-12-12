package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class IllegalAttributeNameException extends RuntimeException {
    private static final long serialVersionUID = -2656378135590187607L;

    public IllegalAttributeNameException(String attributeName) {
        super(String.format("Illegal symbol %s in attribute name %s",
            attributeName.replaceAll("\\w", "").charAt(0),
            attributeName));
    }
}
