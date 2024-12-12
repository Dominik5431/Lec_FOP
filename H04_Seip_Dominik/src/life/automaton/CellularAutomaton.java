package life.automaton;

/**
 * Repräsentiert einen allgemeinen zellulären Automaten mit rechteckigem
 * Spielfeld.
 * <br>
 * Zur Instantiierung eines {@code CellularAutomaton} mit frei wählbaren
 * Komponenten kann das Builder-Interface {@link CellularAutomatonBuilder}
 * herangezogen werden.
 * <br>
 * Bei einer Implementierung dieses Interfaces sollte darauf geachtet werden,
 * dass die Indizierung der Zellen bei 0 beginnt und die Zelle mit dem Index
 * (0, 0) in der oberen linken Ecke des Spielfeldes angesiedelt ist.
 * Die erste Koordinate entspricht hier der Zeile und die zweite der Spalte.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see CellularAutomatonBuilder
 */
public interface CellularAutomaton {
    /**
     * Liefert den momentanen Zustand der Zelle an den gegebenen Koordinaten.
     *
     * @param row die Zeile der Zelle, deren Zustand bestimmt werden soll
     * @param col die Spalte der Zelle, deren Zustand bestimmt werden soll
     *
     * @return {@code true} genau dann, wenn die Zelle an den gegebenen
     *         Koordinaten im momentanen Zustand am Leben ist
     */
    boolean isAlive(int row, int col);

    /**
     * Liefert die Höhe des Spielfeldes, auf dem dieser Automat basiert.
     *
     * @return die Höhe des Spielfeldes
     */
    int getHeight();

    /**
     * Liefert die Breite des Spielfeldes, auf dem dieser Automat basiert.
     *
     * @return die Breite des Spielfeldes
     */
    int getWidth();

    /**
     * Überführt den Zustand dieses Automaten in den entsprechenden
     * Folgezustand.
     */
    void update();
    
    /**
     * Setzt den Zustand dieses Automaten auf den Zustand zum Zeitpunkt der
     * Initialisierung zurück.
     */
    void reset();
}
