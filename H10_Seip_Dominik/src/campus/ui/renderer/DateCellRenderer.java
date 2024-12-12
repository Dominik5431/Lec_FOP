package campus.ui.renderer;

import java.util.Date;

import java.text.DateFormat;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class DateCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = -8375906227779474121L;

    private final DateFormat textFormatter, toolTipFormatter;

    public DateCellRenderer(DateFormat formatter) {
        this(formatter, formatter);
    }

    public DateCellRenderer(
        DateFormat textFormatter, DateFormat toolTipFormatter
    ) {
        this.textFormatter = textFormatter;
        this.toolTipFormatter = toolTipFormatter;
    }

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column
    ) {
        var component = super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);

        var date = (Date) value;

        setText(textFormatter.format(date));
        setToolTipText(toolTipFormatter.format(date));

        return component;
    }
}
