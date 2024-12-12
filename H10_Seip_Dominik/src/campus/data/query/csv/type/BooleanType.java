package campus.data.query.csv.type;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class BooleanType implements CSVAttributeType<Boolean> {
    @Override
    public Boolean read(String value) {
        return Boolean.parseBoolean(value);
    }

    @Override
    public String write(Boolean value) {
        return String.valueOf(value);
    }
}
