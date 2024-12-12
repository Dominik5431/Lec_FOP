package campus.test;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import campus.data.domain.Lecture;

import campus.data.repository.LectureRepository;
import campus.data.repository.LectureWithTitleAlreadyExistsException;
import campus.ui.model.LecturesTableModel;

import static campus.test.matcher.HasID.hasID;
import static campus.test.matcher.HasTitle.hasTitle;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class LecturesTableModelTest {
    private static abstract class MockTableModelListener implements TableModelListener {
        protected int numberOfCalls = 0;

        public int getNumberOfCalls() {
            return numberOfCalls;
        }
    }

    private static class MockLectureRepository implements LectureRepository {
        private TreeSet<Lecture> lectures = new TreeSet<>();

        @Override
        public Lecture persist(Lecture lecture) {
            if (getAll().stream()
                .map(Lecture::getTitle)
                .anyMatch(lecture.getTitle()::equals)) {

                throw new LectureWithTitleAlreadyExistsException(
                    lecture.getTitle());
            }

            var newLecture = new Lecture(lecture.getTitle());

            lecture.getID().ifPresentOrElse(
                lectureID -> newLecture.setID(lectureID),
                () -> newLecture.setID(lectures.stream()
                    .map(Lecture::getID)
                    .map(Optional::get)
                    .mapToLong(n -> n + 1)
                    .max()
                    .orElse(0)));

            lectures.removeIf(l -> l.getID().get() == newLecture.getID().get());
            lectures.add(newLecture);
            return newLecture;
        }

        @Override
        public Set<Lecture> getAll() {
            return lectures.stream().map(lecture -> {
                var newLecture = new Lecture(lecture.getTitle());
                newLecture.setID(lecture.getID().get());
                return newLecture;
            }).collect(Collectors.collectingAndThen(
                Collectors.toCollection(TreeSet::new),
                Collections::unmodifiableSortedSet));
        }

        @Override
        public void delete(Lecture entity) {
            entity.getID().ifPresent(lectureId ->
                lectures.removeIf(lecture ->
                    lecture.getID().filter(id -> id == lectureId).isPresent()));
        }
    }

    private LectureRepository repository;

    private LecturesTableModel model;

    @BeforeEach
    void populateRepository() {
        repository = new MockLectureRepository();

        repository.persist(new Lecture("Lecture 1"));
        repository.persist(new Lecture("Lecture 2"));
        repository.persist(new Lecture("Lecture 3"));
        repository.persist(new Lecture("Lecture 4"));
        repository.persist(new Lecture("Lecture 5"));
        repository.persist(new Lecture("Lecture 6"));

        model = new LecturesTableModel(repository);
    }

    @Test
    void testGetColumnCount() {
        assertEquals(1, model.getColumnCount());
    }

    @Test
    void testGetRowCount() {
        assertEquals(6, model.getRowCount());
    }

    @Test
    void testGetColumnName() {
        assertEquals("Titel", model.getColumnName(0));
    }

    @Test
    void testGetColumnClass() {
        assertEquals(String.class, model.getColumnClass(0));
    }

    @Test
    void testGetLectureAt() {
        var i = 0;
        for (var lecture : repository.getAll()) {
            assertEquals(lecture, model.getLectureAt(i++));
        }
    }

    @Test
    void testGetValueAt() {
        var i = 0;
        for (var lecture : repository.getAll()) {
            assertEquals(lecture.getTitle(), model.getValueAt(i++, 0));
        }
    }

    @Test
    void testIsCellEditable() {
        IntStream.range(0, 6).forEach(i ->
            assertTrue(model.isCellEditable(i, 0)));
    }

    @Test
    void testSetValueAt() {
        var listener = new MockTableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                assertEquals(2, e.getFirstRow());
                assertEquals(2, e.getLastRow());
                assertEquals(0, e.getColumn());

                assertEquals(TableModelEvent.UPDATE, e.getType());

                assertEquals(6, repository.getAll().size());

                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(0), hasTitle("Lecture 1"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(1), hasTitle("Lecture 2"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(2), hasTitle("Foobar"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(3), hasTitle("Lecture 4"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(4), hasTitle("Lecture 5"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(5), hasTitle("Lecture 6"))));

                numberOfCalls++;
            }
        };

        model.addTableModelListener(listener);
        model.setValueAt("Foobar", 2, 0);

        assertEquals(1, listener.getNumberOfCalls(),
            "fireTableCellUpdated wurde nicht genau einmal aufgerufen");
    }

    @Test
    void testSetValueAtWithDuplicateLectureTitle() {
        var listener = new MockTableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                numberOfCalls++;
            }
        };

        model.addTableModelListener(listener);
        model.setValueAt("Lecture 6", 4, 0);

        assertThat(repository.getAll(), hasItem(
            allOf(hasID(0), hasTitle("Lecture 1"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(1), hasTitle("Lecture 2"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(2), hasTitle("Lecture 3"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(3), hasTitle("Lecture 4"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(4), hasTitle("Lecture 5"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(5), hasTitle("Lecture 6"))));

        assertEquals(0, listener.getNumberOfCalls(),
            "fireTableCellUpdated wurde fälschlicherweise aufgerufen");
    }

    @Test
    void testSetValueAtWithEmptyTitle() {
        var listener = new MockTableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                numberOfCalls++;
            }
        };

        model.addTableModelListener(listener);
        model.setValueAt("      ", 3, 0);

        assertThat(repository.getAll(), hasItem(
            allOf(hasID(0), hasTitle("Lecture 1"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(1), hasTitle("Lecture 2"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(2), hasTitle("Lecture 3"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(3), hasTitle("Lecture 4"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(4), hasTitle("Lecture 5"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(5), hasTitle("Lecture 6"))));

        assertEquals(0, listener.getNumberOfCalls(),
            "fireTableCellUpdated wurde fälschlicherweise aufgerufen");
    }

    @Test
    void testAddLecture() {
        var listener = new MockTableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                assertEquals(0, e.getFirstRow());
                assertEquals(0, e.getLastRow());
                assertEquals(TableModelEvent.ALL_COLUMNS, e.getColumn());

                assertEquals(TableModelEvent.INSERT, e.getType());

                assertEquals(7, repository.getAll().size());

                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(0), hasTitle("Lecture 1"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(1), hasTitle("Lecture 2"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(2), hasTitle("Lecture 3"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(3), hasTitle("Lecture 4"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(4), hasTitle("Lecture 5"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(5), hasTitle("Lecture 6"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(6), hasTitle("Barfoo"))));

                assertThat(model.getLectureAt(0),
                    allOf(hasID(6), hasTitle("Barfoo")));

                numberOfCalls++;
            }
        };

        model.addTableModelListener(listener);
        model.addLecture(new Lecture("Barfoo"));

        assertEquals(1, listener.getNumberOfCalls(),
            "fireTableRowsInserted wurde nicht genau einmal aufgerufen");
    }

    @Test
    void testAddLectureWithDuplicateTitle() {
        var listener = new MockTableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                numberOfCalls++;
            }
        };

        model.addTableModelListener(listener);
        model.addLecture(new Lecture("Lecture 4"));

        assertThat(repository.getAll(), hasItem(
            allOf(hasID(0), hasTitle("Lecture 1"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(1), hasTitle("Lecture 2"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(2), hasTitle("Lecture 3"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(3), hasTitle("Lecture 4"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(4), hasTitle("Lecture 5"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(5), hasTitle("Lecture 6"))));

        assertEquals(0, listener.getNumberOfCalls(),
            "fireTableRowsInserted wurde fälschlicherweise aufgerufen");
    }

    @Test
    void testAddAlreadyExistingLecture() {
        var listener = new MockTableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                numberOfCalls++;
            }
        };

        model.addTableModelListener(listener);

        var lecture = new Lecture("Lecture 4");
        lecture.setID(3);

        model.addLecture(lecture);

        assertThat(repository.getAll(), hasItem(
            allOf(hasID(0), hasTitle("Lecture 1"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(1), hasTitle("Lecture 2"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(2), hasTitle("Lecture 3"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(3), hasTitle("Lecture 4"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(4), hasTitle("Lecture 5"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(5), hasTitle("Lecture 6"))));

        assertEquals(0, listener.getNumberOfCalls(),
            "fireTableRowsInserted wurde fälschlicherweise aufgerufen");
    }

    @Test
    void testAddLectureWithEmptyTitle() {
        var listener = new MockTableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                numberOfCalls++;
            }
        };

        model.addTableModelListener(listener);
        model.addLecture(new Lecture(""));

        assertThat(repository.getAll(), hasItem(
            allOf(hasID(0), hasTitle("Lecture 1"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(1), hasTitle("Lecture 2"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(2), hasTitle("Lecture 3"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(3), hasTitle("Lecture 4"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(4), hasTitle("Lecture 5"))));
        assertThat(repository.getAll(), hasItem(
            allOf(hasID(5), hasTitle("Lecture 6"))));

        assertEquals(0, listener.getNumberOfCalls(),
            "fireTableRowsInserted wurde fälschlicherweise aufgerufen");
    }

    @Test
    void testDeleteLecture() {
        var listener = new MockTableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                assertEquals(4, e.getFirstRow());
                assertEquals(4, e.getLastRow());
                assertEquals(TableModelEvent.ALL_COLUMNS, e.getColumn());

                assertEquals(TableModelEvent.DELETE, e.getType());

                assertEquals(5, repository.getAll().size());

                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(0), hasTitle("Lecture 1"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(1), hasTitle("Lecture 2"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(2), hasTitle("Lecture 3"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(3), hasTitle("Lecture 4"))));
                assertThat(repository.getAll(), hasItem(
                    allOf(hasID(5), hasTitle("Lecture 6"))));

                numberOfCalls++;
            }
        };

        var lecture = new Lecture("Lecture 5");
        lecture.setID(4);

        model.addTableModelListener(listener);
        model.deleteLecture(lecture);

        assertEquals(1, listener.getNumberOfCalls(),
            "fireTableRowsDeleted wurde nicht genau einmal aufgerufen");
    }
}
