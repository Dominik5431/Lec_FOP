package campus.data.repository;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.TreeMap;

import java.util.stream.Collectors;

import campus.data.domain.Exam;
import campus.data.domain.Grade;
import campus.data.domain.Student;

import campus.data.query.Database;
import campus.data.query.Tuple;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class StudentRepositoryImpl implements StudentRepository {
    private final Database database;

    public StudentRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public Student persist(Student student) {
        student.getID().ifPresentOrElse(id ->
            database.into("student")
                .update(Map.of(
                        "firstname", student.getFirstName(),
                        "lastname", student.getLastName()),
                    tuple -> tuple.getLong("id").equals(id)),
            () -> {
                Tuple tuple = database
                    .into("student")
                    .insert(Map.of(
                        "firstname", student.getFirstName(),
                        "lastname", student.getLastName()));
                student.setID(tuple.getLong("id"));
            });
        return student;
    }

    @Override
    public void delete(Student student) {
        student.getID().ifPresentOrElse(id -> {
            database.from("student")
                .delete(tuple -> tuple.getLong("id").equals(id));
            database.from("student_exam")
                .delete(tuple -> tuple.getLong("student_id").equals(id));
        }, () -> {
            throw new NonPersistentContextException(student);
        });
    }

    @Override
    public Set<Student> getAll() {
        try (var tuples = database.from("student")
                .select("id", "firstname", "lastname")) {
            return tuples.map(tuple -> {
                Student student = new Student(
                    tuple.getString("firstname"),
                    tuple.getString("lastname"));
                student.setID(tuple.getLong("id"));
                return student;
            }).collect(Collectors.collectingAndThen(
                Collectors.toCollection(TreeSet::new),
                Collections::unmodifiableSortedSet));
        }
    }

    @Override
    public Map<Exam, Optional<Grade>> getGradesForStudent(Student student) {
        var gradeCollector = Collectors.<Grade, Optional<Grade>>reducing(
                Optional.empty(),
                Optional::ofNullable,
                (o1, o2) -> o1.or(() -> o2));
        return student.getID().map(studentID -> {
            try (var tuples = database
                    .from("student_exam").as("se")
                    .join(database.from("student").as("student"),
                            "se.student_id", "student.id")
                    .join(database.from("exam").as("exam"),
                            "se.exam_id", "exam.id")
                    .select("student.id", "exam.id", "exam.date", "se.grade")) {
                return tuples.filter(tuple -> tuple.getLong("student.id").equals(studentID))
                        .collect(Collectors.collectingAndThen(
                                Collectors.groupingBy(tuple -> {
                                    var examId = tuple.getLong("exam.id");
                                    var date = (Date) tuple.getValue("exam.date");
                                    var exam = new Exam(date);
                                    exam.setID(examId);
                                    return exam;
                                }, TreeMap::new, Collectors.mapping(
                                        tuple -> (Grade) tuple.getValue("se.grade"),
                                        gradeCollector)),
                                Collections::unmodifiableMap));
            }
        }).orElseThrow(() -> new NonPersistentContextException(student));
    }
}
