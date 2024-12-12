package campus.data.repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import campus.data.domain.Exam;
import campus.data.domain.Grade;
import campus.data.domain.Lecture;
import campus.data.domain.Student;

import campus.data.query.Database;
import campus.data.query.Tuple;

/**
 * @author Kim Berninger
 * @author ...
 * @version 1.0.2
 */
public class ExamRepositoryImpl implements ExamRepository {
    private final Database database;

    public ExamRepositoryImpl(Database database) {
        this.database = database;
    }

    @Override
    public Exam persist(Exam exam) {
        // TODO H4 Implementieren Sie diese Methode
    	if (exam.getID().equals(Optional.empty())) {
    		throw new NonPersistentContextException(exam);
    	}
    	database.into("exam").update(Map.of("date", exam.getDate()), tpl -> tpl.getValue("id") == exam.getID());
        return exam;
    }

    @Override
    public void delete(Exam exam) {
        // TODO H4 Implementieren Sie diese Methode
    	if (exam.getID().equals(Optional.empty())) {
    		throw new NonPersistentContextException(exam);
    	}
    	database.from("exam").delete(tpl -> tpl.getValue("id") == exam.getID());
    }

    @Override
    public Set<Exam> getAll() {
        // TODO H4 Implementieren Sie diese Methode
    	try (var tuples = database.from("exam").select()) {
    		return tuples.map(tuple -> {
    			Exam exam = new Exam((Date) tuple.getValue("date"));
    			exam.setID(tuple.getLong("id"));
    			return exam;
    		}).collect(Collectors.collectingAndThen(
    				Collectors.toCollection(TreeSet::new), 
    				Collections::unmodifiableSortedSet));
    	}
    }

    @Override
    public Exam scheduleExamForLecture(Exam exam, Lecture lecture) {
        // TODO H4 Implementieren Sie diese Methode
    	if (exam.getID().equals(Optional.empty())) {
    		throw new NonPersistentContextException(exam);
    	}
    	if (lecture.getID().equals(Optional.empty())) {
    		throw new NonPersistentContextException(lecture);
    	}
    	if (database.from("exam").select()
    			.map(tpl -> {
    				List<String> title =  database.from("lecture")
    						.select("title")
    						.filter(tuple -> tuple.getLong("id")
    								.equals(tpl.getLong("lecture_id")))
    						.map(tupel -> tupel.getString("title")).collect(Collectors.toList());
    				return title.get(0);
    						})
    			.anyMatch(str -> str == lecture.getTitle())) {
    		throw new ExamAlreadyScheduledException(exam);
    	}
    	Map<String, ?> entry = Map.of("id", null, "date", exam.getDate(), "lecture_id", lecture.getID());
    	database.into("exam").insert(entry);
        return exam;
    }

    @Override
    public Set<Exam> getAllExamsForLecture(Lecture lecture) {
        // TODO H4 Implementieren Sie diese Methode
    	if (lecture.getID().equals(Optional.empty())) {
    		throw new NonPersistentContextException(lecture);
    	}
        return database.from("exam").select()
        		.filter(tpl -> tpl.getLong("lecture_id").equals(lecture.getID()))
        		.map(tpl -> {
    			Exam exam = new Exam((Date) tpl.getValue("date"));
    			exam.setID(tpl.getLong("id"));
    			return exam;
    		})
        		.collect(Collectors.collectingAndThen(
    				Collectors.toCollection(TreeSet::new), 
    				Collections::unmodifiableSortedSet));
    }

    @Override
    public Lecture getLectureForExam(Exam exam) {
        // TODO H4 Implementieren Sie diese Methode
    	@SuppressWarnings("unlikely-arg-type")
		List<Long> ids = database
    			.from("exam").as("exam")
    			.join(database .from("lecture"), "exam.lecture_id", "id")
    			.select()
    			.filter(tuple -> tuple.getLong("exam.lecture_id").equals(exam.getID()))
    			.map(tupel -> tupel.getLong("exam.lecture_id"))
    			.collect(Collectors.toList());
    	long id = ids.get(0);
        return database.from("lecture").select()
        		.filter(tuple -> tuple.getLong("id").equals(id))
        		.map(tuple -> {
        			Lecture lect = new Lecture(tuple.getString("title"));
        			lect.setID(tuple.getLong("id"));
        			return lect;
        		})
        		.collect(Collectors.toList())
        		.get(0);
    }

    @Override
    public Set<Student> getAttendingStudentsForExam(Exam exam) {
        // TODO H4 Implementieren Sie diese Methode
        return Collections.emptySet();
    }

    @Override
    public void registerStudentForExam(Student student, Exam exam) {
        // TODO H4 Implementieren Sie diese Methode
    }

    @Override
    public void deregisterStudentFromExam(Student student, Exam exam) {
        // TODO H4 Implementieren Sie diese Methode
    }

    @Override
    public void gradeStudentForExam(Student student, Exam exam, Grade grade) {
        // TODO H4 Implementieren Sie diese Methode
    }

    @Override
    public Optional<Grade> getGradeForStudent(Student student, Exam exam) {
        // TODO H4 Implementieren Sie diese Methode
        return Optional.empty();
    }
}
