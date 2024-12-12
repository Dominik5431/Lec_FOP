package life.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;

import life.automaton.CellularAutomaton;
import life.ui.color.CellColorMap;

/**
 * Ein Fenster zur Darstellung und Steuerung der Simulation eines zellulären
 * Automaten.
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
public class GameOfLifeFrame extends JFrame {
    private static final long serialVersionUID = -6770683815791541723L;

    private static final String WINDOW_TITLE = "Game of Life";

    private static final int INITIAL_FPS = 15;

    private static final String START_BUTTON_TEXT = "Start";
    private static final String STOP_BUTTON_TEXT = "Stopp";
    private static final String STEP_BUTTON_TEXT = ">";
    private static final String RESET_BUTTON_TEXT = "Zurücksetzen";

    private CellularAutomaton automaton;
    private GameOfLifePanel panel;

    private Timer timer;
    private boolean isRunning = false;

    private JButton startStopButton;
    private JButton stepButton;

    /**
     * Erzeugt ein neues {@code GameOfLifeFrame} zur Darstellung des übergebenen
     * Automaten.
     *
     * @param automaton der Automat, der in diesem Frame dargestellt werden soll
     * @param colorMap  das Farbschema, das zur Darstellung der Zellen verwendet
     *                  werden soll
     */
    public GameOfLifeFrame(CellularAutomaton automaton, CellColorMap colorMap) {
        super(WINDOW_TITLE);

        this.automaton = automaton;

        panel = new GameOfLifePanel(automaton, colorMap);

        timer = new Timer(Math.floorDiv(1000, INITIAL_FPS), e -> tick());

        startStopButton = new JButton(START_BUTTON_TEXT);
        startStopButton.addActionListener(e -> toggleRunning());

        stepButton = new JButton(STEP_BUTTON_TEXT);
        stepButton.addActionListener(e -> tick());

        var contentPane = getContentPane();
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(createControlsPanel(), BorderLayout.PAGE_END);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });
    }

    private JPanel createControlsPanel() {
        var resetButton = new JButton(RESET_BUTTON_TEXT);
        resetButton.addActionListener(e -> reset());

        var speedSlider = new JSlider(1, 60,
            Math.floorDiv(1000, timer.getDelay()));
        speedSlider.addChangeListener(e -> {
            var value = ((JSlider) e.getSource()).getValue();
            this.timer.setDelay(Math.floorDiv(1000, value));
        });
        speedSlider.setLabelTable(new Hashtable<>(Map.of(
            1, new JLabel("1 FPS"),
            30, new JLabel("30 FPS"),
            60, new JLabel("60 FPS"))));
        speedSlider.setPaintLabels(true);
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setPaintTicks(true);

        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        panel.add(Box.createGlue());
        panel.add(startStopButton);
        panel.add(stepButton);
        panel.add(resetButton);
        panel.add(speedSlider);
        panel.add(Box.createGlue());

        return panel;
    }

    private void tick() {
        automaton.update();
        panel.repaint();
    }

    private void start() {
        isRunning = true;
        timer.start();
        startStopButton.setText(STOP_BUTTON_TEXT);
        stepButton.setEnabled(false);
    }

    private void stop() {
        isRunning = false;
        timer.stop();
        startStopButton.setText(START_BUTTON_TEXT);
        stepButton.setEnabled(true);
    }

    private void reset() {
        stop();
        automaton.reset();
        panel.repaint();
    }

    private void toggleRunning() {
        if (isRunning) {
            stop();
        }
        else {
            start();
        }
    }
}
