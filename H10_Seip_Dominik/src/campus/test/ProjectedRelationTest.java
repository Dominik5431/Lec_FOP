package campus.test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import campus.data.query.ProjectedRelation;
import campus.data.query.Relation;
import campus.data.query.Tuple;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class ProjectedRelationTest {
    static class MockTuple implements Tuple {
        @Override
        public Set<String> getAttributeNames() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getValue(String attributeName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Tuple project(List<String> attributeNames) {
            throw new UnsupportedOperationException();
        }
    }

    class ProjectedTuple extends MockTuple {
        @Override
        public Set<String> getAttributeNames() {
            return Set.of("column1", "column2", "column3");
        }
    }

    @Test
    void testProjection() {
        Relation relation = () -> Stream.of(
            new MockTuple() {
                @Override
                public Tuple project(List<String> attributeNames) {
                    return new ProjectedTuple() {
                        @Override
                        public Object getValue(String attributeName) {
                            switch (attributeName) {
                                case "column1": return "cell1";
                                case "column2": return "cell2";
                                default: return "cell3";
                            }
                        }
                    };
                }
            },
            new MockTuple() {
                @Override
                public Tuple project(List<String> attributeNames) {
                    return new ProjectedTuple() {
                        @Override
                        public Object getValue(String attributeName) {
                            switch (attributeName) {
                                case "column1": return "cell4";
                                case "column2": return "cell5";
                                default: return "cell6";
                            }
                        }
                    };
                }
            },
            new MockTuple() {
                @Override
                public Tuple project(List<String> attributeNames) {
                    return new ProjectedTuple() {
                        @Override
                        public Object getValue(String attributeName) {
                            switch (attributeName) {
                                case "column1": return "cell7";
                                case "column2": return "cell8";
                                default: return "cell9";
                            }
                        }
                    };
                }
            });

        var projected = new ProjectedRelation(
            relation, "column1", "column2", "column3");

        var tuples = projected.select().collect(Collectors.toList());

        assertEquals(3, tuples.size());

        assertTrue(tuples.stream().anyMatch(tuple ->
            tuple.getValue("column1").equals("cell1") &&
            tuple.getValue("column2").equals("cell2") &&
            tuple.getValue("column3").equals("cell3")));

        assertTrue(tuples.stream().anyMatch(tuple ->
            tuple.getValue("column1").equals("cell4") &&
            tuple.getValue("column2").equals("cell5") &&
            tuple.getValue("column3").equals("cell6")));

        assertTrue(tuples.stream().anyMatch(tuple ->
            tuple.getValue("column1").equals("cell7") &&
            tuple.getValue("column2").equals("cell8") &&
            tuple.getValue("column3").equals("cell9")));
    }
}
