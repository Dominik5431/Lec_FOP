package life.test;

import org.junit.jupiter.api.Test;

import life.automaton.neighborhood.Padding;
import life.automaton.state.AutomatonState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import static life.test.TestUtils.BIG_STATE;
import static life.test.TestUtils.initializeClass;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class PaddingTest {
    static final String ZERO_PADDING =
        "life.automaton.neighborhood.ZeroPadding";

    static final String SAME_PADDING =
        "life.automaton.neighborhood.SamePadding";

    static final String DONUT_PADDING =
        "life.automaton.neighborhood.DonutPadding";

    static final String ERROR_FORMAT =
        "Bei einer Höhe von %d und Breite von %d sollte der Zugriff auf" +
        " (%d, %d) den Zustand der Zelle (%d, %d) liefern";


    @Test
    void testZeroPadding() {
        var state = BIG_STATE;
        var maxOffset = 5;

        try {
            var padding = initializeClass(ZERO_PADDING, Padding.class);

            for (var row = 0; row < state.getHeight(); row++) {
                for (var col = 0; col < state.getWidth(); col++) {
                    assertSameCoordinates(
                        state, padding,
                        row, col,
                        row, col);
                }
            }

            for (var offset = 1; offset <= maxOffset; offset++) {
                for (var row = 0; row < state.getHeight(); row++) {
                    assertFalse(padding.isAlive(
                        state, row, -offset));
                    assertFalse(padding.isAlive(
                        state, row, state.getWidth() - 1 + offset));
                }

                for (var col = 0; col < state.getWidth(); col++) {
                    assertFalse(padding.isAlive(
                        state, -offset, col));
                    assertFalse(padding.isAlive(
                        state, state.getHeight() - 1 + offset, col));
                }
            }
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testSamePadding() {
        var state = BIG_STATE;
        var maxOffset = 5;

        try {
            var padding = initializeClass(SAME_PADDING, Padding.class);

            for (var row = 0; row < state.getHeight(); row++) {
                for (var col = 0; col < state.getWidth(); col++) {
                    assertSameCoordinates(
                        state, padding,
                        row, col,
                        row, col);
                }
            }

            for (var offset = 1; offset <= maxOffset; offset++) {
                for (var row = 0; row < state.getHeight(); row++) {
                    assertSameCoordinates(
                        state, padding,
                        row, 0,
                        row, -offset);
                    assertSameCoordinates(
                        state, padding,
                        row, state.getWidth() - 1,
                        row, state.getWidth() - 1 + offset);
                }

                for (var col = 0; col < state.getWidth(); col++) {
                    assertSameCoordinates(
                        state, padding,
                        0, col,
                        -offset, col);
                    assertSameCoordinates(
                        state, padding,
                        state.getHeight() - 1, col,
                        state.getHeight() - 1 + offset, col);
                }
            }
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDonutPadding() {
        var state = BIG_STATE;
        var maxOffset = 5;

        try {
            var padding = initializeClass(DONUT_PADDING, Padding.class);

            for (var row = 0; row < state.getHeight(); row++) {
                for (var col = 0; col < state.getWidth(); col++) {
                    assertSameCoordinates(
                        state, padding,
                        row, col,
                        row, col);
                }
            }

            for (var offset = 1; offset <= maxOffset; offset++) {
                for (var row = 0; row < state.getHeight(); row++) {
                    assertSameCoordinates(
                        state, padding,
                        row, state.getWidth() - offset,
                        row, -offset);
                    assertSameCoordinates(
                        state, padding,
                        row, offset - 1,
                        row, state.getWidth() - 1 + offset);
                }

                for (var col = 0; col < state.getWidth(); col++) {
                    assertSameCoordinates(
                        state, padding,
                        state.getHeight() - offset, col,
                        -offset, col);
                    assertSameCoordinates(
                        state, padding,
                        offset-1, col,
                        state.getHeight() - 1 + offset, col);
                }
            }
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }

    private void assertSameCoordinates(
        AutomatonState state, Padding padding,
        int expectedRow, int expectedCol,
        int actualRow, int actualCol) {
        assertEquals(
            state.isAlive(expectedRow, expectedCol),
            padding.isAlive(state, actualRow, actualCol),
            String.format(
                ERROR_FORMAT,
                state.getHeight(), state.getWidth(),
                actualRow, actualCol,
                expectedRow, expectedCol));
    }
}
