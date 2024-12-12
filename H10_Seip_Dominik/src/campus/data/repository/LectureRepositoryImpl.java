package campus.data.repository;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import campus.data.domain.Lecture;

import campus.data.query.Database;
import campus.data.query.Tuple;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class LectureRepositoryImpl implements LectureRepository {
    private final Database database;

    public LectureRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public Lecture persist(Lecture lecture) {
        if (getAll().stream()
            .map(Lecture::getTitle)
            .anyMatch(lecture.getTitle()::equals)) {

            throw new LectureWithTitleAlreadyExistsException(
                lecture.getTitle());
        }
        lecture.getID().ifPresentOrElse(id ->
            database
                .into("lecture")
                .update(
                    Map.of("title", lecture.getTitle()),
                    tuple -> tuple.getLong("id").equals(id)),
            () -> {
                Tuple tuple = database
                    .into("lecture")
                    .insert(Map.of("title", lecture.getTitle()));
                lecture.setID(tuple.getLong("id"));
            });
        return lecture;
    }

    @Override
    public void delete(Lecture lecture) {
        lecture.getID().ifPresentOrElse(id -> {
            database.from("exam").as("exam")
                .join(database.from("lecture").as("lecture"),
                    "exam.lecture_id", "lecture.id")
                .select("exam.id", "exam.lecture_id")
                .filter(tuple -> tuple.getLong("exam.lecture_id").equals(id))
                .mapToLong(tuple -> tuple.getLong("exam.id"))
                .forEach(examId -> database
                    .from("student_exam")
                    .delete(tuple -> tuple.getLong("exam_id") == examId));

            database
                .from("exam")
                .delete(tuple -> tuple.getLong("lecture_id").equals(id));

            database
                .from("lecture")
                .delete(tuple -> tuple.getLong("id").equals(id));
        }, () -> {
            throw new NonPersistentContextException(lecture);
        });
    }

    @Override
    public Set<Lecture> getAll() {
        try (var tuples = database.from("lecture").select("id", "title")) {
        return tuples.map(tuple -> {
                Lecture lecture = new Lecture(tuple.getString("title"));
                lecture.setID(tuple.getLong("id"));
                return lecture;
            }).collect(Collectors.collectingAndThen(
                Collectors.toCollection(TreeSet::new),
                Collections::unmodifiableSortedSet));
        }
    }
}
