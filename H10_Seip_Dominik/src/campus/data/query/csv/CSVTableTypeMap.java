package campus.data.query.csv;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import campus.data.query.csv.type.BooleanType;
import campus.data.query.csv.type.CSVAttributeType;
import campus.data.query.csv.type.DateType;
import campus.data.query.csv.type.DoubleType;
import campus.data.query.csv.type.IntegerType;
import campus.data.query.csv.type.LongType;
import campus.data.query.csv.type.StringType;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class CSVTableTypeMap {
    private static final String NULL_VALUE = "NULL";

    private final Map<Class<?>, String> registeredTypeNames;
    private final Map<Class<?>, CSVAttributeType<?>> registeredTypes;

    public CSVTableTypeMap() {
        registeredTypeNames = new HashMap<>();
        registeredTypes = new HashMap<>();
    }

    public <T> CSVTableTypeMap registerType(Class<T> type, String typeName, CSVAttributeType<T> attributeType) {
        if (registeredTypeNames.containsKey(type)) {
            throw new TypeAlreadyRegisteredException(type);
        }
        if (registeredTypeNames.containsValue(typeName)) {
            throw new TypeAlreadyRegisteredException(typeName);
        }
        if (!typeName.matches("\\w+")) {
            throw new IllegalTypeNameException(typeName);
        }
        registeredTypeNames.put(type, typeName.toUpperCase());
        registeredTypes.put(type, attributeType);
        return this;
    }

    String nameForType(Class<?> type) {
        return Optional.ofNullable(registeredTypeNames.get(type))
            .orElseThrow(() -> new UnregisteredTypeException(type));
    }

    Class<?> typeForName(String typeName) {
        return registeredTypeNames.entrySet().stream()
            .filter(entry -> entry.getValue().equals(typeName))
            .findFirst()
            .orElseThrow(() -> new UnregisteredTypeException(typeName))
            .getKey();
    }

    @SuppressWarnings("unchecked")
    private <T> CSVAttributeType<T> attributeTypeForType(Class<T> type) {
        return (CSVAttributeType<T>) Optional
            .ofNullable(registeredTypes.get(type))
            .orElseThrow(() -> new UnregisteredTypeException(type));
    }

    <T> T readValue(String value, Class<T> type) {
        if (value.equalsIgnoreCase(NULL_VALUE)) {
            return null;
        }
        return attributeTypeForType(type).read(value);
    }

    <T> String writeValue(Object value, Class<T> type) {
        if (value == null) {
            return NULL_VALUE;
        }
        return attributeTypeForType(type).write(type.cast(value));
    }

    public static CSVTableTypeMap defaultTypeMap() {
        return new CSVTableTypeMap()
            .registerType(Integer.class, "INTEGER", new IntegerType())
            .registerType(Long.class, "LONG", new LongType())
            .registerType(Double.class, "DOUBLE", new DoubleType())
            .registerType(Boolean.class, "BOOLEAN", new BooleanType())
            .registerType(String.class, "STRING", new StringType())
            .registerType(Date.class, "DATETIME", new DateType());
    }
}
