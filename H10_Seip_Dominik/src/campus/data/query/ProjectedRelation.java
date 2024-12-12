package campus.data.query;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Kim Berninger
 * @author ...
 * @version 1.0.2
 */
public class ProjectedRelation implements Relation {
    private final Relation relation;
    private final List<String> attributeNames;

    public ProjectedRelation(Relation relation, String... attributeNames) {
        this.relation = relation;
        this.attributeNames = Arrays.asList(attributeNames);
    }

    @Override
    public Stream<Tuple> select() {
        // TODO H1.1 Implementieren Sie diese Methode
        return relation.select().map((Tuple tup) -> tup.project(attributeNames));
    }
}
