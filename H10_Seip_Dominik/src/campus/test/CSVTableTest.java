package campus.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import java.util.stream.Stream;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.MatcherAssert.assertThat;

import campus.data.query.Database;
import campus.data.query.NoSuchAttributeException;
import campus.data.query.TableFetchException;
import campus.data.query.TableUpdateException;

import campus.data.query.csv.CSVListDatabase;
import campus.data.query.csv.CSVTableTypeMap;

import campus.data.query.csv.io.CSVListAdapter;

import static campus.test.matcher.ThrowsOfType.throwsOfType;
import static campus.test.matcher.WithMessage.withMessage;
import static campus.test.matcher.WithCauseOfType.withCauseOfType;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class CSVTableTest {
    private Database database;
    private CSVListAdapter adapter;

    static void failOnEmptySelection() {
        fail("Leere Tupelmenge. "
            + "Ist select() falsch oder noch nicht implementiert?");
    }

    @BeforeEach
    void connectToDatabase() {
        String[] headers = {
            "column1:LONG (AUTOINCREMENT)",
            "column2: STRING (UNIQUE, NOTNULL)",
            "column3 :INTEGER",
            "column4:BOOLEAN[false](NOTNULL)",
            "column5:DOUBLE [1.0]"
        };
        var typeMap = CSVTableTypeMap.defaultTypeMap();
        adapter = new CSVListAdapter(headers);
        database = new CSVListDatabase(Map.of(
            "table1", adapter,
            "table2", new CSVListAdapter(headers) {
                @Override
                public Stream<String[]> readRows() throws IOException {
                    throw new IOException();
                }

                @Override
                public void appendRow(String... row) throws IOException {
                    throw new IOException();
                }

                @Override
                public void writeRows(Stream<String[]> rows)
                    throws IOException {
                    throw new IOException();
                }
            }), typeMap);
    }

    @Nested
    class TestCSVTuple {
        @Test
        void testGetAttributeNames() {
            adapter.getRows().add(new String[] {
                "9", "Foo", "1", "true", "3.5"
            });
            database.from("table1").select().findFirst()
                .ifPresentOrElse(tuple -> {
                    var attributeNames = tuple.getAttributeNames();
                    assertEquals(
                        Set.of(
                            "column1",
                            "column2",
                            "column3",
                            "column4",
                            "column5"),
                        attributeNames);
                    assertThat(() -> attributeNames.add("column6"),
                        describedAs("Das Ergebnis von getAttributeNames() darf"
                                + " nicht veränderbar sein",
                            throwsOfType(UnsupportedOperationException.class)));
                    assertThat(() -> attributeNames.remove("column6"),
                        describedAs("Das Ergebnis von getAttributeNames() darf"
                                + " nicht veränderbar sein",
                            throwsOfType(UnsupportedOperationException.class)));
            }, CSVTableTest::failOnEmptySelection);
        }

        @Test
        void testGetValue() {
            adapter.getRows().add(new String[] {
                "103", "Bar", "-16", "false", "-7.2"
            });
            database.from("table1").select().findFirst()
                .ifPresentOrElse(tuple -> {
                    assertEquals(103L, tuple.getValue("column1"));
                    assertEquals("Bar", tuple.getValue("column2"));
                    assertEquals(-16, tuple.getValue("column3"));
                    assertEquals(false, tuple.getValue("column4"));
                    assertEquals(-7.2, tuple.getValue("column5"));
            }, CSVTableTest::failOnEmptySelection);
        }

        @Test
        void testProject() {
            adapter.getRows().add(new String[] {
                "10", "Foo, Bar", "NULL", "false", "2.6"
            });
            database.from("table1").select().findFirst()
                .ifPresentOrElse(tuple -> {
                    assertThat(() ->
                            tuple.project(List.of("column1", "column6")),
                        throwsOfType(NoSuchAttributeException.class,
                            withMessage("Attribute column6 does not exist")));

                    var projected = tuple.project(
                        List.of("column1", "column2", "column4"));

                    assertEquals(
                        Set.of("column1", "column2", "column4"),
                        projected.getAttributeNames());

                    assertEquals(10L, projected.getValue("column1"));
                    assertEquals("Foo, Bar", projected.getValue("column2"));
                    assertEquals(false, projected.getValue("column4"));

                    assertThat(() -> projected.getValue("column3"),
                        throwsOfType(NoSuchAttributeException.class,
                            withMessage("Attribute column3 does not exist")));
            }, CSVTableTest::failOnEmptySelection);
        }
    }

    @Nested
    class TestSelect {
        @Test
        void testSelect() {
            String[][] targetRows = {
                {"10", "string1", "NULL", "false", "9.4"},
                {"2", "string2", "8", "true", "4.5"},
                {"123", "string3", "-100", "false", "NULL"},
                {"5", "string4", "910", "false", "70.6"}
            };

            Object[][] targetValues = {
                {10L, "string1", null, false, 9.4},
                {2L, "string2", 8, true, 4.5},
                {123L, "string3", -100, false, null},
                {5L, "string4", 910, false, 70.6}
            };

            adapter.getRows().addAll(Arrays.asList(targetRows));

            var actualRows = database.from("table1").select()
                .map(tuple -> new Object[] {
                    tuple.getLong("column1"),
                    tuple.getString("column2"),
                    tuple.getInteger("column3"),
                    tuple.getBoolean("column4"),
                    tuple.getDouble("column5")
                })
                .toArray(Object[][]::new);

            assertArrayEquals(targetValues, actualRows);
        }

        @Test
        void testSelectFails() {
            assertThat(() -> database.from("table2").select(),
                throwsOfType(TableFetchException.class,
                    allOf(
                        withMessage("Table file could not be read"),
                        withCauseOfType(IOException.class))));
        }
    }

    @Test
    void testInsertInEmptyTable() {
        var tuple = database.into("table1").insert(Map.of(
            "column2", "Bar",
            "column3", 9,
            "column4", true,
            "column5", 8.2));

        assertEquals(0L, tuple.getValue("column1"));
        assertEquals("Bar", tuple.getValue("column2"));
        assertEquals(9, tuple.getValue("column3"));
        assertEquals(true, tuple.getValue("column4"));
        assertEquals(8.2, tuple.getValue("column5"));

        assertEquals(1, adapter.getRows().size());
        assertArrayEquals(
            new String[] {"0", "Bar", "9", "true", "8.2"},
            adapter.getRows().get(0));
    }

    @Nested
    class TestInsert {
        private List<String[]> actualRows;
        private String[][] initialRows;

        @BeforeEach
        void fillTable() {
            initialRows = new String[][] {
                {"7", "foobar", "NULL", "false", "2.0"},
                {"2", "foobaz", "8", "true", "3.1"},
                {"123", "barfoo", "-100", "false", "NULL"},
                {"11", "bazfoo", "910", "false", "9.2"},
                {"29", "barbaz", "NULL", "true", "-10.2"},
                {"13", "foofoo", "160", "true", "0.1"},
                {"0", "baaaaaar", "-19", "false", "7.92"},
                {"5", "fuß", "314", "false", "12.89"}
            };

            actualRows = adapter.getRows();
            actualRows.addAll(Arrays.asList(initialRows));
        }

        @Nested
        class TestSuccessfulInsertions {
            @Test
            void insertWithAllValuesSpecified() {
                var tuple = database.into("table1").insert(Map.of(
                    "column2", "Foo",
                    "column3", 3,
                    "column4", false,
                    "column5", 5.43));

                assertEquals(124L, tuple.getValue("column1"));
                assertEquals("Foo", tuple.getValue("column2"));
                assertEquals(3, tuple.getValue("column3"));
                assertEquals(false, tuple.getValue("column4"));
                assertEquals(5.43, tuple.getValue("column5"));

                assertEquals(9, actualRows.size());
                assertArrayEquals(
                    new String[] {"124", "Foo", "3", "false", "5.43"},
                    adapter.getRows().get(8));

                tuple = database.into("table1").insert(Map.of(
                    "column2", "Bar",
                    "column3", 177,
                    "column4", true,
                    "column5", 0.72));

                assertEquals(125L, tuple.getValue("column1"));
                assertEquals("Bar", tuple.getValue("column2"));
                assertEquals(177, tuple.getValue("column3"));
                assertEquals(true, tuple.getValue("column4"));
                assertEquals(0.72, tuple.getValue("column5"));

                assertEquals(10, actualRows.size());
                assertArrayEquals(
                    new String[] {"124", "Foo", "3", "false", "5.43"},
                    adapter.getRows().get(8));
                assertArrayEquals(
                    new String[] {"125", "Bar", "177", "true", "0.72"},
                    adapter.getRows().get(9));
            }

            @Test
            void insertWithDefaultValues() {
                var tuple = database.into("table1").insert(Map.of(
                    "column2", "Foobar"));
              
                assertEquals(124L, tuple.getValue("column1"));
                assertEquals("Foobar", tuple.getValue("column2"));
                assertNull(tuple.getValue("column3"));
                assertEquals(false, tuple.getValue("column4")); 
                assertEquals(1.0, tuple.getValue("column5"));

                assertEquals(9, actualRows.size());
                assertArrayEquals(
                    new String[] {"124", "Foobar", "NULL", "false", "1.0"},
                    adapter.getRows().get(8));
            }
        }

        @Nested
        class TestFailedInsertions {
            @Test
            void shouldNotUpdateNonExistentAttribute() {
                assertThat(() -> database.into("table1")
                        .insert(
                            Map.of("column6", 4)),
                    throwsOfType(NoSuchAttributeException.class,
                        withMessage("Attribute column6 does not exist")));
            }

            @Test
            void shouldNotManuallySetAutoincrementingValue() {
                assertThat(() -> database.into("table1").insert(Map.of(
                        "column1", 5,
                        "column2", "Barfoo",
                        "column4", "false")),
                    throwsOfType(TableUpdateException.class,
                        withMessage("Automatically incrementing attribute "
                            + "column1 may not be set manually")));
            }

            @Test
            void shouldNotInsertDefaultValuesInNotnullColumns() {
                assertThat(() -> database.into("table1").insert(Map.of(
                        "column3", 9)),
                    throwsOfType(TableUpdateException.class,
                        anyOf(
                            withMessage("Attribute column2 may not be NULL"),
                            withMessage("Attribute column4 may not be NULL"))));
            }

            @Test
            void shouldNotInsertNullValuesInNotnullAttributes() {
                var mapWithNull = new HashMap<String, Object>();
                mapWithNull.put("column2", null);
                mapWithNull.put("column3", "Foo");
                mapWithNull.put("column4", null);
                assertThat(() -> database.into("table1").insert(mapWithNull),
                    throwsOfType(TableUpdateException.class,
                        anyOf(
                            withMessage("Attribute column2 may not be NULL"),
                            withMessage("Attribute column4 may not be NULL"))));
            }

            @Test
            void shouldNotInsertDuplicate() {
                assertThat(() -> database.into("table1").insert(Map.of(
                    "column2", "foofoo",
                    "column4", true)),
                    throwsOfType(TableUpdateException.class,
                        withMessage("Duplicate value foofoo for unique "
                            + "attribute column2")));
            }

            @Test
            void forbiddenCombination1() {
                assertThat(() -> database.into("table1").insert(Map.of(
                        "column1", 5)),
                    throwsOfType(TableUpdateException.class,
                        anyOf(
                            withMessage("Automatically incrementing attribute "
                                + "column1 may not be set manually"),
                            withMessage("Attribute column2 may not be NULL"))));
            }

            @Test
            void shouldNotAllowEmptyUpdateForNotnullAttributes() {
                assertThat(() -> database.into("table1")
                        .insert(Collections.emptyMap()),
                    throwsOfType(TableUpdateException.class,
                        anyOf(
                            withMessage("Attribute column2 may not be NULL"),
                            withMessage("Attribute column3 may not be NULL"))));
            }

            @Test
            void shouldNotSetUniqueToDuplicate() {
                var mapWithNull = new HashMap<String, Object>();
                mapWithNull.put("column2", "foobar");
                mapWithNull.put("column3", 1);
                mapWithNull.put("column4", null);
                assertThat(() -> database.into("table1").insert(mapWithNull),
                    throwsOfType(TableUpdateException.class,
                        anyOf(
                            withMessage("Duplicate value foobar for unique "
                                + "attribute column2"),
                            withMessage("Attribute column4 may not be NULL"))));
            }

            @Test
            void shouldRethrowOnIOException() {
                assertThat(() -> database.into("table2")
                        .insert(Map.of("column2", "")),
                    throwsOfType(TableUpdateException.class,
                        allOf(
                            withMessage("Table file could not be written"),
                            anyOf(
                                withCauseOfType(IOException.class),
                                withCauseOfType(TableFetchException.class)))));
            }

            @AfterEach
            void testNothingChanged() {
                assertEquals(initialRows.length, actualRows.size());
                for (var i = 0; i < actualRows.size(); i++) {
                    assertArrayEquals(initialRows[i], actualRows.get(i));
                }
            }
        }
    }

    @Nested
    class TestDelete {
        private List<String[]> actualRows;

        @BeforeEach
        void fillTable() {
            String[][] rows = {
                {"7", "foobar", "NULL", "false", "2.0"},
                {"2", "foobaz", "8", "true", "3.1"},
                {"123", "barfoo", "-100", "false", "NULL"},
                {"11", "bazfoo", "910", "false", "9.2"},
                {"29", "barbaz", "NULL", "true", "-10.2"},
                {"13", "foofoo", "160", "true", "0.1"},
                {"0", "baaaaaar", "-19", "false", "7.92"},
                {"5", "fuß", "314", "false", "12.89"}
            };

            actualRows = adapter.getRows();
            actualRows.addAll(Arrays.asList(rows));
        }

        @Test
        void testDeleteNothing() {
            database.from("table1")
                .delete(tuple -> false);

            assertEquals(8, actualRows.size());
            assertArrayEquals(
                new String[] {"7", "foobar", "NULL", "false", "2.0"},
                actualRows.get(0));
            assertArrayEquals(
                new String[] {"2", "foobaz", "8", "true", "3.1"},
                actualRows.get(1));
            assertArrayEquals(
                new String[] {"123", "barfoo", "-100", "false", "NULL"},
                actualRows.get(2));
            assertArrayEquals(
                new String[] {"11", "bazfoo", "910", "false", "9.2"},
                actualRows.get(3));
            assertArrayEquals(
                new String[] {"29", "barbaz", "NULL", "true", "-10.2"},
                actualRows.get(4));
            assertArrayEquals(
                new String[] {"13", "foofoo", "160", "true", "0.1"},
                actualRows.get(5));
            assertArrayEquals(
                new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                actualRows.get(6));
            assertArrayEquals(
                new String[] {"5", "fuß", "314", "false", "12.89"},
                actualRows.get(7));
        }

        @Test
        void testDeleteNullCondition() {
            database.from("table1")
                .delete(tuple -> tuple.getInteger("column3") == null);

            assertEquals(6, actualRows.size());
            assertArrayEquals(
                new String[] {"2", "foobaz", "8", "true", "3.1"},
                actualRows.get(0));
            assertArrayEquals(
                new String[] {"123", "barfoo", "-100", "false", "NULL"},
                actualRows.get(1));
            assertArrayEquals(
                new String[] {"11", "bazfoo", "910", "false", "9.2"},
                actualRows.get(2));
            assertArrayEquals(
                new String[] {"13", "foofoo", "160", "true", "0.1"},
                actualRows.get(3));
            assertArrayEquals(
                new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                actualRows.get(4));
            assertArrayEquals(
                new String[] {"5", "fuß", "314", "false", "12.89"},
                actualRows.get(5));
        }

        @Test
        void testDeleteBooleanCondition() {
            database.from("table1")
                .delete(tuple -> tuple.getBoolean("column4"));

            assertEquals(5, actualRows.size());
            assertArrayEquals(
                new String[] {"7", "foobar", "NULL", "false", "2.0"},
                actualRows.get(0));
            assertArrayEquals(
                new String[] {"123", "barfoo", "-100", "false", "NULL"},
                actualRows.get(1));
            assertArrayEquals(
                new String[] {"11", "bazfoo", "910", "false", "9.2"},
                actualRows.get(2));
            assertArrayEquals(
                new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                actualRows.get(3));
            assertArrayEquals(
                new String[] {"5", "fuß", "314", "false", "12.89"},
                actualRows.get(4));
        }

        @Test
        void testDeleteStringCondition() {
            database.from("table1")
                .delete(tuple -> tuple.getString("column2").startsWith("ba"));

            assertEquals(4, actualRows.size());
            assertArrayEquals(
                new String[] {"7", "foobar", "NULL", "false", "2.0"},
                actualRows.get(0));
            assertArrayEquals(
                new String[] {"2", "foobaz", "8", "true", "3.1"},
                actualRows.get(1));
            assertArrayEquals(
                new String[] {"13", "foofoo", "160", "true", "0.1"},
                actualRows.get(2));
            assertArrayEquals(
                new String[] {"5", "fuß", "314", "false", "12.89"},
                actualRows.get(3));
        }

        @Test
        void testDeleteIDCondition() {
            database.from("table1")
                .delete(tuple -> tuple.getLong("column1") > 5);

            assertEquals(3, actualRows.size());
            assertArrayEquals(
                new String[] {"2", "foobaz", "8", "true", "3.1"},
                actualRows.get(0));
            assertArrayEquals(
                new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                actualRows.get(1));
            assertArrayEquals(
                new String[] {"5", "fuß", "314", "false", "12.89"},
                actualRows.get(2));
        }

        @Test
        void testDeleteIntegerCondition() {
            database.from("table1")
                .delete(tuple -> Optional
                    .ofNullable(tuple.getInteger("column3"))
                    .filter(n -> Math.floorMod(n, 2) == 1)
                    .isEmpty());

            assertEquals(1, actualRows.size());
            assertArrayEquals(
                new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                actualRows.get(0));
        }

        @Test
        void testDeleteEverything() {
            database.from("table1")
                .delete(tuple -> true);

            assertEquals(0, actualRows.size());
        }

        @Test
        void testDeleteFails() {
            assertThat(() -> database.from("table2")
                    .delete(tuple -> false),
                throwsOfType(TableUpdateException.class,
                    allOf(
                        withMessage("Table file could not be written"),
                        anyOf(
                            withCauseOfType(IOException.class),
                            withCauseOfType(TableFetchException.class)))));
        }
    }

    @Nested
    class TestUpdate {
        private List<String[]> actualRows;

        @BeforeEach
        void fillTable() {
            String[][] rows = {
                {"7", "foobar", "NULL", "false", "2.0"},
                {"2", "foobaz", "8", "true", "3.1"},
                {"123", "barfoo", "-100", "false", "NULL"},
                {"11", "bazfoo", "910", "false", "9.2"},
                {"29", "barbaz", "NULL", "true", "-10.2"},
                {"13", "foofoo", "160", "true", "0.1"},
                {"0", "baaaaaar", "-19", "false", "7.92"},
                {"5", "fuß", "314", "false", "12.89"}
            };

            actualRows = adapter.getRows();
            actualRows.addAll(Arrays.asList(rows));
        }

        @Nested
        class TestSuccessfulUpdates {
            @Test
            void updateNothing() {
                database.into("table1")
                    .update(Map.of("column3", 9), tuple -> false);

                assertArrayEquals(
                    new String[] {"7", "foobar", "NULL", "false", "2.0"},
                    actualRows.get(0));
                assertArrayEquals(
                    new String[] {"2", "foobaz", "8", "true", "3.1"},
                    actualRows.get(1));
                assertArrayEquals(
                    new String[] {"123", "barfoo", "-100", "false", "NULL"},
                    actualRows.get(2));
                assertArrayEquals(
                    new String[] {"11", "bazfoo", "910", "false", "9.2"},
                    actualRows.get(3));
                assertArrayEquals(
                    new String[] {"29", "barbaz", "NULL", "true", "-10.2"},
                    actualRows.get(4));
                assertArrayEquals(
                    new String[] {"13", "foofoo", "160", "true", "0.1"},
                    actualRows.get(5));
                assertArrayEquals(
                    new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                    actualRows.get(6));
                assertArrayEquals(
                    new String[] {"5", "fuß", "314", "false", "12.89"},
                    actualRows.get(7));
            }
            @Test
            void emptyUpdate() {
                database.into("table1")
                    .update(Collections.emptyMap(), tuple -> true);

                assertArrayEquals(
                    new String[] {"7", "foobar", "NULL", "false", "2.0"},
                    actualRows.get(0));
                assertArrayEquals(
                    new String[] {"2", "foobaz", "8", "true", "3.1"},
                    actualRows.get(1));
                assertArrayEquals(
                    new String[] {"123", "barfoo", "-100", "false", "NULL"},
                    actualRows.get(2));
                assertArrayEquals(
                    new String[] {"11", "bazfoo", "910", "false", "9.2"},
                    actualRows.get(3));
                assertArrayEquals(
                    new String[] {"29", "barbaz", "NULL", "true", "-10.2"},
                    actualRows.get(4));
                assertArrayEquals(
                    new String[] {"13", "foofoo", "160", "true", "0.1"},
                    actualRows.get(5));
                assertArrayEquals(
                    new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                    actualRows.get(6));
                assertArrayEquals(
                    new String[] {"5", "fuß", "314", "false", "12.89"},
                    actualRows.get(7));
            }

            @Test
            void updateNullValues() {
                database.into("table1")
                    .update(
                        Map.of("column3", 0),
                        tuple -> tuple.getInteger("column3") == null);

                assertArrayEquals(
                    new String[] {"7", "foobar", "0", "false", "2.0"},
                    actualRows.get(0));
                assertArrayEquals(
                    new String[] {"2", "foobaz", "8", "true", "3.1"},
                    actualRows.get(1));
                assertArrayEquals(
                    new String[] {"123", "barfoo", "-100", "false", "NULL"},
                    actualRows.get(2));
                assertArrayEquals(
                    new String[] {"11", "bazfoo", "910", "false", "9.2"},
                    actualRows.get(3));
                assertArrayEquals(
                    new String[] {"29", "barbaz", "0", "true", "-10.2"},
                    actualRows.get(4));
                assertArrayEquals(
                    new String[] {"13", "foofoo", "160", "true", "0.1"},
                    actualRows.get(5));
                assertArrayEquals(
                    new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                    actualRows.get(6));
                assertArrayEquals(
                    new String[] {"5", "fuß", "314", "false", "12.89"},
                    actualRows.get(7));
            }

            @Test
            void updateMultipleRows() {
                database.into("table1")
                    .update(
                        Map.of("column3", 10),
                        tuple -> !tuple.getBoolean("column4"));

                assertArrayEquals(
                    new String[] {"7", "foobar", "10", "false", "2.0"},
                    actualRows.get(0));
                assertArrayEquals(
                    new String[] {"2", "foobaz", "8", "true", "3.1"},
                    actualRows.get(1));
                assertArrayEquals(
                    new String[] {"123", "barfoo", "10", "false", "NULL"},
                    actualRows.get(2));
                assertArrayEquals(
                    new String[] {"11", "bazfoo", "10", "false", "9.2"},
                    actualRows.get(3));
                assertArrayEquals(
                    new String[] {"29", "barbaz", "NULL", "true", "-10.2"},
                    actualRows.get(4));
                assertArrayEquals(
                    new String[] {"13", "foofoo", "160", "true", "0.1"},
                    actualRows.get(5));
                assertArrayEquals(
                    new String[] {"0", "baaaaaar", "10", "false", "7.92"},
                    actualRows.get(6));
                assertArrayEquals(
                    new String[] {"5", "fuß", "10", "false", "12.89"},
                    actualRows.get(7));
            }

            @Test
            void updateMultipleColumns() {
                database.into("table1")
                    .update(
                        Map.of(
                            "column2", "",
                            "column4", false),
                        tuple -> tuple.getLong("column1") == 2);

                assertArrayEquals(
                    new String[] {"7", "foobar", "NULL", "false", "2.0"},
                    actualRows.get(0));
                assertArrayEquals(
                    new String[] {"2", "", "8", "false", "3.1"},
                    actualRows.get(1));
                assertArrayEquals(
                    new String[] {"123", "barfoo", "-100", "false", "NULL"},
                    actualRows.get(2));
                assertArrayEquals(
                    new String[] {"11", "bazfoo", "910", "false", "9.2"},
                    actualRows.get(3));
                assertArrayEquals(
                    new String[] {"29", "barbaz", "NULL", "true", "-10.2"},
                    actualRows.get(4));
                assertArrayEquals(
                    new String[] {"13", "foofoo", "160", "true", "0.1"},
                    actualRows.get(5));
                assertArrayEquals(
                    new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                    actualRows.get(6));
                assertArrayEquals(
                    new String[] {"5", "fuß", "314", "false", "12.89"},
                    actualRows.get(7));
            }

            @Test
            void setToNull() {
                var mapWithNull1 = new HashMap<String, Object>();
                mapWithNull1.put("column3", null);
                mapWithNull1.put("column5", null);
                database.into("table1")
                    .update(
                        mapWithNull1,
                        tuple -> tuple.getBoolean("column4"));

                assertArrayEquals(
                    new String[] {"7", "foobar", "NULL", "false", "2.0"},
                    actualRows.get(0));
                assertArrayEquals(
                    new String[] {"2", "foobaz", "NULL", "true", "NULL"},
                    actualRows.get(1));
                assertArrayEquals(
                    new String[] {"123", "barfoo", "-100", "false", "NULL"},
                    actualRows.get(2));
                assertArrayEquals(
                    new String[] {"11", "bazfoo", "910", "false", "9.2"},
                    actualRows.get(3));
                assertArrayEquals(
                    new String[] {"29", "barbaz", "NULL", "true", "NULL"},
                    actualRows.get(4));
                assertArrayEquals(
                    new String[] {"13", "foofoo", "NULL", "true", "NULL"},
                    actualRows.get(5));
                assertArrayEquals(
                    new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                    actualRows.get(6));
                assertArrayEquals(
                    new String[] {"5", "fuß", "314", "false", "12.89"},
                    actualRows.get(7));
            }

            @AfterEach
            void testSameSize() {
                assertEquals(8, actualRows.size());
            }
        }

        @Nested
        class TestFailedUpdates {
            @Test
            void shouldNotUpdateNonExistentAttribute() {
                assertThat(() -> database.into("table1")
                        .update(
                            Map.of("column6", 4),
                            tuple -> tuple.getLong("column1") == 0),
                    throwsOfType(NoSuchAttributeException.class,
                        withMessage("Attribute column6 does not exist")));
            }

            @Test
            void shouldNotUpdateUniqueAttributeToDuplicate() {
                assertThat(() -> database.into("table1")
                        .update(
                            Map.of("column2", "foofoo"),
                            tuple -> tuple.getLong("column1") == 0),
                    throwsOfType(TableUpdateException.class,
                        withMessage("Duplicate value foofoo for unique "
                            + "attribute column2")));
            }

            @Test
            void shouldNotUpdateAutoincrementingAttribute() {
                assertThat(() -> database.into("table1")
                        .update(
                            Map.of("column1", 2),
                            tuple -> tuple.getBoolean("column4")),
                    throwsOfType(TableUpdateException.class,
                        withMessage("Automatically incrementing attribute "
                            + "column1 may not be set manually")));
            }

            @Test
            void shouldNotUpdateNotnullAttributeToNull() {
                var mapWithNull = new HashMap<String, Object>();
                mapWithNull.put("column2", null);
                mapWithNull.put("column4", false);
                assertThat(() -> database.into("table1")
                        .update(
                            mapWithNull,
                            tuple -> tuple.getLong("column1") == 2),
                    throwsOfType(TableUpdateException.class,
                        withMessage("Attribute column2 may not be NULL")));
            }

            @Test
            void shouldNotUpdateMultipleUniqueAttributes() {
                assertThat(() -> database.into("table1")
                        .update(
                            Map.of("column2", "foo"),
                            tuple -> tuple.getBoolean("column4")),
                    throwsOfType(TableUpdateException.class,
                        withMessage("Duplicate value foo for unique attribute "
                            + "column2")));
            }

            @Test
            void shouldRethrowOnIOException() {
                assertThat(() -> database.into("table2")
                        .update(Map.of("column2", ""), tuple -> false),
                    throwsOfType(TableUpdateException.class,
                        allOf(
                            withMessage("Table file could not be written"),
                            anyOf(
                                withCauseOfType(IOException.class),
                                withCauseOfType(TableFetchException.class)))));
            }

            @AfterEach
            void testNothingChanged() {
                assertArrayEquals(
                    new String[] {"7", "foobar", "NULL", "false", "2.0"},
                    actualRows.get(0));
                assertArrayEquals(
                    new String[] {"2", "foobaz", "8", "true", "3.1"},
                    actualRows.get(1));
                assertArrayEquals(
                    new String[] {"123", "barfoo", "-100", "false", "NULL"},
                    actualRows.get(2));
                assertArrayEquals(
                    new String[] {"11", "bazfoo", "910", "false", "9.2"},
                    actualRows.get(3));
                assertArrayEquals(
                    new String[] {"29", "barbaz", "NULL", "true", "-10.2"},
                    actualRows.get(4));
                assertArrayEquals(
                    new String[] {"13", "foofoo", "160", "true", "0.1"},
                    actualRows.get(5));
                assertArrayEquals(
                    new String[] {"0", "baaaaaar", "-19", "false", "7.92"},
                    actualRows.get(6));
                assertArrayEquals(
                    new String[] {"5", "fuß", "314", "false", "12.89"},
                    actualRows.get(7));
            }
        }
	}
}
