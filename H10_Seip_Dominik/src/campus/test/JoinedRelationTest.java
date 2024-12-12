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

import campus.data.query.JoinedRelation;
import campus.data.query.NoSuchAttributeException;
import campus.data.query.Relation;
import campus.data.query.Tuple;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class JoinedRelationTest {
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
        Relation left = () -> Stream.of(
            new MockTuple() {
                @Override
                public Set<String> getAttributeNames() {
                    var set = new HashSet<String>();
                    set.add("column1");
                    set.add("column2");
                    return set;
                }

                @Override
                public Object getValue(String attributeName) {
                    return "cell1";
                }
            });

        Relation right = () -> Stream.of(
            new MockTuple() {
                @Override
                public Set<String> getAttributeNames() {
                    var set = new HashSet<String>();
                    set.add("column2");
                    set.add("column3");
                    return set;
                }

                @Override
                public Object getValue(String attributeName) {
                    return "cell1";
                }
            });

        new JoinedRelation(left, right, "column1", "column3")
            .select().findFirst().ifPresentOrElse(t -> {
                var attributeNames = t.getAttributeNames();
                assertEquals(
                    Set.of("column1", "column2", "column3"),
                    attributeNames);
                assertThat(() -> attributeNames.add("column4"),
                    describedAs("Das Ergebnis von getAttributeNames() darf"
                            + " nicht veränderbar sein",
                        throwsOfType(UnsupportedOperationException.class)));
                assertThat(() -> attributeNames.remove("column4"),
                    describedAs("Das Ergebnis von getAttributeNames() darf"
                            + " nicht veränderbar sein",
                        throwsOfType(UnsupportedOperationException.class)));
            }, JoinedRelationTest::failOnEmptySelection);
    }

    @Test
    void testGetValue() {
        Relation left = () -> Stream.of(
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

        Relation right = () -> Stream.of(
            new MockTuple() {
                @Override
                public Set<String> getAttributeNames() {
                    return Set.of("column2", "column3");
                }

                @Override
                public Object getValue(String attributeName) {
                    return attributeName.equals("column2") ? "cell3" : "cell1";
                }
            });

        new JoinedRelation(left, right, "column1", "column3")
            .select().findFirst().ifPresentOrElse(t -> {
                assertEquals("cell1", t.getValue("column1"));
                assertEquals("cell2", t.getValue("column2"));
                assertEquals("cell1", t.getValue("column3"));

                assertThat(() -> t.getValue("column4"),
                    throwsOfType(NoSuchAttributeException.class,
                        withMessage("Attribute column4 does not exist")));
            }, JoinedRelationTest::failOnEmptySelection);
    }

    @Test
    void testProject() {
        var projectedLeftTuple = new MockTuple() {
            @Override
            public Set<String> getAttributeNames() {
                return Set.of("column1", "column2");
            }

            @Override
            public Object getValue(String attributeName) {
                return attributeName.equals("column1") ? "cell1" : "cell2";
            }
        };

        var projectedRightTuple = new MockTuple() {
            @Override
            public Set<String> getAttributeNames() {
                return Set.of("column2");
            }

            @Override
            public Object getValue(String attributeName) {
                return "cell3";
            }
        };

        Relation left = () -> Stream.of(
            new MockTuple() {
                @Override
                public Set<String> getAttributeNames() {
                    return Set.of("column1", "column2");
                }

                @Override
                public Object getValue(String attributeName) {
                    return attributeName.equals("column1") ? "cell1" : "cell2";
                }

                @Override
                public Tuple project(List<String> attributeNames) {
                    return projectedLeftTuple;
                }
            });

        Relation right = () -> Stream.of(
            new MockTuple() {
                @Override
                public Set<String> getAttributeNames() {
                    return Set.of("column2", "column3");
                }

                @Override
                public Object getValue(String attributeName) {
                    return attributeName.equals("column2") ? "cell3" : "cell1";
                }

                @Override
                public Tuple project(List<String> attributeNames) {
                    return projectedRightTuple;
                }
            });

        new JoinedRelation(left, right, "column1", "column3")
            .select().findFirst().ifPresentOrElse(t -> {
                assertThat(() -> t.project(List.of("column1", "column4")),
                    throwsOfType(NoSuchAttributeException.class,
                        withMessage("Attribute column4 does not exist")));

                var projected = t.project(List.of("column1", "column2"));

                assertEquals(
                    Set.of("column1", "column2"),
                    projected.getAttributeNames());

                assertEquals("cell1", projected.getValue("column1"));
                assertEquals("cell2", projected.getValue("column2"));

                assertThat(() -> projected.getValue("column3"),
                    throwsOfType(NoSuchAttributeException.class,
                        withMessage("Attribute column3 does not exist")));
            }, JoinedRelationTest::failOnEmptySelection);
    }

    @Test
    void testSelect() {
        class LeftTuple extends MockTuple {
            @Override
            public Set<String> getAttributeNames() {
                return Set.of("column1", "column2");
            }
        }

        class RightTuple extends MockTuple {
            @Override
            public Set<String> getAttributeNames() {
                return Set.of("column3", "column4", "column5");
            }
        }

        Relation left = () -> Stream.of(
            new LeftTuple() {
                @Override
                public Object getValue(String attributeName) {
                    return attributeName.equals("column1") ? "cell1" : "cell2";
                }
            },
            new LeftTuple() {
                @Override
                public Object getValue(String attributeName) {
                    return attributeName.equals("column1") ? "cell3" : "cell4";
                }
            },
            new LeftTuple() {
                @Override
                public Object getValue(String attributeName) {
                    return attributeName.equals("column1") ? "cell5" : "cell4";
                }
            });

        Relation right = () -> Stream.of(
            new RightTuple() {
                @Override
                public Object getValue(String attributeName) {
                    switch (attributeName) {
                        case "column3": return "cell7";
                        case "column4": return "cell2";
                        default: return "cell9";
                    }
                }
            },
            new RightTuple() {
                @Override
                public Object getValue(String attributeName) {
                    switch (attributeName) {
                        case "column3": return "cell10";
                        case "column4": return "cell4";
                        default: return "cell12";
                    }
                }
            },
            new RightTuple() {
                @Override
                public Object getValue(String attributeName) {
                    switch (attributeName) {
                        case "column3": return "cell13";
                        case "column4": return "cell4";
                        default: return "cell15";
                    }
                }
            },
            new RightTuple() {
                @Override
                public Object getValue(String attributeName) {
                    switch (attributeName) {
                        case "column3": return "cell16";
                        case "column4": return "cell17";
                        default: return "cell18";
                    }
                }
            });

        var joined = new JoinedRelation(left, right, "column2", "column4");
        var tuples = joined.select().collect(Collectors.toList());

        assertEquals(5, tuples.size());

        assertTrue(tuples.stream().anyMatch(tuple ->
            tuple.getValue("column1").equals("cell1") &&
            tuple.getValue("column2").equals("cell2") &&
            tuple.getValue("column3").equals("cell7") &&
            tuple.getValue("column4").equals("cell2") &&
            tuple.getValue("column5").equals("cell9")));

        assertTrue(tuples.stream().anyMatch(tuple ->
            tuple.getValue("column1").equals("cell3") &&
            tuple.getValue("column2").equals("cell4") &&
            tuple.getValue("column3").equals("cell10") &&
            tuple.getValue("column4").equals("cell4") &&
            tuple.getValue("column5").equals("cell12")));

        assertTrue(tuples.stream().anyMatch(tuple ->
            tuple.getValue("column1").equals("cell3") &&
            tuple.getValue("column2").equals("cell4") &&
            tuple.getValue("column3").equals("cell13") &&
            tuple.getValue("column4").equals("cell4") &&
            tuple.getValue("column5").equals("cell15")));

        assertTrue(tuples.stream().anyMatch(tuple ->
            tuple.getValue("column1").equals("cell5") &&
            tuple.getValue("column2").equals("cell4") &&
            tuple.getValue("column3").equals("cell10") &&
            tuple.getValue("column4").equals("cell4") &&
            tuple.getValue("column5").equals("cell12")));

        assertTrue(tuples.stream().anyMatch(tuple ->
            tuple.getValue("column1").equals("cell5") &&
            tuple.getValue("column2").equals("cell4") &&
            tuple.getValue("column3").equals("cell13") &&
            tuple.getValue("column4").equals("cell4") &&
            tuple.getValue("column5").equals("cell15")));
    }
}
