package campus.ui.form;

import java.awt.Component;
import java.util.Optional;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import campus.data.domain.Lecture;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class LectureCreatorForm extends CreatorForm<Lecture> {
    private static final long serialVersionUID = 7508375508236578044L;

    private final JTextField titleField;

    private LectureCreatorForm() {
        var layout = new GroupLayout(this);

        var titleLabel = new JLabel("Titel:");

        titleField = new JTextField(30);

        titleField.getDocument().addDocumentListener(this);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(titleLabel))
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(titleField)));

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(titleLabel)
                        .addComponent(titleField)));

        setLayout(layout);

        updateOkayButton();
    }

    @Override
    public Lecture getValue() {
        return new Lecture(titleField.getText().strip());
    }

    @Override
    public boolean shouldEnableOkayButton() {
        return !titleField.getText().isBlank();
    }

    public static Optional<Lecture> showDialog(Component parentComponent) {
        return CreatorForm.showDialog(
            parentComponent,
            new LectureCreatorForm(),
            "Veranstaltung registrieren");
    }
}
