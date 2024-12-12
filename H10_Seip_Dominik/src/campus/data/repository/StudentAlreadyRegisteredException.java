package campus.data.repository;

import campus.data.domain.Exam;
import campus.data.domain.Student;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class StudentAlreadyRegisteredException extends RuntimeException {
    private static final long serialVersionUID = 4045581515821490738L;

    private final Student student;
    private final Exam exam;

    public StudentAlreadyRegisteredException(Student student, Exam exam) {
        super(String.format("Student %s already registered for exam %s",
            student, exam));
        this.student = student;
        this.exam = exam;
    }

    public Student getStudent() {
        return student;
    }

    public Exam getExam() {
        return exam;
    }
}
