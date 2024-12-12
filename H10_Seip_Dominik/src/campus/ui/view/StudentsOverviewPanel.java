package campus.ui.view;

import java.util.Date;

import java.text.DateFormat;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import campus.data.domain.Grade;
import campus.data.domain.Lecture;
import campus.data.domain.Student;

import campus.data.repository.ExamRepository;
import campus.data.repository.StudentRepository;

import campus.ui.form.StudentCreatorForm;

import campus.ui.model.StudentExamsTableModel;
import campus.ui.model.StudentsTableModel;

import campus.ui.renderer.DateCellRenderer;
import campus.ui.renderer.GradeCellRenderer;
import campus.ui.renderer.LectureCellRenderer;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class StudentsOverviewPanel
    extends JPanel implements ListSelectionListener {
    private static final long serialVersionUID = -8375486144749159037L;

    private static final String ADD_STUDENT_BUTTON_TEXT =
        "Student*in hinzufügen";
    private static final String DELETE_STUDENT_BUTTON_TEXT =
        "Student*in löschen";

    private static final String STUDENTS_TITLED_BORDER_TEXT =
        "Studierende";
    private static final String STUDENT_LECTURES_TITLED_BORDER_TEXT =
        "Prüfungen";

    private final StudentsTableModel studentsModel;
    private final StudentExamsTableModel studentExamsModel;

    private final JTable studentsTable, studentExamsTable;

    private final JButton addStudentButton =
        new JButton(ADD_STUDENT_BUTTON_TEXT);
    private final JButton deleteStudentButton =
        new JButton(DELETE_STUDENT_BUTTON_TEXT);

    private Student selectedStudent;

    public StudentsOverviewPanel(
        ExamRepository examRepository, StudentRepository studentRepository) {
        studentsModel = new StudentsTableModel(studentRepository);
        studentExamsModel = new StudentExamsTableModel(
            examRepository, studentRepository);

        studentsTable = new JTable(studentsModel);
        studentExamsTable = new JTable(studentExamsModel);

        addStudentButton.addActionListener(e ->
            StudentCreatorForm.showDialog(this)
                .ifPresent(student -> {
                    studentsModel.addStudent(student);

                    var row = studentsTable.convertRowIndexToView(0);
                    var col = studentsTable.convertColumnIndexToView(0);
                    var rect = studentsTable.getCellRect(row, col, true);
                    studentsTable.setRowSelectionInterval(row, row);
                    studentsTable.scrollRectToVisible(rect);
                }));

        deleteStudentButton.addActionListener(e -> {
            var selectedRow = studentsTable.getSelectedRow();

            studentsModel.deleteStudent(selectedStudent);

            var maxIndex = studentsTable.getRowCount() - 1;
            var newIndex = Math.min(selectedRow, maxIndex);
            if (newIndex >= 0) {
                studentsTable.setRowSelectionInterval(newIndex, newIndex);
            }
        });

        setupStudentsTable();
        setupExamsTable();
        setupGUI();
        updateButtons();
    }

    private JPanel getStudentsPane() {
        var studentsPane = new JPanel(new BorderLayout());

        var studentsButtonPane = new JPanel();
        studentsButtonPane.setLayout(
            new BoxLayout(studentsButtonPane, BoxLayout.LINE_AXIS));

        studentsButtonPane.add(addStudentButton);
        studentsButtonPane.add(Box.createHorizontalGlue());
        studentsButtonPane.add(deleteStudentButton);

        var studentsScrollPane = new JScrollPane(studentsTable);
        studentsScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        studentsPane.add(studentsScrollPane, BorderLayout.CENTER);
        studentsPane.add(studentsButtonPane, BorderLayout.PAGE_END);

        var border = BorderFactory.createTitledBorder(
            STUDENTS_TITLED_BORDER_TEXT);
        studentsPane.setBorder(border);

        studentsPane.setAlignmentX(RIGHT_ALIGNMENT);

        return studentsPane;
    }

    private JPanel getExamsPane() {
        var examsPane = new JPanel(new BorderLayout());

        var examsScrollPane = new JScrollPane(studentExamsTable);
        examsScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        examsPane.add(examsScrollPane, BorderLayout.CENTER);

        var border = BorderFactory.createTitledBorder(
            STUDENT_LECTURES_TITLED_BORDER_TEXT);
        examsPane.setBorder(border);

        examsPane.setAlignmentX(LEFT_ALIGNMENT);

        return examsPane;
    }

    private void setupStudentsTable() {
        studentsTable.setAutoCreateRowSorter(true);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        studentsTable.getSelectionModel().addListSelectionListener(this);

        studentsTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        studentsTable.getColumnModel().getColumn(1).setPreferredWidth(150);

        var height = studentsTable
            .getPreferredScrollableViewportSize().height;

        studentsTable.setPreferredScrollableViewportSize(
            new Dimension(studentsTable.getPreferredSize().width, height));
    }

    private void setupExamsTable() {
        studentExamsTable.setAutoCreateRowSorter(true);
        studentExamsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        studentExamsTable.setDefaultRenderer(
            Lecture.class, new LectureCellRenderer());

        studentExamsTable.setDefaultRenderer(
            Date.class, new DateCellRenderer(
                DateFormat.getDateInstance(DateFormat.SHORT),
                DateFormat.getDateTimeInstance(
                    DateFormat.FULL, DateFormat.SHORT)));

        studentExamsTable.setDefaultRenderer(
            Grade.class, new GradeCellRenderer());

        studentExamsTable.getColumnModel().getColumn(0).setPreferredWidth(400);
        studentExamsTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        studentExamsTable.getColumnModel().getColumn(2).setPreferredWidth(70);

        var height = studentExamsTable
            .getPreferredScrollableViewportSize().height;

        studentExamsTable.setPreferredScrollableViewportSize(
            new Dimension(studentExamsTable.getPreferredSize().width, height));
    }

    private void setupGUI() {
        setLayout(new BorderLayout());
        add(getStudentsPane(), BorderLayout.CENTER);
        add(getExamsPane(), BorderLayout.LINE_END);
    }

    private void updateButtons() {
        deleteStudentButton.setEnabled(selectedStudent != null);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            var selectedRow = studentsTable.getSelectedRow();

            if (selectedRow >= 0) {
                selectedStudent = studentsModel.getStudentAt(
                    studentsTable.convertRowIndexToModel(selectedRow));
            } else {
                selectedStudent = null;
            }

            studentExamsModel.setStudent(selectedStudent);
            updateButtons();
        }
    }
}
