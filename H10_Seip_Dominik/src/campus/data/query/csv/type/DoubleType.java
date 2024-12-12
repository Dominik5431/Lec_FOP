package campus.data.query.csv.type;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class DoubleType implements CSVAttributeType<Double> {
    @Override
    public Double read(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public String write(Double value) {
        return String.valueOf(value);
    }
}
