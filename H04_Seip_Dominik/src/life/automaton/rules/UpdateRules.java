package life.automaton.rules;

/**
 * Definiert das Regelwerk, mit dem der Folgezustand einer Zelle abhängig ihres
 * momentanen Zustandes und der Anzahl ihrer lebendigen Nachbar ermittelt wird.
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
public interface UpdateRules {
    /**
     * Liefert den Folgezustand einer Zelle basierend auf diesem Regelwerk.
     *
     * @param isAlive        der momentane Zustand einer Zelle
     * @param aliveNeighbors die Anzahl der lebendigen Nachbarn einer Zelle
     *
     * @return {@code true} genau dann, wenn besagte Zelle im nächsten
     *         Zeitschritt am Leben sein soll
     */
    boolean getNextCellState(boolean isAlive, int aliveNeighbors);
}
