package campus.data.repository;

import java.util.Map;
import java.util.Optional;

import campus.data.domain.Exam;
import campus.data.domain.Grade;
import campus.data.domain.Student;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface StudentRepository extends Repository<Student> {
    Map<Exam, Optional<Grade>> getGradesForStudent(Student student);
}
