package life.automaton.state;

/**
 * Eine Implementation von {@link AutomatonState}, die auf einem
 * zweidimensionalen {@code boolean}-Array basiert.
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
public class ArrayAutomatonState implements AutomatonState {
    private boolean[][] state;

    /**
     * Erzeugt einen neuen {@code ArrayAutomatonState} mit gegebenener Höhe und
     * Breite.
     * Zunächst sind alle Zellen in diesem Zustand tot.
     *
     * @param height die Höhe des Spielfeldes im erzeugten Zustand
     * @param width  die Breite des Spielfeldes im erzeugten Zustand
     *
     * @throws IllegalArgumentException falls für {@code height} oder
     *                                  {@code width} ein nicht-positiver Wert
     *                                  übergeben wird
     */
    public ArrayAutomatonState(int height, int width) {
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException(
                "Höhe und Breite sollten größer als 0 sein");
        }
        state = new boolean[height][width];
    }

    @Override
    public boolean isAlive(int row, int col) {
        return state[row][col];
    }

    @Override
    public void giveBirth(int row, int col) {
        state[row][col] = true;
    }

    @Override
    public void kill(int row, int col) {
        state[row][col] = false;
    }

    @Override
    public int getHeight() {
        return state.length;
    }

    @Override
    public int getWidth() {
        return state[0].length;
    }

    @Override
    public AutomatonState copy() {
        var result = new ArrayAutomatonState(getHeight(), getWidth());
        for (var row = 0; row < getHeight(); row++) {
            for (var col = 0; col < getWidth(); col++) {
                if (isAlive(row, col)) {
                    result.giveBirth(row, col);
                }
            }
        }
        return result;
    }
}
