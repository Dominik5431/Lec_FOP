package life.test;

import life.automaton.CellularAutomaton;
import life.automaton.state.AutomatonState;
import life.automaton.neighborhood.Neighborhood;
import life.automaton.neighborhood.Padding;
import life.automaton.initializer.StateInitializer;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class TestUtils {
    static final boolean T = true;
    static final boolean F = false;

    static final Padding MOCK_PADDING = (state, row, col) ->
        row < 0 || row >= state.getHeight() ||
        col < 0 || col >= state.getWidth() ||
        state.isAlive(row, col);

    static final boolean[][] SMALL_STATE_ARRAY = new boolean[][] {
        {F, F},
        {F, F},
        {F, T},
        {F, F}
    };

    static final boolean[][] BIG_STATE_ARRAY = new boolean[][] {
        {F, F, F, F, F, F, F, F, F, F, F, F, F, F, F},
        {F, T, F, F, F, T, T, F, F, F, F, F, F, F, F},
        {F, F, F, F, F, T, T, F, F, T, T, T, F, F, F},
        {F, F, F, F, F, F, F, F, F, T, T, F, F, F, F},
        {F, F, T, T, F, F, F, F, F, F, T, T, F, F, F},
        {F, T, F, T, F, F, F, F, T, F, T, F, F, F, F},
        {F, F, F, T, F, F, T, F, F, F, F, F, F, T, T},
        {F, F, F, F, F, F, T, F, F, F, T, T, F, T, F},
        {F, F, F, F, F, F, T, F, F, F, F, T, F, F, F},
        {T, T, F, F, F, F, F, F, F, F, F, T, F, F, F}
    };

    private static abstract class MockState implements AutomatonState {
        @Override
        public void kill(int row, int col) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void giveBirth(int row, int col) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AutomatonState copy() {
            throw new UnsupportedOperationException();
        }
    }

    static final AutomatonState SMALL_STATE = new MockState() {
        @Override
        public boolean isAlive(int row, int col) {
            return SMALL_STATE_ARRAY[row][col];
        }

        @Override
        public int getHeight() {
            return SMALL_STATE_ARRAY.length;
        }

        @Override
        public int getWidth() {
            return SMALL_STATE_ARRAY[0].length;
        }
    };

    static final AutomatonState BIG_STATE = new MockState() {
        @Override
        public boolean isAlive(int row, int col) {
            return BIG_STATE_ARRAY[row][col];
        }

        @Override
        public int getHeight() {
            return BIG_STATE_ARRAY.length;
        }

        @Override
        public int getWidth() {
            return BIG_STATE_ARRAY[0].length;
        }
    };

    static boolean[][] automatonToArray(CellularAutomaton automaton) {
        var result = new boolean[automaton.getHeight()][automaton.getWidth()];

        for (var row = 0; row < automaton.getHeight(); row++) {
            for (var col = 0; col < automaton.getWidth(); col++) {
                result[row][col] = automaton.isAlive(row, col);
            }
        }

        return result;
    }

    static boolean[][] stateToArray(AutomatonState state) {
        var result = new boolean[state.getHeight()][state.getWidth()];

        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                result[row][col] = state.isAlive(row, col);
            }
        }

        return result;
    }

    static void addToHistogram(int[][] accumulator, AutomatonState state) {
        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                accumulator[row][col] += state.isAlive(row, col) ? 1 : 0;
            }
        }
    }

    static double aliveRatio(AutomatonState state) {
        var numAlive = 0;

        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                numAlive += state.isAlive(row, col) ? 1 : 0;
            }
        }

        return (double) numAlive / (state.getHeight() * state.getWidth());
    }

    static int[][] countAliveNeighborsForEachCell(
            AutomatonState state, Neighborhood neighborhood, Padding padding) {
        var neighbors = new int[state.getHeight()][state.getWidth()];

        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                neighbors[row][col] = neighborhood.getNumberOfAliveNeighbors(
                    state, row, col, padding);
            }
        }

        return neighbors;
    }

    static <T> T initializeClass(String className, Class<? extends T> cls)
        throws InitializationException {
        try {
            return Class.forName(className)
                .asSubclass(cls)
                .getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new InitializationException(
                className + " not found", e);
        } catch (ClassCastException e) {
            throw new InitializationException(
                className + " does not implement " + cls, e);
        } catch (ReflectiveOperationException e) {
            throw new InitializationException(
                className +
                " is abstract or does not provide an empty constructor", e);
        }
    }

    static StateInitializer initializeRandomizer(int height, int width, double rate)
        throws InitializationException {
        var className = "life.automaton.initializer.RandomInitializer";
        try {
            return Class.forName(className)
                .asSubclass(StateInitializer.class)
                .getDeclaredConstructor(int.class, int.class, double.class)
                .newInstance(height, width, rate);
        } catch (ClassNotFoundException e) {
            throw new InitializationException(
                className + " not found", e);
        } catch (ClassCastException e) {
            throw new InitializationException(
                className + " does not implement " + StateInitializer.class, e);
        } catch (ReflectiveOperationException e) {
            throw new InitializationException(
                className +
                " is abstract or does not provide an empty constructor", e);
        }
    }
}
