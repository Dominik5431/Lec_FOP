package campus.ui.renderer;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import javax.swing.plaf.UIResource;

import javax.swing.table.TableCellRenderer;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class AlternatingBooleanCellRenderer extends JCheckBox
    implements TableCellRenderer {

    private static final long serialVersionUID = 919967690457457095L;

    public AlternatingBooleanCellRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorderPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        }
        else {
            var background = table.getBackground();

            if (background == null || background instanceof UIResource) {
                var alternateColor = 
                    UIManager.getColor("Table.alternateRowColor");
                if (alternateColor != null && row % 2 != 0) {
                    background = alternateColor;
                }
            }

            setForeground(table.getForeground());
            setBackground(background);
        }

        setSelected((value != null && ((Boolean) value)));

        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else {
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }

        return this;
    }
}
