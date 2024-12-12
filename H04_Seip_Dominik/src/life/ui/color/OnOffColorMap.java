package life.ui.color;

import java.awt.Color;

import life.ui.GameOfLifePanel;

/**
 * Ein zweifarbiges Farbschema zur Darstellung von Zellen in einem zellulären
 * Automaten.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see GameOfLifePanel
 */
public class OnOffColorMap implements CellColorMap {
    private Color onColor;
    private Color offColor;

    /**
     * Erzeugt ein Farbschema mit den übergebenen Farben zur Darstellung von
     * lebendigen bzw. toten Zellen.
     *
     * @param onColor  die Farbe zur Darstellungen lebendiger Zellen
     * @param offColor die Farbe zur Darstellungen toter Zellen
     */
    public OnOffColorMap(Color onColor, Color offColor) {
        this.onColor = onColor;
        this.offColor = offColor;
    }

    @Override
    public Color getColor(boolean isAlive) {
        return isAlive ? onColor : offColor;
    }
}
