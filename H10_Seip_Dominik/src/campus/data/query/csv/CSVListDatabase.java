package campus.data.query.csv;

import java.util.Map;

import java.io.IOException;

import campus.data.query.Database;
import campus.data.query.Table;
import campus.data.query.Attribute;

import campus.data.query.NoSuchTableException;
import campus.data.query.TableAlreadyExistsException;
import campus.data.query.TableCreationException;

import campus.data.query.csv.io.CSVListAdapter;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class CSVListDatabase implements Database {
    private final Map<String, CSVListAdapter> adapters;
    private final CSVTableTypeMap typeMap;

    public CSVListDatabase(Map<String, CSVListAdapter> adapters,
                           CSVTableTypeMap typeMap) {
        this.adapters = adapters;
        this.typeMap = typeMap;
    }

    @Override
    public void createTable(String tableName, boolean ignoreIfExists,
                            Attribute... attributes) {
        if (adapters.containsKey(tableName) && !ignoreIfExists) {
            throw new TableAlreadyExistsException(tableName);
        } else if (!adapters.containsKey(tableName)) {
            try {
                CSVTable.create(adapters.get(tableName), attributes, typeMap);
            } catch (IOException e) {
                throw new TableCreationException(e);
            }
        }
    }

    @Override
    public Table from(String tableName) {
        try {
            return CSVTable.fromFile(adapters.get(tableName), typeMap);
        } catch (IOException e) {
            throw new NoSuchTableException(tableName);
        }
    }
}
