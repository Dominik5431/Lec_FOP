package campus.data.query.csv.type;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class LongType implements CSVAttributeType<Long> {
    @Override
    public Long read(String value) {
        return Long.parseLong(value);
    }

    @Override
    public String write(Long value) {
        return String.valueOf(value);
    }
}
