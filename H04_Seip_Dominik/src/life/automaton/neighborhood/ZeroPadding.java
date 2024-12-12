package life.automaton.neighborhood;

import life.automaton.state.AutomatonState;

/**
 * Eine Padding-Strategie, die das Spielfeld um einen virtuellen unendlich
 * großen Rahmen von toten Zellen erweitert.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see Neighborhood
 * @see AutomatonState
 */
public class ZeroPadding implements Padding {
    @Override
    public boolean isAlive(AutomatonState state, int row, int col) {
        return 0 <= row && row < state.getHeight() &&
               0 <= col && col < state.getWidth() &&
               state.isAlive(row, col);
    }
}
