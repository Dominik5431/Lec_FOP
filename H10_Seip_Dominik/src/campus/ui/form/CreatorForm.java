package campus.ui.form;

import java.awt.Component;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
abstract class CreatorForm<T> extends JPanel implements DocumentListener {
    private static final long serialVersionUID = 2898426611189226856L;

    private final JButton okayButton, cancelButton;

    public CreatorForm() {
        okayButton = new JButton("Speichern");
        cancelButton = new JButton("Abbrechen");

        okayButton.addActionListener(e -> {
            JOptionPane pane = getOptionPane((JComponent) e.getSource());
            pane.setValue(okayButton);
        });

        cancelButton.addActionListener(e -> {
            JOptionPane pane = getOptionPane((JComponent) e.getSource());
            pane.setValue(cancelButton);
        });
    }

    protected void updateOkayButton() {
        okayButton.setEnabled(shouldEnableOkayButton());
    }

    public abstract T getValue();

    public abstract boolean shouldEnableOkayButton();

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateOkayButton();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateOkayButton();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateOkayButton();
    }

    private static JOptionPane getOptionPane(JComponent component) {
        if (component instanceof JOptionPane) {
            return (JOptionPane) component;
        } else {
            return getOptionPane((JComponent) component.getParent());
        }
    }

    public static <T> Optional<T> showDialog(
        Component parentComponent, CreatorForm<? extends T> form, String title
    ) {
        Object[] options = {
            form.okayButton,
            form.cancelButton
        };

        int answer = JOptionPane.showOptionDialog(
            parentComponent, form, title,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
            null, options, null);

        if (answer == JOptionPane.OK_OPTION) {
            return Optional.of(form.getValue());
        } else {
            return Optional.empty();
        }
    }
}
