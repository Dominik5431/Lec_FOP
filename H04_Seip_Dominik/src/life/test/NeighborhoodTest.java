package life.test;

import org.junit.jupiter.api.Test;

import life.automaton.neighborhood.Neighborhood;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static life.test.TestUtils.SMALL_STATE;
import static life.test.TestUtils.BIG_STATE;

import static life.test.TestUtils.MOCK_PADDING;

import static life.test.TestUtils.countAliveNeighborsForEachCell;
import static life.test.TestUtils.initializeClass;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class NeighborhoodTest {
    static final String VON_NEUMANN_NEIGHBORHOOD =
        "life.automaton.neighborhood.VonNeumannNeighborhood";

    static final String MOORE_NEIGHBORHOOD =
        "life.automaton.neighborhood.MooreNeighborhood";

    @Test
    void testVonNeumannOnSmallState() {
        try {
            var neighborhood = initializeClass(
                VON_NEUMANN_NEIGHBORHOOD, Neighborhood.class);

            assertArrayEquals(new int[][] {
                {2, 2},
                {1, 2},
                {2, 1},
                {2, 3}
            }, countAliveNeighborsForEachCell(
                SMALL_STATE, neighborhood, MOCK_PADDING));
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testVonNeumannOnBigState() {
        try {
            var neighborhood = initializeClass(
                VON_NEUMANN_NEIGHBORHOOD, Neighborhood.class);

            assertArrayEquals(new int[][] {
                {2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2},
                {2, 0, 1, 0, 1, 2, 2, 1, 0, 1, 1, 1, 0, 0, 1},
                {1, 1, 0, 0, 1, 2, 2, 1, 1, 2, 3, 1, 1, 0, 1},
                {1, 0, 1, 1, 0, 1, 1, 0, 1, 2, 3, 3, 0, 0, 1},
                {1, 2, 1, 2, 1, 0, 0, 0, 1, 2, 3, 1, 1, 0, 1},
                {2, 0, 3, 2, 1, 0, 1, 1, 0, 2, 1, 2, 0, 1, 2},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 2, 1, 1, 2, 2},
                {1, 0, 0, 1, 0, 1, 2, 1, 0, 1, 1, 2, 2, 1, 3},
                {2, 1, 0, 0, 0, 1, 1, 1, 0, 0, 2, 2, 1, 1, 1},
                {3, 2, 2, 1, 1, 1, 2, 1, 1, 1, 2, 2, 2, 1, 2}
            }, countAliveNeighborsForEachCell(
                BIG_STATE, neighborhood, MOCK_PADDING));
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testMooreOnSmallState() {
        try {
            var neighborhood = initializeClass(
                MOORE_NEIGHBORHOOD, Neighborhood.class);

            assertArrayEquals(new int[][] {
                {5, 5},
                {4, 4},
                {4, 3},
                {6, 6}
            }, countAliveNeighborsForEachCell(
                SMALL_STATE, neighborhood, MOCK_PADDING));
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testMooreOnBigState() {
        try {
            var neighborhood = initializeClass(
                MOORE_NEIGHBORHOOD, Neighborhood.class);

            assertArrayEquals(new int[][] {
                {6, 4, 4, 3, 4, 5, 5, 4, 3, 3, 3, 3, 3, 3, 5},
                {4, 0, 1, 0, 2, 3, 3, 2, 1, 2, 3, 2, 1, 0, 3},
                {4, 1, 1, 0, 2, 3, 3, 2, 2, 3, 4, 2, 1, 0, 3},
                {3, 1, 2, 2, 2, 2, 2, 1, 2, 4, 6, 5, 2, 0, 3},
                {4, 2, 3, 2, 2, 0, 0, 1, 2, 5, 4, 3, 1, 0, 3},
                {4, 1, 5, 3, 3, 1, 1, 2, 0, 3, 2, 3, 2, 2, 5},
                {4, 1, 3, 1, 2, 2, 1, 3, 1, 3, 3, 3, 3, 2, 5},
                {3, 0, 1, 1, 1, 3, 2, 3, 0, 1, 2, 2, 4, 2, 6},
                {5, 2, 1, 0, 0, 2, 1, 2, 0, 1, 4, 3, 4, 1, 4},
                {6, 4, 4, 3, 3, 4, 4, 4, 3, 3, 5, 4, 5, 3, 5}
            }, countAliveNeighborsForEachCell(
                BIG_STATE, neighborhood, MOCK_PADDING));
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }
}
