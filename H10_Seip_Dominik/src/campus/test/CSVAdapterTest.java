package campus.test;

import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Path;

import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import campus.data.query.csv.io.CSVFileAdapter;
import campus.data.query.csv.io.CSVFormatException;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class CSVAdapterTest {
    static Path csvFileDirectory;

    @BeforeAll
    static void setupFileDirectory() throws URISyntaxException {
        csvFileDirectory = new File(
            CSVAdapterTest.class.getResource("csv-files").toURI()).toPath();
    }

    @Test
    void testReadingFile() throws IOException {
        var csvFile = csvFileDirectory.resolve("table.csv").toFile();
        var adapter = new CSVFileAdapter(csvFile);

        assertArrayEquals(new String[] {
            "Stunde", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"
        }, adapter.getHeaders());

        try (var rows = adapter.readRows()) {
            assertArrayEquals(new String[][] {
                {"1", "Mathematik", "Deutsch", "Englisch", "Erdkunde", "Informatik"},
                {"2", "Sport", "Deutsch", "Englisch", "Sport", ""},
                {"3", "Sport", "Religion (ev., kath.)", "Kunst", "", "Kunst"}
            }, rows.toArray(String[][]::new));
        }
    }

    @Test
    void testEmptyFile() throws IOException {
        var csvFile = csvFileDirectory.resolve("empty.csv").toFile();
        var adapter = new CSVFileAdapter(csvFile);

        var exception = assertThrows(
            CSVFormatException.class, () -> adapter.getHeaders());
        assertEquals("CSV file may not be empty", exception.getMessage());

        try (var rows = adapter.readRows()) {
            assertEquals(0, rows.count());
        }
    }

    @Test
    void testNewFile() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        adapter.setHeaders("Column1", "Column2");

        assertArrayEquals(
            new String[] {"Column1", "Column2"}, adapter.getHeaders());

        try (var rows = adapter.readRows()) {
            assertEquals(0, rows.count());
        }

        csvFile.delete();
    }

    @Test
    void testAppendingRows() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        adapter.setHeaders("Column1", "Column2");

        assertArrayEquals(
            new String[] {"Column1", "Column2"}, adapter.getHeaders());

        try (var rows = adapter.readRows()) {
            assertEquals(0, rows.count());
        }

        adapter.appendRow("Cell 1", "Cell 2");

        try (var rows = adapter.readRows()) {
            assertArrayEquals(new String[][] {
                {"Cell 1", "Cell 2"}
            }, rows.toArray(String[][]::new));
        }

        adapter.appendRow("Cell 3", "Cell 4");

        try (var rows = adapter.readRows()) {
            assertArrayEquals(new String[][] {
                {"Cell 1", "Cell 2"},
                {"Cell 3", "Cell 4"}
            }, rows.toArray(String[][]::new));
        }

        csvFile.delete();
    }

    @Test
    void testWritingRows() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        adapter.setHeaders("Column1", "Column2", "Column3");

        assertArrayEquals(
            new String[] {"Column1", "Column2", "Column3"},
            adapter.getHeaders());

        try (var rows = adapter.readRows()) {
            assertEquals(0, rows.count());
        }

        var newRows = new ArrayList<String[]>();
        newRows.add(new String[] {"Cell 1", "Cell 2", "Cell 3"});
        newRows.add(new String[] {"Cell 4", "Cell 5", "Cell 6"});

        adapter.writeRows(newRows.stream());

        assertArrayEquals(
            new String[] {"Column1", "Column2", "Column3"},
            adapter.getHeaders());

        try (var rows = adapter.readRows()) {
            assertArrayEquals(newRows.toArray(), rows.toArray(String[][]::new));
        }

        csvFile.delete();
    }

    @Test
    void testPipelining() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        try (var fileWriter = new FileWriter(csvFile);
             var bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Column1 ; Column2 ; Column3");
            bufferedWriter.newLine();
            bufferedWriter.write(" Cell 1 ;  Cell 2 ;    true");
            bufferedWriter.newLine();
            bufferedWriter.write(" Cell 4 ;  Cell 5 ;   false");
            bufferedWriter.newLine();
            bufferedWriter.write(" Cell 7 ;  Cell 8 ;    true");
            bufferedWriter.newLine();
            bufferedWriter.write("Cell 10 ; Cell 11 ;    true");
            bufferedWriter.newLine();
            bufferedWriter.write("Cell 13 ; Cell 14 ;   false");
            bufferedWriter.newLine();
        }

        var adapter = new CSVFileAdapter(csvFile);

        try (var rows = adapter.readRows()) {
            adapter.writeRows(rows.filter(row -> row[2].equals("true")));
        }

        assertArrayEquals(
            new String[] {"Column1", "Column2", "Column3"},
            adapter.getHeaders());

        try (var rows = adapter.readRows()) {
            assertArrayEquals(new String[][] {
                {"Cell 1", "Cell 2", "true"},
                {"Cell 7", "Cell 8", "true"},
                {"Cell 10", "Cell 11", "true"}
            }, rows.toArray(String[][]::new));
        }

        csvFile.delete();
    }

    @Test
    void testWritingRowsReplacesOldRows() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        adapter.setHeaders("Column1", "Column2", "Column3");

        assertArrayEquals(
            new String[] {"Column1", "Column2", "Column3"},
            adapter.getHeaders());

        try (var rows = adapter.readRows()) {
            assertEquals(0, rows.count());
        }

        var newRows = new ArrayList<String[]>();
        newRows.add(new String[] {"Cell 1", "Cell 2", "Cell 3"});
        newRows.add(new String[] {"Cell 4", "Cell 5", "Cell 6"});

        adapter.writeRows(newRows.stream());

        try (var rows = adapter.readRows()) {
            assertArrayEquals(newRows.toArray(), rows.toArray(String[][]::new));
        }

        adapter.appendRow("Cell 7", "Cell 8", "Cell 9");

        adapter.writeRows(newRows.stream());

        try (var rows = adapter.readRows()) {
        assertArrayEquals(
            newRows.toArray(), rows.toArray(String[][]::new));
        }

        csvFile.delete();
    }

    @Test
    void testSettingHeadersReplacesOldRows() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        adapter.setHeaders("Column1", "Column2", "Column3");

        assertArrayEquals(
            new String[] {"Column1", "Column2", "Column3"},
            adapter.getHeaders());

        try (var rows = adapter.readRows()) {
            assertEquals(0, rows.count());
        }

        var newRows = new ArrayList<String[]>();
        newRows.add(new String[] {"Cell 1", "Cell 2", "Cell 3"});
        newRows.add(new String[] {"Cell 4", "Cell 5", "Cell 6"});

        adapter.writeRows(newRows.stream());

        try (var rows = adapter.readRows()) {
            assertArrayEquals(newRows.toArray(), rows.toArray(String[][]::new));
        }

        adapter.setHeaders("Column5", "Column6");

        try (var rows = adapter.readRows()) {
            assertEquals(0, rows.count());
        }

        csvFile.delete();
    }

    @Test
    void testIllegalHeader() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        var exception = assertThrows(
            CSVFormatException.class,
            () -> adapter.setHeaders("Column1", "Column;2"));
        assertEquals("Illegal symbol: ;", exception.getMessage());

        csvFile.delete();
    }

    @Test
    void testAppendingIllegalRow() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        adapter.setHeaders("Column 1", "Column 2");
        var exception = assertThrows(
            CSVFormatException.class,
            () -> adapter.appendRow("Cell 1", "Cell 2;"));
        assertEquals("Illegal symbol: ;", exception.getMessage());

        csvFile.delete();
    }

    @Test
    void testAppendingRowWithInconsistenWidth() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        adapter.setHeaders("Column 1", "Column 2");

        var exception = assertThrows(
            CSVFormatException.class,
            () -> adapter.appendRow("Cell 1", "Cell 2", "Cell 3"));
        assertEquals("Inconsistent row width: 3", exception.getMessage());

        csvFile.delete();
    }

    @Test
    void testWritingRowWithIllegalSymbol() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        adapter.setHeaders("Column 1", "Column 2");

        var rows = new ArrayList<String[]>();
        rows.add(new String[] {"Cell 1", "Cell 2"});
        rows.add(new String[] {";Cell 4", "Cell 5"});

        var exception = assertThrows(
            CSVFormatException.class,
            () -> adapter.writeRows(rows.stream()));
        assertEquals("Illegal symbol: ;", exception.getMessage());

        csvFile.delete();
    }

    @Test
    void testWritingRowWithInconsistenWidth() throws IOException {
        var csvFile = csvFileDirectory.resolve("new.csv").toFile();

        var adapter = new CSVFileAdapter(csvFile);
        adapter.setHeaders("Column 1", "Column 2", "Column 3");

        var rows = new ArrayList<String[]>();
        rows.add(new String[] {"Cell 1", "Cell 2", "Cell 3"});
        rows.add(new String[] {"Cell 4", "Cell 5", "Cell 6", "Cell 7"});
        rows.add(new String[] {"Cell 8", "Cell 9", "Cell 10"});

        var exception = assertThrows(
            CSVFormatException.class,
            () -> adapter.writeRows(rows.stream()));
        assertEquals("Inconsistent row width: 4", exception.getMessage());

        csvFile.delete();
    }
}
