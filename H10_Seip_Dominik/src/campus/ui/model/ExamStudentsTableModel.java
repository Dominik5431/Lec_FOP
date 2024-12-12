package campus.ui.model;

import javax.swing.table.AbstractTableModel;

import campus.data.domain.Exam;
import campus.data.domain.Grade;
import campus.data.domain.Student;
import campus.data.repository.ExamRepository;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class ExamStudentsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1224258266459074834L;

    private Exam exam;

    private final ExamRepository examRepository;

    private Object[][] data;

    public ExamStudentsTableModel(ExamRepository examRepository) {
        this.examRepository = examRepository;
        reload();
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        reload();
    }

    public void reload() {
        if (exam != null) {
            data = examRepository.getAttendingStudentsForExam(exam).stream()
                .map(student -> {
                    var grade = examRepository
                        .getGradeForStudent(student, exam)
                        .orElse(null);
                    return new Object[] { student, grade };
                }).toArray(Object[][]::new);
        } else {
            data = new Object[0][2];
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
            case 0:
            case 1:
                return String.class;
            case 2:
                return Grade.class;
            default:
                throw new IndexOutOfBoundsException(columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Nachname";
            case 1: return "Vorname";
            case 2: return "Note";
            default: throw new IndexOutOfBoundsException(column);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var student = (Student) data[rowIndex][0];
        switch (columnIndex) {
            case 0: return student.getLastName();
            case 1: return student.getFirstName();
            case 2: return data[rowIndex][1];
            default: throw new IndexOutOfBoundsException(columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        var student = (Student) data[rowIndex][0];
        var grade = (Grade) aValue;
        if (grade != null) {
            examRepository.gradeStudentForExam(student, exam, grade);
            reload();
        }
    }
}
