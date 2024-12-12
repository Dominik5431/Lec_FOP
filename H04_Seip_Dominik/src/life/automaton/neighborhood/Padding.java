package life.automaton.neighborhood;

import life.automaton.state.AutomatonState;

/**
 * Dieses Interface definiert eine allgemeine Padding-Strategie, also das
 * Verfahren, mit dem die Nachbarschaft einer am Spielfeldrand gelegenen Zelle
 * bestimmt werden soll.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see Neighborhood
 * @see AutomatonState
 */
public interface Padding {
    /**
     * Ruft den Zustand der Zelle in der Zeile {@code row} und Spalte
     * {@code col} im Automatenzustand {@code state} ab und liefert {@code true}
     * genau dann wenn die entsprechende Zelle am Leben ist.
     * Sollten die übergebenen Koordinaten außerhalb des Spielfeldes liegen,
     * werden sie entsprechend der hier definierten Padding-Strategie auf ein
     * Feld innerhalb des Spielfeldes projiziert.
     *
     * @param state der Zustand des Automaten
     * @param row   die Zeile der Zelle, deren Zustand bestimmt werden soll
     * @param col   die Spalte der Zelle, deren Zustand bestimmt werden soll
     *
     * @return {@code true} genau dann wenn die Koordinaten auf eine valide
     *         Position innerhalb des Spielfeldes verweisen und die
     *         entsprechende Zelle am Leben ist, oder wenn die hier definierte
     *         Padding-Strategie die Koordinaten auf das Feld einer lebendigen
     *         Zelle projizieren würde
     *
     * @see Neighborhood#getNumberOfAliveNeighbors
     * @see life.automaton.state.AutomatonState#isAlive
     */
    boolean isAlive(AutomatonState state, int row, int col);
}
