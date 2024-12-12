package life.ui.color;

import java.awt.Color;

import life.ui.GameOfLifePanel;

/**
 * Ein Farbschema zur Darstellung von Zellen in einem zellulären Automaten, bei
 * dem lebendige Zellen in einer einheitlichen Farbe angezeigt werden.
 * Tote Zellen werden nicht gezeichnet.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see GameOfLifePanel
 */
public class TransparencyColorMap implements CellColorMap {
    private Color color;

    /**
     * Erzeugt ein Farbschema mit der übergebenen Farben zur Darstellung von
     * lebendigen Zellen.
     *
     * @param color die Farbe zur Darstellungen lebendiger Zellen
     */
    public TransparencyColorMap(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor(boolean isAlive) {
        return new Color(
            color.getColorSpace(),
            color.getColorComponents(null),
            isAlive ? 1 : 0);
    }
}
