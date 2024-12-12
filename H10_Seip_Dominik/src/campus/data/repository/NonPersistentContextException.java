package campus.data.repository;

import campus.data.domain.Entity;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class NonPersistentContextException extends RuntimeException {
    private static final long serialVersionUID = 4549010061457093000L;

    private final Entity entity;

    public NonPersistentContextException(Entity entity) {
        super(String.format(
            "Entity %s is not yet stored in a database",
            entity));

        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
