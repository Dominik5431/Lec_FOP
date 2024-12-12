package campus.ui.model;

import javax.swing.table.AbstractTableModel;

import campus.data.domain.Exam;
import campus.data.domain.Student;
import campus.data.repository.ExamRepository;
import campus.data.repository.StudentRepository;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class AttendeesTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 7757778227928302486L;

    private Exam exam;

    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    private Object[][] data;

    public AttendeesTableModel(
        ExamRepository examRepository,
        StudentRepository studentRepository
    ) {
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        reload();
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        reload();
    }

    public void reload() {
        if (exam != null) {
            var attendees = examRepository.getAttendingStudentsForExam(exam);
            data = studentRepository.getAll().stream().map(student -> {
                var attending = attendees.contains(student);
                return new Object[] { student, attending };
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
                return Boolean.class;
            default:
                throw new IndexOutOfBoundsException(columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Nachname";
            case 1: return "Vorname";
            case 2: return "Nimmt teil";
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
        var attending = (Boolean) aValue;
        if (attending) {
            examRepository.registerStudentForExam(student, exam);
        } else {
            examRepository.deregisterStudentFromExam(student, exam);
        }
        reload();
    }
}
