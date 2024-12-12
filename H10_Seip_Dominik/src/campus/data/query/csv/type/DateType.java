package campus.data.query.csv.type;

import java.util.Date;
import java.util.Locale;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import campus.data.query.AttributeFormatException;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class DateType implements CSVAttributeType<Date> {
    private static final DateFormat format =
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    @Override
    public Date read(String value) throws AttributeFormatException {
        try {
            return format.parse(value);
        } catch (ParseException e) {
            throw new AttributeFormatException();
        }
    }

    @Override
    public String write(Date value) {
        return format.format(value);
    }
}
