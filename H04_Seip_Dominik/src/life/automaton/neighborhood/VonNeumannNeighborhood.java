package life.automaton.neighborhood;

import life.automaton.state.AutomatonState;

/**
 * Repräsentiert eine Von-Neumann-Nachbarschaft, also eine Nachbarschaft, bei
 * der die vier horizontal und vertikal angrenzenden Zellen als die Nachbarn
 * einer Zelle betrachtet werden.
 * <br>
 * Im folgenden Schaubild ist die betrachtete Zelle mit einem x makriert,
 * während die Zellen in ihrer Von-Neumann-Nachbarschaft mit einem o angedeutet
 * werden. Die übrigen Zellen werden jeweils durch einen Punkt dargestellt.
 * <pre> ........
 * ...o....
 * ..oxo...
 * ...o....
 * ........</pre>
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see Padding
 * @see AutomatonState
 */
public class VonNeumannNeighborhood implements Neighborhood {
    /**
     * Bestimmt die Anzahl der lebendigen Nachbarzellen der Zelle in Zeile
     * {@code row} und Spalte {@code col} im Zustand {@code state} basierend auf
     * einer Von-Neumann-Nachbarschaft.
     *
     * @param state   der Zustand des Automaten
     * @param row     die Zeile der Zelle, deren lebendige Nachbarn bestimmt
     *                werden sollen
     * @param col     die Spalte der Zelle, deren lebendige Nachbarn bestimmt
     *                werden sollen
     * @param padding die Padding-Strategie, die für die Abfrage der
     *                Zellzustände verwendet werden soll
     *
     * @return die Anzahl der lebendigen Nachbarn der Zelle mit den Koordinaten
     *         {@code row} und {@code col}
     *
     * @see Padding#isAlive
     */
    @Override
    public int getNumberOfAliveNeighbors(
        AutomatonState state, int row, int col, Padding padding) {
        var result = 0;
        result += padding.isAlive(state, row, col+1) ? 1 : 0;
        result += padding.isAlive(state, row, col-1) ? 1 : 0;
        result += padding.isAlive(state, row+1, col) ? 1 : 0;
        result += padding.isAlive(state, row-1, col) ? 1 : 0;
        return result;
    }
}
