package campus.ui.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import campus.data.domain.Lecture;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class LectureCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 6923457913780543523L;

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column
    ) {
        var component = super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);

        var lecture = (Lecture) value;

        setText(lecture.getTitle());
        setToolTipText(lecture.getTitle());

        return component;
    }
}
