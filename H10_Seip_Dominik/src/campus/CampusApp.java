package campus;

import java.util.Date;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import campus.data.domain.Grade;
import campus.data.domain.GradeType;

import campus.data.query.Attribute;

import campus.data.query.csv.CSVDatabaseDriver;
import campus.data.query.csv.CSVTableTypeMap;

import campus.data.repository.ExamRepository;
import campus.data.repository.LectureRepository;
import campus.data.repository.StudentRepository;

import campus.data.repository.ExamRepositoryImpl;
import campus.data.repository.LectureRepositoryImpl;
import campus.data.repository.StudentRepositoryImpl;

import static campus.data.query.AttributeOption.AUTOINCREMENT;
import static campus.data.query.AttributeOption.NOTNULL;
import static campus.data.query.AttributeOption.UNIQUE;

import campus.ui.CampusMainWindow;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class CampusApp {
    public static void main(String[] args) {
        File databaseFile;
        if (args.length >= 1) {
            databaseFile = new File(args[0]);
        } else {
            var fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            var fileChooserResult = fileChooser.showOpenDialog(null);

            if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
                databaseFile = fileChooser.getSelectedFile();
            } else {
                return;
            }
        }

        var typeMap = CSVTableTypeMap.defaultTypeMap();
        typeMap.registerType(Grade.class, "GRADE", new GradeType());

        var driver = new CSVDatabaseDriver(typeMap);

        var database = driver.connect(databaseFile.toString(), true);

        database.createTable("lecture", true,
            new Attribute("id", Long.class, AUTOINCREMENT),
            new Attribute("title", String.class, UNIQUE, NOTNULL));

        database.createTable("exam", true,
            new Attribute("id", Long.class, AUTOINCREMENT),
            new Attribute("date", Date.class, NOTNULL),
            new Attribute("lecture_id", Long.class, NOTNULL));

        database.createTable("student", true,
            new Attribute("id", Long.class, AUTOINCREMENT),
            new Attribute("lastname", String.class, NOTNULL),
            new Attribute("firstname", String.class, NOTNULL));

        database.createTable("student_exam", true,
            new Attribute("id", Long.class, AUTOINCREMENT),
            new Attribute("student_id", Long.class, NOTNULL),
            new Attribute("exam_id", Long.class, NOTNULL),
            new Attribute("grade", Grade.class));

        var lectureRepository = new LectureRepositoryImpl(database);
        var examRepository = new ExamRepositoryImpl(database);
        var studentRepository = new StudentRepositoryImpl(database);

        SwingUtilities.invokeLater(() -> createAndShowGUI(
            lectureRepository, examRepository, studentRepository));
    }

    private static void createAndShowGUI(
        LectureRepository lectureRepository,
        ExamRepository examRepository,
        StudentRepository studentRepository
    ) {
        var mainWindow = new CampusMainWindow(
            lectureRepository, examRepository, studentRepository);

        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
    }
}
