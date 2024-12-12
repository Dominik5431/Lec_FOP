package life.test;

import org.junit.jupiter.api.Test;

import life.automaton.rules.UpdateRules;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static life.test.TestUtils.initializeClass;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class RulesTest {
    static final String GAME_OF_LIFE_RULES =
        "life.automaton.rules.GameOfLifeRules";

    static final String HIGHLIFE_RULES =
        "life.automaton.rules.HighlifeRules";

    @Test
    void testGameOfLifeRules() {
        try {
            var rules = initializeClass(GAME_OF_LIFE_RULES, UpdateRules.class);

            assertFalse(rules.getNextCellState(false, 0));
            assertFalse(rules.getNextCellState(false, 1));
            assertFalse(rules.getNextCellState(false, 2));
            assertTrue(rules.getNextCellState(false, 3));
            assertFalse(rules.getNextCellState(false, 4));
            assertFalse(rules.getNextCellState(false, 5));
            assertFalse(rules.getNextCellState(false, 6));
            assertFalse(rules.getNextCellState(false, 7));
            assertFalse(rules.getNextCellState(false, 8));

            assertFalse(rules.getNextCellState(true, 0));
            assertFalse(rules.getNextCellState(true, 1));
            assertTrue(rules.getNextCellState(true, 2));
            assertTrue(rules.getNextCellState(true, 3));
            assertFalse(rules.getNextCellState(true, 4));
            assertFalse(rules.getNextCellState(true, 5));
            assertFalse(rules.getNextCellState(true, 6));
            assertFalse(rules.getNextCellState(true, 7));
            assertFalse(rules.getNextCellState(true, 8));
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testHighlifeRules() {
        try {
            var rules = initializeClass(HIGHLIFE_RULES, UpdateRules.class);

            assertFalse(rules.getNextCellState(false, 0));
            assertFalse(rules.getNextCellState(false, 1));
            assertFalse(rules.getNextCellState(false, 2));
            assertTrue(rules.getNextCellState(false, 3));
            assertFalse(rules.getNextCellState(false, 4));
            assertFalse(rules.getNextCellState(false, 5));
            assertTrue(rules.getNextCellState(false, 6));
            assertFalse(rules.getNextCellState(false, 7));
            assertFalse(rules.getNextCellState(false, 8));

            assertFalse(rules.getNextCellState(true, 0));
            assertFalse(rules.getNextCellState(true, 1));
            assertTrue(rules.getNextCellState(true, 2));
            assertTrue(rules.getNextCellState(true, 3));
            assertFalse(rules.getNextCellState(true, 4));
            assertFalse(rules.getNextCellState(true, 5));
            assertFalse(rules.getNextCellState(true, 6));
            assertFalse(rules.getNextCellState(true, 7));
            assertFalse(rules.getNextCellState(true, 8));
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }
}
