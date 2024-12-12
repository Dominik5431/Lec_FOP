package campus.data.query.csv;

import java.io.File;
import java.io.IOException;

import campus.data.query.Attribute;
import campus.data.query.Database;
import campus.data.query.NoSuchDatabaseException;
import campus.data.query.NoSuchTableException;
import campus.data.query.Table;
import campus.data.query.TableAlreadyExistsException;
import campus.data.query.TableCreationException;
import campus.data.query.csv.io.CSVFileAdapter;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class CSVDatabase implements Database {
    private final File tablesDirectory;
    private final CSVTableTypeMap typeMap;

    private CSVDatabase(File tablesDirectory, CSVTableTypeMap typeMap) {
        this.tablesDirectory = tablesDirectory;
        this.typeMap = typeMap;
    }

    static Database connect(File tablesDirectory, CSVTableTypeMap typeMap,
                            boolean create) {
        boolean success = true;
        if (create && !tablesDirectory.exists()) {
            success = tablesDirectory.mkdir();
        }

        if (!success || !tablesDirectory.isDirectory()) {
            throw new NoSuchDatabaseException(tablesDirectory.getName());
        }

        return new CSVDatabase(tablesDirectory, typeMap);
    }

    @Override
    public void createTable(String tableName, boolean ignoreIfExists,
                            Attribute... attributes) {
        String fileName = tableName + ".csv";
        File csvFile = tablesDirectory.toPath().resolve(fileName).toFile();

        if (csvFile.exists() && !ignoreIfExists) {
            throw new TableAlreadyExistsException(tableName);
        } else if (!csvFile.exists()) {
            try {
                CSVTable.create(
                        new CSVFileAdapter(csvFile), attributes, typeMap);
            } catch (IOException e) {
                throw new TableCreationException(e);
            }
        }
    }

    @Override
    public Table from(String tableName) {
        String fileName = tableName + ".csv";
        File csvFile = tablesDirectory.toPath().resolve(fileName).toFile();
        try {
            return CSVTable.fromFile(new CSVFileAdapter(csvFile), typeMap);
        } catch (IOException e) {
            throw new NoSuchTableException(tableName);
        }
    }
}
