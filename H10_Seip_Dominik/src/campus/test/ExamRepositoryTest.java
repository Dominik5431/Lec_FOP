package campus.test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import campus.data.domain.Lecture;
import campus.data.domain.Exam;
import campus.data.domain.Student;
import campus.data.domain.Grade;
import campus.data.domain.GradeType;

import campus.data.query.csv.CSVListDatabase;
import campus.data.query.csv.CSVTableTypeMap;
import campus.data.query.csv.io.CSVListAdapter;

import campus.data.repository.ExamAlreadyScheduledException;
import campus.data.repository.ExamRepository;
import campus.data.repository.ExamRepositoryImpl;
import campus.data.repository.NonPersistentContextException;
import campus.data.repository.StudentAlreadyRegisteredException;
import campus.data.repository.StudentNotRegisteredException;

import static campus.test.matcher.ThrowsOfType.throwsOfType;
import static campus.test.matcher.WithEntity.withEntity;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class ExamRepositoryTest {
    private ExamRepository repository;
    private List<String[]> lectureRows, examRows, studentRows, studentExamRows;

    @BeforeEach
    void populateRepository() {
        var typeMap = CSVTableTypeMap.defaultTypeMap()
            .registerType(Grade.class, "GRADE", new GradeType());

        var lectureAdapter = new CSVListAdapter(new String[] {
            "id:         LONG     (AUTOINCREMENT)",
            "title:      STRING   (UNIQUE, NOTNULL)"
        });
        lectureRows = lectureAdapter.getRows();

        var examAdapter = new CSVListAdapter(new String[] {
            "id:         LONG     (AUTOINCREMENT)",
            "date:       DATETIME (NOTNULL)",
            "lecture_id: LONG     (NOTNULL)"
        });
        examRows = examAdapter.getRows();

        var studentAdapter = new CSVListAdapter(new String[] {
            "id:         LONG     (AUTOINCREMENT)",
            "lastname:   STRING   (NOTNULL)",
            "firstname:  STRING   (NOTNULL)"
        });
        studentRows = studentAdapter.getRows();

        var studentExamAdapter = new CSVListAdapter(new String[] {
            "id:         LONG     (AUTOINCREMENT)",
            "student_id: LONG     (NOTNULL)",
            "exam_id:    LONG     (NOTNULL)",
            "grade:      GRADE"
        });
        studentExamRows = studentExamAdapter.getRows();

        var adapters = Map.of(
            "lecture", lectureAdapter,
            "exam", examAdapter,
            "student", studentAdapter,
            "student_exam", studentExamAdapter);

        var database = new CSVListDatabase(adapters, typeMap);
        repository = new ExamRepositoryImpl(database);

        lectureRows.add(new String[] {"0", "Lecture 1"});
        lectureRows.add(new String[] {"2", "Lecture 2"});
        lectureRows.add(new String[] {"5", "Lecture 3"});

        examRows.add(new String[] {"5", "2021-04-06 12:00:00.000", "2"});
        examRows.add(new String[] {"3", "2021-03-11 14:00:00.000", "0"});
        examRows.add(new String[] {"4", "2020-04-07 09:30:00.000", "0"});

        studentRows.add(new String[] {"1", "Last Name 1", "First Name 1"});
        studentRows.add(new String[] {"2", "Last Name 2", "First Name 2"});
        studentRows.add(new String[] {"3", "Last Name 3", "First Name 3"});
        studentRows.add(new String[] {"0", "Last Name 4", "First Name 4"});
        studentRows.add(new String[] {"9", "Last Name 5", "First Name 5"});
        studentRows.add(new String[] {"4", "Last Name 6", "First Name 6"});

        studentExamRows.add(new String[] {"0", "9", "5", "NULL"});
        studentExamRows.add(new String[] {"1", "4", "5", "NULL"});
        studentExamRows.add(new String[] {"5", "2", "3", "GRADE37"});
        studentExamRows.add(new String[] {"2", "2", "5", "NULL"});
        studentExamRows.add(new String[] {"4", "1", "3", "NULL"});
        studentExamRows.add(new String[] {"3", "0", "5", "GRADE17"});
        studentExamRows.add(new String[] {"7", "9", "3", "NULL"});
        studentExamRows.add(new String[] {"6", "3", "3", "GRADE23"});
    }

    @Test
    void testPersistExamSuccess() {
        var date = new GregorianCalendar(2022, 2, 15, 10, 0, 0).getTime();
        var exam = new Exam(date);
        exam.setID(3);

        var savedExam = repository.persist(exam);

        assertEquals(3L, exam.getID().get());
        assertEquals(3L, savedExam.getID().get());
        assertEquals(date, savedExam.getDate());
        assertSame(exam, savedExam);

        assertEquals(3, examRows.size());

        assertThat(examRows, hasItems(
            new String[] {"5", "2021-04-06 12:00:00.000", "2"},
            new String[] {"3", "2022-03-15 10:00:00.000", "0"},
            new String[] {"4", "2020-04-07 09:30:00.000", "0"}));
    }

    @Test
    void testPersistExamFail() {
        var date = new GregorianCalendar(2021, 2, 2, 11, 0, 0).getTime();
        var exam = new Exam(date);

        assertThat(() -> repository.persist(exam),
            throwsOfType(NonPersistentContextException.class,
                withEntity(exam)));

        assertEquals(3, examRows.size());

        assertThat(examRows, hasItems(
            new String[] {"5", "2021-04-06 12:00:00.000", "2"},
            new String[] {"3", "2021-03-11 14:00:00.000", "0"},
            new String[] {"4", "2020-04-07 09:30:00.000", "0"}));
    }

    @Test
    void testDeleteExamSuccess() {
        var date = new GregorianCalendar(2021, 2, 11, 14, 0, 0).getTime();
        var exam = new Exam(date);
        exam.setID(3);

        repository.delete(exam);

        assertEquals(2, examRows.size());

        assertThat(examRows, hasItems(
            new String[] {"5", "2021-04-06 12:00:00.000", "2"},
            new String[] {"4", "2020-04-07 09:30:00.000", "0"}));
    }

    @Test
    void testDeleteExamFail() {
        var date = new GregorianCalendar(2021, 2, 2, 11, 0, 0).getTime();
        var exam = new Exam(date);

        assertThat(() -> repository.delete(exam),
            throwsOfType(NonPersistentContextException.class,
                withEntity(exam)));

        assertEquals(3, examRows.size());

        assertThat(examRows, hasItems(
            new String[] {"5", "2021-04-06 12:00:00.000", "2"},
            new String[] {"3", "2021-03-11 14:00:00.000", "0"},
            new String[] {"4", "2020-04-07 09:30:00.000", "0"}));
    }

    @Test
    void testGetAllExams() {
        var exams = repository.getAll();

        var exam1 = new Exam(
            new GregorianCalendar(2021, 3, 6, 12, 0, 0).getTime());
        exam1.setID(5);
        var exam2 = new Exam(
            new GregorianCalendar(2021, 2, 11, 14, 0, 0).getTime());
        exam2.setID(3);
        var exam3 = new Exam(
            new GregorianCalendar(2020, 3, 7, 9, 30, 0).getTime());
        exam3.setID(4);

        assertEquals(3, exams.size());
        assertThat(exams, hasItems(exam1, exam2, exam3));

        assertThat(exams, instanceOf(SortedSet.class));

        assertThat(() -> exams.add(new Exam(new Date())),
            describedAs("Das Ergebnis von getAllExams() darf nicht veränderbar "
                    + "sein",
                throwsOfType(UnsupportedOperationException.class)));
        assertThat(() -> exams.remove(exam1),
            describedAs("Das Ergebnis von getAllExams() darf nicht veränderbar "
                    + "sein",
                throwsOfType(UnsupportedOperationException.class)));
    }

    @Test
    void testGetAllExamsForLectureSucces() {
        var lecture = new Lecture("Lecture 1");
        lecture.setID(0);

        var exams = repository.getAllExamsForLecture(lecture);

        var exam1 = new Exam(
            new GregorianCalendar(2021, 2, 11, 14, 0, 0).getTime());
        exam1.setID(3);
        var exam2 = new Exam(
            new GregorianCalendar(2020, 3, 7, 9, 30, 0).getTime());
        exam2.setID(4);

        assertEquals(2, exams.size());
        assertThat(exams, hasItems(exam1, exam2));

        assertThat(exams, instanceOf(SortedSet.class));

        assertThat(() -> exams.add(new Exam(new Date())),
            describedAs("Das Ergebnis von getAllExamsForLecture(Lecture) darf "
                    + "nicht veränderbar sein",
                throwsOfType(UnsupportedOperationException.class)));
        assertThat(() -> exams.remove(exam1),
            describedAs("Das Ergebnis von getAllExamsForLecture(Lecture) darf "
                    + "nicht veränderbar sein",
                throwsOfType(UnsupportedOperationException.class)));
    }

    @Test
    void testGetAllExamsForLectureFail() {
        var lecture = new Lecture("Lecture 4");

        assertThat(() -> repository.getAllExamsForLecture(lecture),
            throwsOfType(NonPersistentContextException.class,
                withEntity(lecture)));
    }

    @Test
    void testScheduleExamForLectureSuccess() {
        var lecture = new Lecture("Lecture 3");
        lecture.setID(5);

        var date = new GregorianCalendar(2021, 2, 2, 11, 0, 0).getTime();
        var exam = new Exam(date);

        var savedExam = repository.scheduleExamForLecture(exam, lecture);

        assertEquals(6L, exam.getID().get());
        assertEquals(6L, savedExam.getID().get());
        assertEquals(date, savedExam.getDate());
        assertSame(exam, savedExam);

        assertEquals(4, examRows.size());

        assertThat(examRows, hasItems(
            new String[] {"5", "2021-04-06 12:00:00.000", "2"},
            new String[] {"3", "2021-03-11 14:00:00.000", "0"},
            new String[] {"4", "2020-04-07 09:30:00.000", "0"},
            new String[] {"6", "2021-03-02 11:00:00.000", "5"}));
    }

    @Test
    void testScheduleExamForLectureFail() {
        var lecture = new Lecture("Lecture 3");
        lecture.setID(5);

        var date = new GregorianCalendar(2021, 3, 6, 12, 0, 0).getTime();
        var exam = new Exam(date);
        exam.setID(5);

        assertThat(() -> repository.scheduleExamForLecture(exam, lecture),
            throwsOfType(ExamAlreadyScheduledException.class));

        assertEquals(3, examRows.size());

        assertThat(examRows, hasItems(
            new String[] {"5", "2021-04-06 12:00:00.000", "2"},
            new String[] {"3", "2021-03-11 14:00:00.000", "0"},
            new String[] {"4", "2020-04-07 09:30:00.000", "0"}));
    }

    @Test
    void testGetAttendingStudentsForExamSuccess() {
        var date = new GregorianCalendar(2021, 3, 6, 12, 0, 0).getTime();
        var exam = new Exam(date);
        exam.setID(5);

        var students = repository.getAttendingStudentsForExam(exam);

        var student1 = new Student("First Name 4", "Last Name 4");
        student1.setID(0);
        var student2 = new Student("First Name 2", "Last Name 2");
        student2.setID(2);
        var student3 = new Student("First Name 6", "Last Name 6");
        student3.setID(4);
        var student4 = new Student("First Name 5", "Last Name 5");
        student4.setID(9);

        assertEquals(4, students.size());
        assertThat(students, hasItems(student1, student2, student3, student4));

        assertThat(students, instanceOf(SortedSet.class));

        assertThat(() ->
                students.add(new Student("First Name 7", "Last Name 7")),
            describedAs("Das Ergebnis von getAttendingStudentsForExam(Exam) "
                + "darf nicht veränderbar sein",
                throwsOfType(UnsupportedOperationException.class)));
        assertThat(() -> students.remove(student1),
            describedAs("Das Ergebnis von getAttendingStudentsForExam(Exam) "
                + "darf nicht veränderbar sein",
                throwsOfType(UnsupportedOperationException.class)));
    }

    @Test
    void testGetAttendingStudentsForExamFail() {
        var date = new GregorianCalendar(2021, 2, 2, 11, 0, 0).getTime();
        var exam = new Exam(date);

        assertThat(() -> repository.getAttendingStudentsForExam(exam),
            throwsOfType(NonPersistentContextException.class,
                withEntity(exam)));
    }

    @Test
    void testRegisterStudentForExamSuccess() {
        var student = new Student("First Name 3", "Last Name 3");
        student.setID(3);

        var date = new GregorianCalendar(2021, 3, 6, 12, 0, 0).getTime();
        var exam = new Exam(date);
        exam.setID(5);

        repository.registerStudentForExam(student, exam);

        assertEquals(9, studentExamRows.size());
        assertThat(studentExamRows, hasItems(
            new String[] {"0", "9", "5", "NULL"},
            new String[] {"1", "4", "5", "NULL"},
            new String[] {"5", "2", "3", "GRADE37"},
            new String[] {"2", "2", "5", "NULL"},
            new String[] {"4", "1", "3", "NULL"},
            new String[] {"3", "0", "5", "GRADE17"},
            new String[] {"7", "9", "3", "NULL"},
            new String[] {"6", "3", "3", "GRADE23"},
            new String[] {"8", "3", "5", "NULL"}));
    }

    @Test
    void testRegisterStudentForExamFail() {
        var student = new Student("First Name 3", "Last Name 3");

        var date = new GregorianCalendar(2021, 2, 11, 14, 0, 0).getTime();
        var exam = new Exam(date);

        assertThat(() -> repository.registerStudentForExam(student, exam),
            throwsOfType(NonPersistentContextException.class,
                anyOf(withEntity(student), withEntity(exam))));

        student.setID(3);

        assertThat(() -> repository.registerStudentForExam(student, exam),
            throwsOfType(NonPersistentContextException.class,
                withEntity(exam)));

        exam.setID(3);

        assertThat(() -> repository.registerStudentForExam(student, exam),
            throwsOfType(StudentAlreadyRegisteredException.class));

        assertEquals(8, studentExamRows.size());
        assertThat(studentExamRows, hasItems(
            new String[] {"0", "9", "5", "NULL"},
            new String[] {"1", "4", "5", "NULL"},
            new String[] {"5", "2", "3", "GRADE37"},
            new String[] {"2", "2", "5", "NULL"},
            new String[] {"4", "1", "3", "NULL"},
            new String[] {"3", "0", "5", "GRADE17"},
            new String[] {"7", "9", "3", "NULL"},
            new String[] {"6", "3", "3", "GRADE23"}));
    }

    @Test
    void testDeregisterStudentFromExamSuccess() {
        var student = new Student("First Name 1", "Last Name 1");
        student.setID(1);

        var date = new GregorianCalendar(2021, 2, 11, 14, 0, 0).getTime();
        var exam = new Exam(date);
        exam.setID(3);

        repository.deregisterStudentFromExam(student, exam);

        assertEquals(7, studentExamRows.size());
        assertThat(studentExamRows, hasItems(
            new String[] {"0", "9", "5", "NULL"},
            new String[] {"1", "4", "5", "NULL"},
            new String[] {"5", "2", "3", "GRADE37"},
            new String[] {"2", "2", "5", "NULL"},
            new String[] {"3", "0", "5", "GRADE17"},
            new String[] {"7", "9", "3", "NULL"},
            new String[] {"6", "3", "3", "GRADE23"}));
    }

    @Test
    void testDeregisterStudentFromExamFail() {
        var student = new Student("First Name 3", "Last Name 3");

        var date = new GregorianCalendar(2021, 3, 6, 12, 0, 0).getTime();
        var exam = new Exam(date);

        assertThat(() -> repository.deregisterStudentFromExam(student, exam),
            throwsOfType(NonPersistentContextException.class,
                anyOf(withEntity(student), withEntity(exam))));

        student.setID(3);

        assertThat(() -> repository.deregisterStudentFromExam(student, exam),
            throwsOfType(NonPersistentContextException.class,
                withEntity(exam)));

        exam.setID(5);

        assertThat(() -> repository.deregisterStudentFromExam(student, exam),
            throwsOfType(StudentNotRegisteredException.class));

        assertEquals(8, studentExamRows.size());
        assertThat(studentExamRows, hasItems(
            new String[] {"0", "9", "5", "NULL"},
            new String[] {"1", "4", "5", "NULL"},
            new String[] {"5", "2", "3", "GRADE37"},
            new String[] {"2", "2", "5", "NULL"},
            new String[] {"4", "1", "3", "NULL"},
            new String[] {"3", "0", "5", "GRADE17"},
            new String[] {"7", "9", "3", "NULL"},
            new String[] {"6", "3", "3", "GRADE23"}));
    }

    @Test
    void testGradeStudentForExamSuccess() {
        var student = new Student("First Name 5", "Last Name 5");
        student.setID(9);

        var date = new GregorianCalendar(2021, 2, 11, 14, 0, 0).getTime();
        var exam = new Exam(date);
        exam.setID(3);

        repository.gradeStudentForExam(student, exam, Grade.GRADE40);

        assertEquals(8, studentExamRows.size());
        assertThat(studentExamRows, hasItems(
            new String[] {"0", "9", "5", "NULL"},
            new String[] {"1", "4", "5", "NULL"},
            new String[] {"5", "2", "3", "GRADE37"},
            new String[] {"2", "2", "5", "NULL"},
            new String[] {"4", "1", "3", "NULL"},
            new String[] {"3", "0", "5", "GRADE17"},
            new String[] {"7", "9", "3", "GRADE40"},
            new String[] {"6", "3", "3", "GRADE23"}));
    }

    @Test
    void testGradeStudentForExamFail() {
        var student = new Student("First Name 3", "Last Name 3");

        var date = new GregorianCalendar(2021, 3, 6, 12, 0, 0).getTime();
        var exam = new Exam(date);

        assertThat(() -> repository.gradeStudentForExam(
                student, exam, Grade.GRADE10),
            throwsOfType(NonPersistentContextException.class,
                anyOf(withEntity(student), withEntity(exam))));

        student.setID(3);

        assertThat(() -> repository.gradeStudentForExam(
                student, exam, Grade.GRADE10),
            throwsOfType(NonPersistentContextException.class,
                withEntity(exam)));

        exam.setID(5);

        assertThat(() -> repository.gradeStudentForExam(
                student, exam, Grade.GRADE10),
            throwsOfType(StudentNotRegisteredException.class));

        assertEquals(8, studentExamRows.size());
        assertThat(studentExamRows, hasItems(
            new String[] {"0", "9", "5", "NULL"},
            new String[] {"1", "4", "5", "NULL"},
            new String[] {"5", "2", "3", "GRADE37"},
            new String[] {"2", "2", "5", "NULL"},
            new String[] {"4", "1", "3", "NULL"},
            new String[] {"3", "0", "5", "GRADE17"},
            new String[] {"7", "9", "3", "NULL"},
            new String[] {"6", "3", "3", "GRADE23"}));
    }

    @Test
    void testGetLectureForExamSuccess() {
        var date = new GregorianCalendar(2021, 3, 6, 12, 0, 0).getTime();
        var exam = new Exam(date);
        exam.setID(5);

        var lecture = new Lecture("Lecture 2");
        lecture.setID(2);

        assertEquals(lecture, repository.getLectureForExam(exam));
    }

    @Test
    void testGetLectureForExamFail() {
        var date = new GregorianCalendar(2021, 2, 2, 11, 0, 0).getTime();
        var exam = new Exam(date);

        assertThat(() -> repository.getLectureForExam(exam),
            throwsOfType(NonPersistentContextException.class,
                withEntity(exam)));
    }

    @Test
    void testGetGradeForStudentSuccess() {
        var date = new GregorianCalendar(2021, 2, 11, 14, 0, 0).getTime();
        var exam = new Exam(date);
        exam.setID(3);

        var student1 = new Student("First Name 1", "Last Name 1");
        student1.setID(1);

        var student2 = new Student("First Name 2", "Last Name 2");
        student2.setID(2);

        assertEquals(
            Optional.empty(),
            repository.getGradeForStudent(student1, exam));
        assertEquals(
            Optional.of(Grade.GRADE37),
            repository.getGradeForStudent(student2, exam));
    }

    @Test
    void testGetGradeForStudentFail() {
        var date = new GregorianCalendar(2021, 2, 11, 14, 0, 0).getTime();
        var exam = new Exam(date);

        var student1 = new Student("First Name 1", "Last Name 1");

        assertThat(() -> repository.getGradeForStudent(student1, exam),
            throwsOfType(NonPersistentContextException.class,
                anyOf(withEntity(student1), withEntity(exam))));

        student1.setID(1);

        assertThat(() -> repository.getGradeForStudent(student1, exam),
            throwsOfType(NonPersistentContextException.class,
                withEntity(exam)));

        exam.setID(3);

        var student2 = new Student("First Name 6", "Last Name 6");
        student2.setID(4);

        assertThat(() -> repository.getGradeForStudent(student2, exam),
            throwsOfType(StudentNotRegisteredException.class));
    }

    @AfterEach
    void testOtherTablesUnchanged() {
        assertEquals(3, lectureRows.size());
        assertThat(lectureRows, hasItems(
            new String[] {"0", "Lecture 1"},
            new String[] {"2", "Lecture 2"},
            new String[] {"5", "Lecture 3"}));

        assertEquals(6, studentRows.size());
        assertThat(studentRows, hasItems(
            new String[] {"1", "Last Name 1", "First Name 1"},
            new String[] {"2", "Last Name 2", "First Name 2"},
            new String[] {"3", "Last Name 3", "First Name 3"},
            new String[] {"0", "Last Name 4", "First Name 4"},
            new String[] {"9", "Last Name 5", "First Name 5"},
            new String[] {"4", "Last Name 6", "First Name 6"}));
    }
}
