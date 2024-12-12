package campus.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.MatcherAssert.assertThat;

import static campus.test.matcher.ThrowsOfType.throwsOfType;
import static campus.test.matcher.WithMessage.withMessage;

import campus.data.query.AliasRelation;
import campus.data.query.NoSuchAttributeException;
import campus.data.query.Relation;
import campus.data.query.Tuple;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class AliasRelationTest {
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

    static void failOnEmptySelection() {
        fail("Leere Tupelmenge. "
            + "Ist select() falsch oder noch nicht implementiert?");
    }

    @Test
    void testGetAttributeNames() {
        Relation relation = () -> Stream.of(
            new MockTuple() {
                @Override
                public Set<String> getAttributeNames() {
                    var set = new HashSet<String>();
                    set.add("column1");
                    set.add("column2");
                    return set;
                }
            });

        new AliasRelation(relation, "table1")
            .select().findFirst().ifPresentOrElse(t -> {
                var attributeNames = t.getAttributeNames();
                assertEquals(
                    Set.of("table1.column1", "table1.column2"),
                    attributeNames);
                assertThat(() -> attributeNames.add("column4"),
                    describedAs("Das Ergebnis von getAttributeNames() darf"
                            + " nicht veränderbar sein",
                        throwsOfType(UnsupportedOperationException.class)));
                assertThat(() -> attributeNames.remove("column4"),
                    describedAs("Das Ergebnis von getAttributeNames() darf"
                            + " nicht veränderbar sein",
                        throwsOfType(UnsupportedOperationException.class)));
            }, AliasRelationTest::failOnEmptySelection);
    }

    @Test
    void testGetValue() {
        Relation relation = () -> Stream.of(
            new MockTuple() {
                @Override
                public Set<String> getAttributeNames() {
                    return Set.of("column1", "column2");
                }

                @Override
                public Object getValue(String attributeName) {
                    return attributeName.equals("column1") ? "cell1" : "cell2";
                }
            });

        new AliasRelation(relation, "table1")
            .select().findFirst().ifPresentOrElse(t -> {
                assertEquals("cell1", t.getValue("table1.column1"));
                assertEquals("cell2", t.getValue("table1.column2"));

                assertThat(() -> t.getValue("column1"),
                    throwsOfType(NoSuchAttributeException.class,
                        withMessage("Attribute column1 does not exist")));
            }, AliasRelationTest::failOnEmptySelection);
    }

//    @Test
//    void testProject() {
//        var projectedTuple = new MockTuple() {
//            @Override
//            public Set<String> getAttributeNames() {
//                return Set.of("column1", "column2");
//            }
//
//            @Override
//            public Object getValue(String attributeName) {
//                return attributeName.equals("column1") ? "cell1" : "cell2";
//            }
//        };
//
//        Relation relation = () -> Stream.of(
//            new MockTuple() {
//                @Override
//                public Set<String> getAttributeNames() {
//                    return Set.of("column1", "column2", "column3");
//                }
//
//                @Override
//                public Tuple project(List<String> attributeNames) {
//                    return projectedTuple;
//                }
//            });
//
//        new AliasRelation(relation, "table1")
//            .select().findFirst().ifPresentOrElse(t -> {
//                assertThat(() ->
//                        t.project(List.of("table1.column1", "column2")),
//                    throwsOfType(NoSuchAttributeException.class,
//                        withMessage("Attribute column2 does not exist")));
//
//                assertThat(() ->
//                        t.project(List.of("table1.column1", "table1.column4")),
//                    throwsOfType(NoSuchAttributeException.class,
//                        withMessage(
//                            "Attribute table1.column4 does not exist")));
//
//                assertThat(() ->
//                        t.project(List.of("table1.column1", "table2.column2")),
//                    throwsOfType(NoSuchAttributeException.class,
//                        withMessage(
//                            "Attribute table2.column2 does not exist")));
//
//                var renamed = t.project(
//                    List.of("table1.column1", "table1.column2"));
//                assertEquals(
//                    Set.of("table1.column1", "table1.column2"),
//                    renamed.getAttributeNames());
//                assertEquals("cell1", renamed.getValue("table1.column1"));
//                assertEquals("cell2", renamed.getValue("table1.column2"));
//
//                assertThat(() -> renamed.getValue("column2"),
//                    throwsOfType(NoSuchAttributeException.class,
//                        withMessage("Attribute column2 does not exist")));
//            }, AliasRelationTest::failOnEmptySelection);
//    }
//
//    @Test
//    void testRenaming() {
//        class TestTuple extends MockTuple {
//            @Override
//            public Set<String> getAttributeNames() {
//                return Set.of("column1", "test.column2");
//            }
//        }
//
//        Relation relation = () -> Stream.of(
//            new TestTuple() {
//                @Override
//                public Object getValue(String attributeName) {
//                    return attributeName.equals("column1") ? "cell1" : "cell2";
//                }
//            },
//            new TestTuple() {
//                @Override
//                public Object getValue(String attributeName) {
//                    return attributeName.equals("column1") ? "cell3" : "cell4";
//                }
//            },
//            new TestTuple() {
//                @Override
//                public Object getValue(String attributeName) {
//                    return attributeName.equals("column1") ? "cell5" : "cell6";
//                }
//            },
//            new TestTuple() {
//                @Override
//                public Object getValue(String attributeName) {
//                    return attributeName.equals("column1") ? "cell7" : "cell8";
//                }
//            });
//
//        var renamed = new AliasRelation(relation, "table");
//
//        var tuples = renamed.select().collect(Collectors.toList());
//
//        assertEquals(4, tuples.size());
//
//        assertTrue(tuples.stream().anyMatch(tuple ->
//            tuple.getValue("table.column1").equals("cell1") &&
//            tuple.getValue("table.test.column2").equals("cell2")));
//
//        assertTrue(tuples.stream().anyMatch(tuple ->
//            tuple.getValue("table.column1").equals("cell3") &&
//            tuple.getValue("table.test.column2").equals("cell4")));
//
//        assertTrue(tuples.stream().anyMatch(tuple ->
//            tuple.getValue("table.column1").equals("cell5") &&
//            tuple.getValue("table.test.column2").equals("cell6")));
//
//        assertTrue(tuples.stream().anyMatch(tuple ->
//            tuple.getValue("table.column1").equals("cell7") &&
//            tuple.getValue("table.test.column2").equals("cell8")));
//    }
}
