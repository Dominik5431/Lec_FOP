package campus.ui.view;

import java.util.Date;
import java.text.DateFormat;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import campus.data.domain.Exam;
import campus.data.domain.Grade;
import campus.data.domain.Lecture;

import campus.data.repository.LectureRepository;
import campus.data.repository.ExamRepository;
import campus.data.repository.StudentRepository;

import campus.ui.editor.DateCellEditor;

import campus.ui.form.ExamCreatorForm;
import campus.ui.form.LectureCreatorForm;

import campus.ui.model.AttendeesTableModel;
import campus.ui.model.ExamStudentsTableModel;
import campus.ui.model.LectureExamsTableModel;
import campus.ui.model.LecturesTableModel;

import campus.ui.renderer.AlternatingBooleanCellRenderer;
import campus.ui.renderer.DateCellRenderer;
import campus.ui.renderer.GradeCellRenderer;
import campus.ui.renderer.LectureCellRenderer;


/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class LecturesOverviewPanel extends JPanel {
    private static final long serialVersionUID = 825047441989254469L;

    private static final String LECTURES_TITLED_BORDER_TEXT =
        "Veranstaltungen";
    private static final String LECTURE_EXAMS_TITLED_BORDER_TEXT =
        "Prüfungstermine";
    private static final String EXAM_STUDENTS_TITLED_BORDER_TEXT =
        "Teilnehmende";

    private final LecturesTableModel lecturesModel;
    private final LectureExamsTableModel lectureExamsModel;
    private final ExamStudentsTableModel examStudentsModel;
    private final AttendeesTableModel attendeesModel;

    private final JTable lecturesTable, lectureExamsTable, examStudentsTable;

    private final JButton addLectureButton =
       new JButton("Veranstaltung hinzufügen");
    private final JButton addExamButton = new JButton("Prüfung hinzufügen");
    private final  JButton deleteLectureButton =
       new JButton("Veranstaltung löschen");
    private final JButton deleteExamButton = new JButton("Prüfung löschen");
    private final JButton editAttendeesButton =
        new JButton("Teilnehmer*innen bearbeiten");

    private Lecture selectedLecture;
    private Exam selectedExam;

    public LecturesOverviewPanel(
        LectureRepository lectureRepository,
        ExamRepository examRepository,
        StudentRepository studentRepository
    ) {
        lecturesModel = new LecturesTableModel(lectureRepository);
        lectureExamsModel = new LectureExamsTableModel(examRepository);
        examStudentsModel = new ExamStudentsTableModel(examRepository);
        attendeesModel = new AttendeesTableModel(
            examRepository, studentRepository);

        lecturesTable = new JTable(lecturesModel);
        lectureExamsTable = new JTable(lectureExamsModel);
        examStudentsTable = new JTable(examStudentsModel);

        setupLecturesTable();
        setupExamsTable();
        setupStudentsTable();

        addLectureButton.addActionListener(e ->
            LectureCreatorForm.showDialog(this).ifPresent(lecture -> {
                lecturesModel.addLecture(lecture);

                var row = lecturesTable.convertRowIndexToView(0);
                var col = lecturesTable.convertColumnIndexToView(0);
                var rect = lecturesTable.getCellRect(row, col, true);
                lecturesTable.setRowSelectionInterval(row, row);
                lecturesTable.scrollRectToVisible(rect);
            }));

        addExamButton.addActionListener(e ->
            ExamCreatorForm.showDialog(this).ifPresent(exam -> {
                lectureExamsModel.addExam(exam);

                var row = lectureExamsTable.convertRowIndexToView(0);
                var col = lectureExamsTable.convertColumnIndexToView(0);
                var rect = lectureExamsTable.getCellRect(row, col, true);
                lectureExamsTable.setRowSelectionInterval(row, row);
                lectureExamsTable.scrollRectToVisible(rect);
            }));

        deleteLectureButton.addActionListener(e -> {
            var selectedRow = lecturesTable.getSelectedRow();

            lecturesModel.deleteLecture(selectedLecture);

            var maxIndex = lecturesTable.getRowCount() - 1;
            var newIndex = Math.min(selectedRow, maxIndex);
            if (newIndex >= 0) {
                lecturesTable.setRowSelectionInterval(newIndex, newIndex);
            }
        });

        deleteExamButton.addActionListener(e -> {
            var selectedRow = lectureExamsTable.getSelectedRow();

            lectureExamsModel.deleteExam(selectedExam);

            var maxIndex = lectureExamsTable.getRowCount() - 1;
            var newIndex = Math.min(selectedRow, maxIndex);
            if (newIndex >= 0) {
                lectureExamsTable.setRowSelectionInterval(newIndex, newIndex);
            }
        });

        editAttendeesButton.addActionListener(e -> {
            var model = examStudentsTable.getModel();
            if (model instanceof ExamStudentsTableModel &&
                selectedExam != null) {
                startEditingAttendees();
            } else {
                stopEditingAttendees();
            }
        });

        setupGUI();
        updateButtons();
    }

    private JPanel getLecturesPane() {
        var lecturesPane = new JPanel(new BorderLayout());

        var lecturesButtonPane = new JPanel();
        lecturesButtonPane.setLayout(
            new BoxLayout(lecturesButtonPane, BoxLayout.LINE_AXIS));

        lecturesButtonPane.add(addLectureButton);
        lecturesButtonPane.add(Box.createHorizontalGlue());
        lecturesButtonPane.add(deleteLectureButton);

        var lecturesScrollPane = new JScrollPane(lecturesTable);
        lecturesScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        lecturesPane.add(lecturesScrollPane, BorderLayout.CENTER);
        lecturesPane.add(lecturesButtonPane, BorderLayout.PAGE_END);

        var border = BorderFactory.createTitledBorder(
            LECTURES_TITLED_BORDER_TEXT);
        lecturesPane.setBorder(border);

        return lecturesPane;
    }

    private JPanel getExamsPane() {
        var examsPane = new JPanel(new BorderLayout());

        var examsButtonPane = new JPanel();
        examsButtonPane.setLayout(
            new BoxLayout(examsButtonPane, BoxLayout.LINE_AXIS));

        examsButtonPane.add(addExamButton);
        examsButtonPane.add(Box.createHorizontalGlue());
        examsButtonPane.add(deleteExamButton);

        var examsScrollPane = new JScrollPane(lectureExamsTable);
        examsScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        examsPane.add(examsScrollPane, BorderLayout.CENTER);
        examsPane.add(examsButtonPane, BorderLayout.PAGE_END);

        var border = BorderFactory.createTitledBorder(
            LECTURE_EXAMS_TITLED_BORDER_TEXT);
        examsPane.setBorder(border);

        return examsPane;
    }

    private JPanel getStudentsPane() {
        var studentsPane = new JPanel(new BorderLayout());

        var studentsButtonPane = new JPanel();
        studentsButtonPane.setLayout(
            new BoxLayout(studentsButtonPane, BoxLayout.LINE_AXIS));

        studentsButtonPane.add(Box.createHorizontalGlue());
        studentsButtonPane.add(editAttendeesButton);
        studentsButtonPane.add(Box.createHorizontalGlue());

        var studentsScrollPane = new JScrollPane(examStudentsTable);
        studentsScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        studentsPane.add(studentsScrollPane, BorderLayout.CENTER);
        studentsPane.add(studentsButtonPane, BorderLayout.PAGE_END);

        var border = BorderFactory.createTitledBorder(
            EXAM_STUDENTS_TITLED_BORDER_TEXT);
        studentsPane.setBorder(border);

        return studentsPane;
    }

    private void setupLecturesTable() {
        lecturesTable.setAutoCreateRowSorter(true);
        lecturesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lecturesTable.setDefaultRenderer(
            Lecture.class, new LectureCellRenderer());

        lecturesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                var selectedRow = lecturesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedLecture = lecturesModel.getLectureAt(
                        lecturesTable.convertRowIndexToModel(selectedRow));
                } else {
                    selectedLecture = null;
                }

                lectureExamsModel.setLecture(selectedLecture);
                if (lectureExamsModel.getRowCount() > 0) {
                    lectureExamsTable.setRowSelectionInterval(0, 0);
                }
            }
            stopEditingAttendees();
            updateButtons();
        });

        lecturesTable.getColumnModel().getColumn(
            lecturesTable.convertColumnIndexToView(0))
            .setPreferredWidth(400);

        var height = lecturesTable
            .getPreferredScrollableViewportSize().height;

        lecturesTable.setPreferredScrollableViewportSize(
            new Dimension(lecturesTable.getPreferredSize().width, height));
    }

    private void setupExamsTable() {
        lectureExamsTable.setAutoCreateRowSorter(true);
        lectureExamsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lectureExamsTable.setDefaultRenderer(
            Date.class, new DateCellRenderer(
                DateFormat.getDateTimeInstance(
                    DateFormat.FULL, DateFormat.SHORT)));

        lectureExamsTable.setDefaultEditor(Date.class, new DateCellEditor());

        lectureExamsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                var selectedRow = lectureExamsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedExam = lectureExamsModel.getExamAt(
                        lectureExamsTable.convertRowIndexToModel(selectedRow));
                } else {
                    selectedExam = null;
                }

                examStudentsModel.setExam(selectedExam);
                attendeesModel.setExam(selectedExam);
            }
            stopEditingAttendees();
            updateButtons();
        });

        lectureExamsTable.getColumnModel().getColumn(
            lectureExamsTable.convertColumnIndexToModel(0))
            .setPreferredWidth(70);

        var height = lectureExamsTable
            .getPreferredScrollableViewportSize().height;

        lectureExamsTable.setPreferredScrollableViewportSize(
            new Dimension(lectureExamsTable.getPreferredSize().width, height));
    }

    private void setupStudentsTable() {
        examStudentsTable.setAutoCreateRowSorter(true);
        examStudentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        examStudentsTable.setDefaultRenderer(
            Boolean.class, new AlternatingBooleanCellRenderer());

        examStudentsTable.setDefaultRenderer(
            Grade.class, new GradeCellRenderer());

        var gradesComboBox = new JComboBox<>(Grade.values());
        gradesComboBox.setMaximumRowCount(Integer.MAX_VALUE);
        examStudentsTable.setDefaultEditor(
            Grade.class, new DefaultCellEditor(gradesComboBox));

        examStudentsTable.getColumnModel().getColumn(
            examStudentsTable.convertColumnIndexToView(0))
            .setPreferredWidth(150);
        examStudentsTable.getColumnModel().getColumn(
            examStudentsTable.convertColumnIndexToView(1))
            .setPreferredWidth(150);
        examStudentsTable.getColumnModel().getColumn(
            examStudentsTable.convertColumnIndexToView(2))
            .setPreferredWidth(70);

        var height = examStudentsTable
            .getPreferredScrollableViewportSize().height;

        examStudentsTable.setPreferredScrollableViewportSize(
            new Dimension(examStudentsTable.getPreferredSize().width, height));
    }

    private void setupGUI() {
        setLayout(new BorderLayout());
        add(getLecturesPane(), BorderLayout.LINE_START);
        add(getExamsPane(), BorderLayout.CENTER);
        add(getStudentsPane(), BorderLayout.LINE_END);
    }

    private void startEditingAttendees() {
        attendeesModel.reload();
        examStudentsTable.setModel(attendeesModel);
        setupStudentsTable();
        editAttendeesButton.setText("Fertig");
    }

    private void stopEditingAttendees() {
        examStudentsModel.reload();
        examStudentsTable.setModel(examStudentsModel);
        setupStudentsTable();
        editAttendeesButton.setText("Teilnehmende bearbeiten");
    }

    private void updateButtons() {
        deleteLectureButton.setEnabled(selectedLecture != null);
        addExamButton.setEnabled(selectedLecture != null);
        deleteExamButton.setEnabled(selectedExam != null);
        editAttendeesButton.setEnabled(selectedExam != null);
    }
}
