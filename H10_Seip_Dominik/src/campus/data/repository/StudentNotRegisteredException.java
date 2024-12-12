package campus.data.repository;

import campus.data.domain.Exam;
import campus.data.domain.Student;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class StudentNotRegisteredException extends RuntimeException {
    private static final long serialVersionUID = -3742066578303758732L;

    private final Student student;
    private final Exam exam;

    public StudentNotRegisteredException(Student student, Exam exam) {
        super(String.format("Student %s not registered for exam %s",
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
