package campus.data.query.csv.type;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface CSVAttributeType<T> {
    T read(String value);
    String write(T value);
}
