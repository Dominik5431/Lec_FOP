package campus.data.query;

import java.util.stream.Stream;

/**
 * Repräsentiert eine Relation als {@link java.util.stream.Stream} von Tupeln.
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface Relation {
    /**
     * Liefert die in dieser Relation enthaltenen Tupel als {@link Stream}.
     *
     * @return ein {@code Stream} mit den Tupeln, aus denen sich diese Relation
     *         zusammensetzt
     */
    Stream<Tuple> select();

    /**
     * Wendet eine Projektion auf diese Relation an.
     *
     * @param attributeNames die Namen der Attribute, die in der Projektion
     *                       erhalten bleiben sollen
     * @return die projizierte Relation
     */
    default Stream<Tuple> select(String... attributeNames) {
        return new ProjectedRelation(this, attributeNames).select();
    }

    default Relation join(Relation other, String onLeft, String onRight) {
        return new JoinedRelation(this, other, onLeft, onRight);
    }

    default Relation as(String alias) {
        return new AliasRelation(this, alias);
    }
}
