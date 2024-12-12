package campus.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import campus.data.domain.Student;
import campus.data.repository.StudentRepository;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class StudentsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -5809686841568631568L;

    private final StudentRepository studentRepository;

    private List<Student> students;

    public StudentsTableModel(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        reload();
    }

    private void reload() {
        students = new ArrayList<>();
        students.addAll(studentRepository.getAll());
        fireTableDataChanged();
    }

    public void addStudent(Student student) {
        students.add(0, studentRepository.persist(student));
        fireTableRowsInserted(0, 0);
    }

    public void deleteStudent(Student student) {
        var index = students.indexOf(student);
        students.remove(index);
        studentRepository.delete(student);
        fireTableRowsDeleted(index, index);
    }

    public Student getStudentAt(int row) {
        return students.get(row);
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Nachname";
            case 1: return "Vorname";
            default: throw new IndexOutOfBoundsException(column);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var student = students.get(rowIndex);
        switch (columnIndex) {
            case 0: return student.getLastName();
            case 1: return student.getFirstName();
            default: throw new IndexOutOfBoundsException(columnIndex);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        var student = students.get(rowIndex);
        switch (columnIndex) {
            case 0:
                student.setLastName((String) aValue);
                break;
            case 1:
                student.setFirstName((String) aValue);
                break;
        }
        studentRepository.persist(student);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}
