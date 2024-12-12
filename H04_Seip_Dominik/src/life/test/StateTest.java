package life.test;

import org.junit.jupiter.api.Test;

import life.automaton.state.ArrayAutomatonState;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class StateTest {
    @Test
    void testEmptyState() {
        var height = 8;
        var width = 10;
        var state = new ArrayAutomatonState(height, width);

        assertEquals(height, state.getHeight());
        assertEquals(width, state.getWidth());

        for (var row = 0; row < height; row++) {
            for (var col = 0; col < width; col++) {
                assertFalse(state.isAlive(row, col));
            }
        }
    }

    @Test
    void testGivingBirth() {
        var height = 8;
        var width = 10;
        var state = new ArrayAutomatonState(height, width);

        state.giveBirth(4, 8);

        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                if (row == 4 && col == 8) {
                    assertTrue(state.isAlive(row, col));
                }
                else {
                    assertFalse(state.isAlive(row, col));
                }
            }
        }

        state.giveBirth(5, 1);

        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                if (row == 4 && col == 8 || row == 5 && col == 1) {
                    assertTrue(state.isAlive(row, col));
                }
                else {
                    assertFalse(state.isAlive(row, col));
                }
            }
        }

        state.giveBirth(4, 8);

        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                if (row == 4 && col == 8 || row == 5 && col == 1) {
                    assertTrue(state.isAlive(row, col));
                }
                else {
                    assertFalse(state.isAlive(row, col));
                }
            }
        }
    }

    @Test
    void testKilling() {
        var height = 8;
        var width = 10;
        var state = new ArrayAutomatonState(height, width);

        state.giveBirth(4, 8);
        state.giveBirth(5, 1);
        state.kill(4, 8);

        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                if (row == 5 && col == 1) {
                    assertTrue(state.isAlive(row, col));
                }
                else {
                    assertFalse(state.isAlive(row, col));
                }
            }
        }

        state.kill(4, 8);

        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                if (row == 5 && col == 1) {
                    assertTrue(state.isAlive(row, col));
                }
                else {
                    assertFalse(state.isAlive(row, col));
                }
            }
        }

        state.kill(5, 1);

        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                assertFalse(state.isAlive(row, col));
            }
        }
    }

    @Test
    void testCopy() {
        var height = 8;
        var width = 10;
        var state1 = new ArrayAutomatonState(height, width);

        state1.giveBirth(3, 7);
        state1.giveBirth(6, 2);

        assertTrue(state1.isAlive(3, 7));
        assertTrue(state1.isAlive(6, 2));

        var state2 = state1.copy();

        assertTrue(state2.isAlive(3, 7));
        assertTrue(state2.isAlive(6, 2));

        state1.giveBirth(4, 7);

        assertTrue(state1.isAlive(3, 7));
        assertTrue(state1.isAlive(6, 2));
        assertTrue(state1.isAlive(4, 7));

        assertTrue(state2.isAlive(3, 7));
        assertTrue(state2.isAlive(6, 2));
        assertFalse(state2.isAlive(4, 7));

        state2.kill(3, 7);

        assertTrue(state1.isAlive(3, 7));
        assertTrue(state1.isAlive(6, 2));
        assertTrue(state1.isAlive(4, 7));

        assertFalse(state2.isAlive(3, 7));
        assertTrue(state2.isAlive(6, 2));
        assertFalse(state2.isAlive(4, 7));
    }
}
