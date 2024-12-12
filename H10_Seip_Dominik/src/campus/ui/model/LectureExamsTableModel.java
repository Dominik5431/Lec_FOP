package campus.ui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import campus.data.domain.Lecture;
import campus.data.domain.Exam;

import campus.data.repository.ExamRepository;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class LectureExamsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 2950588825200630494L;

    private Lecture lecture;

    private final ExamRepository examRepository;

    private List<Exam> exams;

    public LectureExamsTableModel(ExamRepository examRepository) {
        this.examRepository = examRepository;
        reload();
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
        reload();
    }

    private void reload() {
        exams = new ArrayList<>();
        if (lecture != null) {
            exams.addAll(examRepository.getAllExamsForLecture(lecture));
        }
        fireTableDataChanged();
    }

    public void addExam(Exam exam) {
        exams.add(0, examRepository.scheduleExamForLecture(exam, lecture));
        fireTableRowsInserted(0, 0);
    }

    public void deleteExam(Exam exam) {
        var index = exams.indexOf(exam);
        exams.remove(index);
        examRepository.delete(exam);
        fireTableRowsDeleted(index, index);
    }

    public Exam getExamAt(int row) {
        return exams.get(row);
    }

    @Override
    public int getRowCount() {
        return exams.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Date.class;
    }

    @Override
    public String getColumnName(int column) {
        return "Datum und Uhrzeit";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return exams.get(rowIndex).getDate();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue != null) {
            exams.get(rowIndex).setDate((Date) aValue);
            examRepository.persist(exams.get(rowIndex));
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
}
