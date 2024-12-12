package campus.data.query;

import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface Table extends Relation {
    Tuple insert(Map<String, ?> values);
    void update(Map<String, ?> newValues, Predicate<? super Tuple> predicate);
    void delete(Predicate<? super Tuple> predicate);
}
