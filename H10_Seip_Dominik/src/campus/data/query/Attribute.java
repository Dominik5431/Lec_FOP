package campus.data.query;

import java.util.Objects;
import java.util.Set;

import static campus.data.query.AttributeOption.AUTOINCREMENT;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public final class Attribute {
    private final String name;
    private final Class<?> type;
    private final Object defaultValue;
    private final Set<AttributeOption> options;

    public Attribute(String name, Class<?> type, AttributeOption... options) {
        this(name, type, null, options);
    }

    public Attribute(String name, Class<?> type, Object defaultValue, AttributeOption... options) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);

        if (!name.matches("\\w+")) {
            throw new IllegalAttributeNameException(name);
        }

        if (defaultValue != null && !type.isInstance(defaultValue)) {
            throw new UnsuitableDefaultValueException(defaultValue, type);
        }

        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.options = Set.of(options);

        if (!type.equals(Long.class) &&
            this.options.contains(AUTOINCREMENT)) {
            throw new UnsuitableAttributeOptionException(AUTOINCREMENT, type);
        }
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public Set<AttributeOption> getOptions() {
        return options;
    }

    public boolean hasOption(AttributeOption option) {
        return options.contains(option);
    }
}
