package campus.ui.model;

import java.util.Date;
import javax.swing.table.AbstractTableModel;

import campus.data.domain.Grade;
import campus.data.domain.Lecture;
import campus.data.domain.Student;

import campus.data.repository.ExamRepository;
import campus.data.repository.StudentRepository;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class StudentExamsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -8562751890561019357L;

    private Student student;

    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    private Object[][] data;

    public StudentExamsTableModel(
        ExamRepository examRepository, StudentRepository studentRepository) {
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        reload();
    }

    public void setStudent(Student student) {
        this.student = student;
        reload();
    }

    private void reload() {
        if (student != null) {
            data = studentRepository.getGradesForStudent(student)
                .entrySet()
                .stream()
                .map(entry -> {
                    var exam = entry.getKey();
                    var grade = entry.getValue().orElse(null);
                    var lecture = examRepository.getLectureForExam(exam);
                    var examDate = exam.getDate();
                    return new Object[] { lecture, examDate, grade };
                }).toArray(Object[][]::new);
        } else {
            data = new Object[0][3];
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Lecture.class;
            case 1: return Date.class;
            case 2: return Grade.class;
            default: throw new IndexOutOfBoundsException(columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Veranstaltung";
            case 1: return "Datum";
            case 2: return "Note";
            default: throw new IndexOutOfBoundsException(column);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
}
