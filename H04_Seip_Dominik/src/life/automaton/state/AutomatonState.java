package life.automaton.state;

import life.automaton.CellularAutomaton;

/**
 * Repräsentiert den Zustand eines zellulären Automaten mit rechteckigem
 * Spielfeld.
 * Dieses Interface definiert die Methoden, mit denen ein zellulärer Automat
 * die Zustände einzelner Zellen steuern kann.
 * <br>
 * Wie in {@link CellularAutomaton} stehen die Koordinaten (0, 0) hier für
 * die Zelle in der oberen linken Ecke.
 * Die erste Koordinate gibt die Zeile und die zweite die Spalte an, in der sich
 * die korrespondierende Zelle befindet.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see CellularAutomaton
 */
public interface AutomatonState {
    /**
     * Liefert den momentanen Zustand der Zelle an den gegebenen Koordinaten.
     *
     * @param row die Zeile der Zelle, deren Zustand bestimmt werden soll
     * @param col die Spalte der Zelle, deren Zustand bestimmt werden soll
     *
     * @return {@code true} genau dann, wenn die Zelle an den gegebenen
     *         Koordinaten in diesem Zustand am Leben ist
     *
     * @see CellularAutomaton#isAlive(int, int)
     */
    boolean isAlive(int row, int col);

    /**
     * Setzt den Zustand der Zelle an den gegebenen Koordinaten auf lebendig.
     * Sollte die entsprechende Zelle bereits am Leben sein, ändert sich ihr
     * Zustand nicht.
     *
     * @param row die Zeile der Zelle, die belebt werden soll
     * @param col die Spalte der Zelle, die belebt werden soll
     */
    void giveBirth(int row, int col);

    /**
     * Setzt den Zustand der Zelle an den gegebenen Koordinaten auf tot. Sollte
     * die entsprechende Zelle bereits tot sein, ändert sich ihr Zustand nicht.
     *
     * @param row die Zeile der Zelle, die getötet werden soll
     * @param col die Spalte der Zelle, die getötet werden soll
     */
    void kill(int row, int col);

    /**
     * Liefert die Höhe des Spielfeldes, auf dem dieser Zustand basiert.
     *
     * @return die Höhe des Spielfeldes
     */
    int getHeight();

    /**
     * Liefert die Breite des Spielfeldes, auf dem dieser Zustand basiert.
     *
     * @return die Breite des Spielfeldes
     */
    int getWidth();

    /**
     * Erzeugt eine Kopie dieses Zustandes.
     * Änderungen an dem Ergebnis dieser Methode sollten sich nicht auf diesen
     * Zustand auswirken.
     *
     * @return eine Kopie dieses Zustandes
     */
    AutomatonState copy();
}
