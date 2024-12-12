package campus.data.query.csv;

import java.io.File;

import campus.data.query.Database;
import campus.data.query.DatabaseDriver;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class CSVDatabaseDriver implements DatabaseDriver {
    private final CSVTableTypeMap typeMap;

    public CSVDatabaseDriver(CSVTableTypeMap typeMap) {
        this.typeMap = typeMap;
    }

    @Override
    public Database connect(String address, boolean create) {
        return CSVDatabase.connect(new File(address), typeMap, create);
    }
}
