package campus.data.domain;

import java.util.Objects;
import java.util.Optional;

/**
 * Repräsentiert ein Objekt, das sich in einer Tabelle speichern und dort anhand
 * seiner ID eindeutig identifizieren lässt.
 * <br>
 * Die ID muss nicht vorhanden sein. Dieser Fall ist so zu interpretieren, dass
 * die entsprechende Entität noch nicht in einer Tabelle gespeichert wurde.
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
public abstract class Entity {
    private Long id;

    /**
     * Liefert die ID dieser Entität, falls vorhanden.
     *
     * @return ein {@code Optional} mit der ID, falls diese vorhanden ist,
     *         ansonsten {@code Optional.empty()}
     */
    public Optional<Long> getID() {
        return Optional.ofNullable(id);
    }

    /**
     * Setzt die ID dieser Entität auf den übergebenen Wert.
     *
     * @param id die neue ID dieser Entität
     */
    public void setID(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Entity)) {
            return false;
        }

        return Objects.equals(id, ((Entity) obj).id);
    }
}
