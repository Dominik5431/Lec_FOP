package campus.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import campus.data.domain.Lecture;

import campus.data.repository.LectureRepository;

/**
 * @author Kim Berninger
 * @author ...
 * @version 1.0.2
 */
public class LecturesTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 96505648359996064L;

    private final LectureRepository lectureRepository;

    private final List<Lecture> lectures;

    public LecturesTableModel(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
        lectures = new ArrayList<>(lectureRepository.getAll());
    }

    public Lecture getLectureAt(int row) {
        // TODO H5 Implementieren Sie diese Methode
        return null;
    }

    public void addLecture(Lecture lecture) {
        // TODO H5 Implementieren Sie diese Methode
    }

    public void deleteLecture(Lecture lecture) {
        // TODO H5 Implementieren Sie diese Methode
    }

    @Override
    public int getRowCount() {
        // TODO H5 Implementieren Sie diese Methode
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // TODO H5 Implementieren Sie diese Methode
        return Object.class;
    }

    @Override
    public String getColumnName(int column) {
        // TODO H5 Implementieren Sie diese Methode
        return "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO H5 Implementieren Sie diese Methode
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // TODO H5 Implementieren Sie diese Methode
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // TODO H5 Implementieren Sie diese Methode
        return false;
    }
}
