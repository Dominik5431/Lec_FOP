package campus.data.query.csv.type;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class IntegerType implements CSVAttributeType<Integer> {
    @Override
    public Integer read(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public String write(Integer value) {
        return String.valueOf(value);
    }
}
