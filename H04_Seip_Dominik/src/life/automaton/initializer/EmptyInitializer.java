package life.automaton.initializer;

import life.automaton.state.AutomatonState;
import life.automaton.state.ArrayAutomatonState;

/**
 * Initialisiert den Zustand eines zellulären Automaten mit gegebener Größe,
 * sodass zunächst keine der Zellen am Leben ist.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see AutomatonState
 * @see ArrayAutomatonState
 */
public class EmptyInitializer implements StateInitializer {
    private int height;
    private int width;

    /**
     * Initialisiert eine Factory, die imstande ist, den Zustand eines
     * zellulären Automaten mit ausschließlich toten Zellen zu erzeugen.
     *
     * @param height die Höhe des Zustandes
     * @param width  die Breite des Zustandes
     */
    public EmptyInitializer(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * Erzeugt den Zustand eines zellulären Automaten mit auschließlich toten
     * Zellen.
     *
     * @return den von der Factory erzeugten Zustand
     */
    @Override
    public AutomatonState createState() {
        return new ArrayAutomatonState(height, width);
    }
}
