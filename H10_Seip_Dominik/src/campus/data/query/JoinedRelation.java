package campus.data.query;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Kim Berninger
 * @author ...
 * @version 1.0.2
 */
public class JoinedRelation implements Relation {
    private final Relation left, right;
    private final String onLeft, onRight;

    private static class JoinedTuple implements Tuple {
        private final Tuple leftTuple, rightTuple;

        public JoinedTuple(Tuple leftTuple, Tuple rightTuple) {
            this.leftTuple = leftTuple;
            this.rightTuple = rightTuple;
        }

        @Override
        public Set<String> getAttributeNames() {
            // TODO H1.2 Implementieren Sie diese Methode
        	Set<String> result = Stream
        			.concat(leftTuple.getAttributeNames().stream(), rightTuple.getAttributeNames().stream())
        			.collect(Collectors.toUnmodifiableSet());
            return result;
        }

        @Override
        public Object getValue(String attributeName) throws NoSuchAttributeException {
            // TODO H1.2 Implementieren Sie diese Methode
        	if (leftTuple.hasAttribute(attributeName)) {
        		return leftTuple.getValue(attributeName);
        	}
        	if (rightTuple.hasAttribute(attributeName)) {
        		return rightTuple.getValue(attributeName);
        	}
            throw new NoSuchAttributeException(attributeName);
        }

        @Override
        public Tuple project(List<String> attributeNames) throws NoSuchAttributeException {
            // TODO H1.2 Implementieren Sie diese Methode
        	Map<Boolean, List<String>> left = attributeNames.stream().collect(Collectors.partitioningBy((String attr) -> leftTuple.hasAttribute(attr))); 
        	List<String> leftAttributes = left.get(true).stream().collect(Collectors.toList());
        	Stream<String> rightAttributesTemp = left.get(false).stream();
        	List<String> rightAttributes = rightAttributesTemp
        			.peek(attr -> {if (!rightTuple.hasAttribute(attr)) {throw new NoSuchAttributeException(attr);}})
        			.filter((String attr) -> rightTuple.hasAttribute(attr))
        			.collect(Collectors.toList());
        	return new JoinedTuple(leftTuple.project(leftAttributes), rightTuple.project(rightAttributes));
        }

    }

    public JoinedRelation(
        Relation left, Relation right, String onLeft, String onRight) {
        this.left = left;
        this.right = right;
        this.onLeft = onLeft;
        this.onRight = onRight;
    }

    @Override
    public Stream<Tuple> select() {
        // TODO H1.2 Implementieren Sie diese Methode
    	Stream<Tuple> result = left.select().flatMap(el -> right.select().map(el2 -> new JoinedTuple(el, el2)));
        return result.filter(tpl -> tpl.getValue(onLeft) == tpl.getValue(onRight));
    }
}
