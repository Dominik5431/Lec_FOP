package life.ui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import life.automaton.CellularAutomaton;
import life.ui.color.CellColorMap;

/**
 * Diese Komponente kann verwendet werden, um den Zustand eines zellulären
 * Automaten darzustellen.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see GameOfLifeFrame
 */
public class GameOfLifePanel extends JPanel {
    private static final long serialVersionUID = 1204956243706303231L;

    private static final Dimension PREFERRED_MAXIMUM_PANEL_DIMENSION =
        new Dimension(800, 800);

    private CellularAutomaton automaton;
    private CellColorMap colorMap;

    /**
     * Erzeugt ein {@code GameOfLifePanel}, das verwendet werden kann, um den
     * übergebenen zellulären Automaten im entsprechenden Farbschema
     * darzustellen.
     *
     * @param automaton der darzustellende Automat
     * @param colorMap  das Farbschema, in dem die Zellzustände dargestellt
     *                  werden sollen
     *
     * @see CellColorMap#getColor(boolean)
     */
    public GameOfLifePanel(CellularAutomaton automaton, CellColorMap colorMap) {
        this.automaton = automaton;
        this.colorMap = colorMap;

        setMinimumSize(new Dimension(
            automaton.getWidth(),
            automaton.getHeight()));

        var preferredCellSize = getCellSize(PREFERRED_MAXIMUM_PANEL_DIMENSION);

        setPreferredSize(new Dimension(
            preferredCellSize * automaton.getWidth(),
            preferredCellSize * automaton.getHeight()));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        var dimension = getSize();

        var cellSize = getCellSize(dimension);

        var width = automaton.getWidth() * cellSize;
        var height = automaton.getHeight() * cellSize;

        g.translate(
            Math.floorDiv(dimension.width - width, 2),
            Math.floorDiv(dimension.height - height, 2));

        for (var col = 0; col < automaton.getWidth(); col++) {
            for (var row = 0; row < automaton.getHeight(); row++) {
                g.setColor(colorMap.getColor(automaton.isAlive(row, col)));
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
    }

    private int getCellSize(Dimension d) {
        return (int) Math.min(
            (float) d.width / automaton.getWidth(),
            (float) d.height / automaton.getHeight());
    }
}
