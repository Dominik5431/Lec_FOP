package campus.data.query.csv.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.io.IOException;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class CSVListAdapter implements CSVAdapter {
    private String[] headers;
    private final List<String[]> rows;

    public CSVListAdapter(String[] headers) {
        this(headers, new ArrayList<>());
    }

    public CSVListAdapter(String[] headers, List<String[]> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public List<String[]> getRows() {
        return rows;
    }

    @Override
    public void setHeaders(String... headers) throws IOException {
        this.headers = Arrays.copyOf(headers, headers.length);
        rows.clear();
    }

    @Override
    public String[] getHeaders() throws IOException {
        return Arrays.copyOf(headers, headers.length);
    }

    @Override
    public Stream<String[]> readRows() throws IOException {
        return rows.stream().map(row -> Arrays.copyOf(row, row.length));
    }

    @Override
    public void writeRows(Stream<String[]> rows) throws IOException {
        var newRows = rows.collect(Collectors.toList());
        this.rows.clear();
        this.rows.addAll(newRows);
    }

    @Override
    public void appendRow(String... row) throws IOException {
        rows.add(Arrays.copyOf(row, row.length));
    }
}
