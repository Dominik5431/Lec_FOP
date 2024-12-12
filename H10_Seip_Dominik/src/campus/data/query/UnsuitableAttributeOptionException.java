package campus.data.query;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class UnsuitableAttributeOptionException extends RuntimeException {
    private static final long serialVersionUID = -8802052893644200638L;

    public UnsuitableAttributeOptionException(AttributeOption option, Class<?> type) {
        super(String.format("Unsuitable option %s for attribute type %s",
            option.name(), type.getName()));
    }
}
