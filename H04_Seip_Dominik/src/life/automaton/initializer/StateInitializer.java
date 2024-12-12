package life.automaton.initializer;

import life.automaton.state.AutomatonState;

/**
 * Dieses Interface bildet die abstrakte Grundlage der Factory-Klassen, mit
 * denen sich Zustände von zellulären Automaten erzeugen lassen.
 * Hierfür ist die Methode {@link #createState} verantwortlich.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see AutomatonState
 */
public interface StateInitializer {
    /**
     * Erzeugt den Zustand eines zellulären Automaten basierend auf der
     * Konfiguration des konkreten {@code StateInitializer}.
     * <br>
     * Eine Implementation dieser Methode sollte einen validen Zustand liefern,
     * d.h. dieser sollte eine Höhe bzw. Breite von mindestens 1 haben und für
     * jede kombination von natürlichen Zahlen {@code i} und {@code j}
     * mit {@code 0 &lt;= i &lt; h} und {@code 0 &lt;= j &lt; w}
     * valide Ergebnisse für {@code isAlive(i, j)} liefern, wobei
     * {@code h} und {@code w} die Höhe und Breite des Automaten sind.
     * <br>
     * Außerdem sollte sich diese Methode mehrmals aufrufen lassen, um mehrere
     * Zustände mit derselben Konfiguration zu erzeugen.
     *
     * @return den von der Factory erzeugten Zustand
     */
    AutomatonState createState();
}
