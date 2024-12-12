package campus.data.repository;

import java.util.Optional;
import java.util.Set;

import campus.data.domain.Exam;
import campus.data.domain.Grade;
import campus.data.domain.Lecture;
import campus.data.domain.Student;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface ExamRepository extends Repository<Exam> {
    Exam scheduleExamForLecture(Exam exam, Lecture lecture);

    Set<Exam> getAllExamsForLecture(Lecture lecture);
    Set<Student> getAttendingStudentsForExam(Exam exam);

    void registerStudentForExam(Student student, Exam exam);
    void deregisterStudentFromExam(Student student, Exam exam);
    void gradeStudentForExam(Student student, Exam exam, Grade grade);

    Lecture getLectureForExam(Exam exam);

    Optional<Grade> getGradeForStudent(Student student, Exam exam);
}
