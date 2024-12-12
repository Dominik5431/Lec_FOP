package campus.ui.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import campus.data.domain.Grade;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class GradeCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 2632514676470859230L;

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column
    ) {
        var component = super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);

        var grade = (Grade) value;

        setText(grade != null ? grade.toString() : "-");
        setHorizontalAlignment(TRAILING);

        return component;
    }
}
