package campus.data.query;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.util.Collections;

/**
 * @author Kim Berninger
 * @author ...
 * @version 1.0.2
 */
public class AliasRelation implements Relation {
    private final Relation relation;
    private final String alias;

    private class AliasTuple implements Tuple {
        private final Tuple tuple;

        public AliasTuple(Tuple tuple) {
            this.tuple = tuple;
        }

        @Override
        public Set<String> getAttributeNames() {
            // TODO H1.3 Implementieren Sie diese Methode
            return tuple.getAttributeNames().stream().map(name -> alias + "." + name).collect(Collectors.toUnmodifiableSet());
        }

        @Override
        public Object getValue(String attributeName) throws NoSuchAttributeException {
            // TODO H1.3 Implementieren Sie diese Methode
        	String[] parts = attributeName.split("\\.", 2);
        	String res = parts[parts.length - 1]; //Nicht Überschreiben
        	if (tuple.hasAttribute(attributeName)) {
        		return tuple.getValue(attributeName);
        		
        	} else {
        		throw new NoSuchAttributeException(res);
        	}
        }

        @Override
        public Tuple project(List<String> attributeNames) throws NoSuchAttributeException {
            // TODO H1.3 Implementieren Sie diese Methode
        	attributeNames = attributeNames.stream()
        				.peek(str -> {if (!str.startsWith(alias)){throw new NoSuchAttributeException(str);};})
        				.map((String str) -> str.split("\\.", 2)[1])
        				.peek(str -> {if (!tuple.hasAttribute(str)) {throw new NoSuchAttributeException(str);};})
        				.filter(str -> tuple.hasAttribute(str))
        				.collect(Collectors.toList());
            return tuple.project(attributeNames);
        }
    }

    public AliasRelation(Relation relation, String alias) {
        if (alias.contains(".")) {
            throw new IllegalArgumentException("Alias may not contain .");
        }
        this.relation = relation;
        this.alias = alias;
    }

    @Override
    public Stream<Tuple> select() {
        // TODO H1.3 Implementieren Sie diese Methode
        return relation.select().map(tpl -> new AliasTuple(tpl));
    }
}
