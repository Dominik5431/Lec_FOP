package campus.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import campus.data.repository.LectureRepository;
import campus.data.repository.ExamRepository;
import campus.data.repository.StudentRepository;

import campus.ui.view.CampusLogo;
import campus.ui.view.LecturesOverviewPanel;
import campus.ui.view.StudentsOverviewPanel;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class CampusMainWindow extends JFrame {
    private static final long serialVersionUID = -1987064181161734328L;

    public CampusMainWindow(
        LectureRepository lectureRepository,
        ExamRepository examRepository,
        StudentRepository studentRepository) {
        super("Campus");

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            System.err.println("Failed to set L&F");
//        }

        UIManager.put("TabbedPane.foreground", Color.BLACK);

        UIManager.put("Table.alternateRowColor", new Color(232, 232, 232));

        UIManager.put("Table.selectionForeground", Color.BLACK);
        UIManager.put("Table.selectionBackground", new Color(180, 187, 59));

        UIManager.put("Table.focusCellHighlightBorder",
            BorderFactory.createEmptyBorder());

        UIManager.put("Table.sortIconColor", Color.WHITE);
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("TableHeader.background", new Color(156, 164, 52));

        var lecturesPane = new LecturesOverviewPanel(
            lectureRepository, examRepository, studentRepository);

        var studentsPane = new StudentsOverviewPanel(
            examRepository, studentRepository);

        var tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Veranstaltungen und Prüfungen", lecturesPane);
        tabbedPane.addTab("Studierende", studentsPane);

        var logo = new CampusLogo();
        logo.setPreferredSize(new Dimension(100, 100));
        logo.setMaximumSize(new Dimension(100, 100));

        var label = new JLabel("<html>"
            + "<span style=\"color:#969696;\">CaMP</span>"
            + "<span style=\"color:#9CA434;\">US</span>"
            + "</html>");
        var font = new Font("Verdana", Font.PLAIN, 96);
        label.setFont(font);

        var titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.LINE_AXIS));
        titlePanel.add(logo);
        titlePanel.add(Box.createHorizontalStrut(10));
        titlePanel.add(label);
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 5));

        getContentPane().add(titlePanel, BorderLayout.PAGE_START);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
}
