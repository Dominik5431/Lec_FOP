package campus.data.domain;

import campus.data.query.csv.type.CSVAttributeType;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class GradeType implements CSVAttributeType<Grade> {
    @Override
    public Grade read(String value) {
        return Grade.valueOf(value);
    }

    @Override
    public String write(Grade value) {
        return value.name();
    }
}
