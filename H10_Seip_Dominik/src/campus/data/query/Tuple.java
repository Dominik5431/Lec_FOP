package campus.data.query;

import java.util.List;
import java.util.Set;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface Tuple {
    Set<String> getAttributeNames();

    Tuple project(List<String> attributeNames);

    Object getValue(String attributeName);

    default Integer getInteger(String attributeName) {
        return (Integer) getValue(attributeName);
    }

    default Long getLong(String attributeName) {
        return (Long) getValue(attributeName);
    }

    default Double getDouble(String attributeName) {
        return (Double) getValue(attributeName);
    }

    default Boolean getBoolean(String attributeName) {
        return (Boolean) getValue(attributeName);
    }

    default String getString(String attributeName) {
        return (String) getValue(attributeName);
    }

    default boolean hasAttribute(String attributeName) {
        return getAttributeNames().contains(attributeName);
    }
}
