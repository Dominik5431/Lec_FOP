package campus.ui.editor;

import java.util.Date;
import java.util.EventObject;

import java.awt.Component;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;

import javax.swing.table.TableCellEditor;

import campus.data.domain.Exam;

import campus.ui.form.ExamCreatorForm;
import campus.ui.renderer.DateCellRenderer;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class DateCellEditor extends AbstractCellEditor
    implements TableCellEditor {
    private static final long serialVersionUID = -2197658480213770878L;

    Date date;
    JLabel label = new JLabel();

    public DateCellEditor() {
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                showEditDialog();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                showEditDialog();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                showEditDialog();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                showEditDialog();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        ExamCreatorForm.showDialog(label, date, "Datum und Uhrzeit anpassen")
            .map(Exam::getDate)
            .ifPresentOrElse(d -> {
                date = d;
                stopCellEditing();
            }, this::cancelCellEditing);
    }

    @Override
    public Object getCellEditorValue() {
        return date;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return ((MouseEvent) e).getClickCount() >= 2;
        }
        return true;
    }

    @Override
    public Component getTableCellEditorComponent(
        JTable table, Object value, boolean isSelected, int row, int column
    ) {
        var renderer = (DateCellRenderer) table.getCellRenderer(row, column)
            .getTableCellRendererComponent(
                table, value, isSelected, true, row, column);

        date = (Date) value;

        label.setText(renderer.getText());
        label.setFont(renderer.getFont());

        label.setForeground(renderer.getForeground());
        label.setBackground(renderer.getBackground());

        return label;
    }
}
