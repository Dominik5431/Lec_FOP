package campus.ui.form;

import java.awt.Component;
import java.util.Optional;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import campus.data.domain.Student;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class StudentCreatorForm extends CreatorForm<Student> {
    private static final long serialVersionUID = -2087674786142739819L;

    private final JTextField firstNameField;
    private final JTextField lastNameField;

    private StudentCreatorForm() {
        var layout = new GroupLayout(this);

        var firstNameLabel = new JLabel("Vorname:");
        var lastNameLabel = new JLabel("Nachname:");

        firstNameField = new JTextField();
        lastNameField = new JTextField();

        firstNameField.getDocument().addDocumentListener(this);
        lastNameField.getDocument().addDocumentListener(this);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(firstNameLabel)
                        .addComponent(lastNameLabel))
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(firstNameField)
                        .addComponent(lastNameField)));

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(firstNameLabel)
                        .addComponent(firstNameField))
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lastNameLabel)
                        .addComponent(lastNameField)));

        setLayout(layout);

        updateOkayButton();
    }

    @Override
    public Student getValue() {
        return new Student(
            firstNameField.getText().strip(),
            lastNameField.getText().strip());
    }

    @Override
    public boolean shouldEnableOkayButton() {
        return !firstNameField.getText().isBlank() &&
            !lastNameField.getText().isBlank();
    }

    public static Optional<Student> showDialog(Component parentComponent) {
        return CreatorForm.showDialog(
            parentComponent,
            new StudentCreatorForm(),
            "Student*in immatrikulieren");
    }
}
