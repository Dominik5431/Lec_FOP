package life.ui.color;

import java.awt.Color;

import life.ui.GameOfLifePanel;

/**
 * Dieses Interface definiert eine Methode zur Bestimmung der Farbe einer Zelle
 * abhängig von ihrem Zustand.
 * 
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see GameOfLifePanel
 */
public interface CellColorMap {
    /**
     * Liefert die Farbe, mit der eine Zelle in einem {@link GameOfLifePanel}
     * dargestellt werden soll, abhängig von ihrem momentanem Zustand.
     *
     * @param isAlive der Zustand der Zelle
     *
     * @return die Farbe, mit der die Zelle dargestellt werden soll
     */
    Color getColor(boolean isAlive);
}
