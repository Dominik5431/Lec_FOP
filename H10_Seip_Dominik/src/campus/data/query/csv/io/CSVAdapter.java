package campus.data.query.csv.io;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface CSVAdapter {
    void setHeaders(String... headers) throws IOException;
    String[] getHeaders() throws IOException;

    Stream<String[]> readRows() throws IOException;
    void writeRows(Stream<String[]> rows) throws IOException;

    void appendRow(String... row) throws IOException;
}
