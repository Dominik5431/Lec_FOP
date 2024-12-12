package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class UnsuitableDefaultValueException extends RuntimeException {
    private static final long serialVersionUID = -5563421366624914015L;

    public UnsuitableDefaultValueException(Object value, Class<?> type) {
        super(String.format("Unsuitable default value %s for attribute type %s",
            value, type.getName()));
    }
}
