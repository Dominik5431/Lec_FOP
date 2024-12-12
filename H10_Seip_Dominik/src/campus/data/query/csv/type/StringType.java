package campus.data.query.csv.type;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class StringType implements CSVAttributeType<String> {
    @Override
    public String read(String value) {
        return value;
    }

    @Override
    public String write(String value) {
        return value;
    }
}
