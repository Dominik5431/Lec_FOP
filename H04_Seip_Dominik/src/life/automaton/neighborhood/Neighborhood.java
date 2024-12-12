package life.automaton.neighborhood;

import life.automaton.state.AutomatonState;

/**
 * Dieses Interface definiert eine Strategie zur Bestimmung von Nachbarzellen in
 * einem zellulären Automaten.
 * Diese verwendet ein {@link Padding} um den Zustand der als Nachbarn
 * betrachteten Zellen abzufragen, damit eine einheitliche Behandlung aller
 * Zellen, auch derer am Spielfeldrand, möglich ist.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see Padding
 * @see AutomatonState
 */
public interface Neighborhood {
    /**
     * Bestimmt die Anzahl der lebendigen Nachbarzellen der Zelle in Zeile
     * {@code row} und Spalte {@code col} im Zustand {@code state}.
     * Eine Implementierung dieser Methode sollte die Methode
     * {@link Padding#isAlive} der übergebenen Padding-Strategie {@code padding}
     * verwenden, um den Zustand der als Nachbarn betrachteten Zellen
     * abzufragen.
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
    int getNumberOfAliveNeighbors(
        AutomatonState state, int row, int col, Padding padding);
}
