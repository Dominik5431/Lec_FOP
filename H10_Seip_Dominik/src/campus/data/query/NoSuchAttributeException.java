package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class NoSuchAttributeException extends RuntimeException {
    private static final long serialVersionUID = 8172342674212851061L;

    private final String attributeName;

    public NoSuchAttributeException(String attributeName) {
        super("Attribute " + attributeName + " does not exist");
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
