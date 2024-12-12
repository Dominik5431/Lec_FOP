package life.test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import life.automaton.initializer.EmptyInitializer;
import life.automaton.initializer.FileInitializer;

import life.reader.AutomatonFileReader;
import life.reader.PlainTextFormat;
import life.reader.RLEFormat;
import life.reader.CellsFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static life.test.TestUtils.SMALL_STATE_ARRAY;
import static life.test.TestUtils.BIG_STATE_ARRAY;

import static life.test.TestUtils.addToHistogram;
import static life.test.TestUtils.stateToArray;
import static life.test.TestUtils.initializeRandomizer;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class InitializerTest {
    final static int RANDOM_TRIALS = 100000;

    final AutomatonFileReader reader = new AutomatonFileReader()
        .registerFormat("txt", new PlainTextFormat())
        .registerFormat("rle", new RLEFormat())
        .registerFormat("cells", new CellsFormat());

    File getResource(String filename) {
        try {
            return new File(getClass().getResource(filename).toURI());
        } catch (URISyntaxException e) {
            fail();
        }
        return null;
    }

    @Test
    void testReadingSmallPlainTextState() {
        var stateFile = getResource("small_state.txt");
        var initializer = new FileInitializer(reader, stateFile);

        var state = initializer.createState();

        assertArrayEquals(SMALL_STATE_ARRAY, stateToArray(state));
    }

    @Test
    void testReadingBigPlainTextState() {
        var stateFile = getResource("big_state.txt");
        var initializer = new FileInitializer(reader, stateFile);

        var state = initializer.createState();

        assertArrayEquals(BIG_STATE_ARRAY, stateToArray(state));
    }

    @Test
    void testReadingSmallRLEState() {
        var stateFile = getResource("small_state.rle");
        var initializer = new FileInitializer(reader, stateFile);

        var state = initializer.createState();

        assertArrayEquals(SMALL_STATE_ARRAY, stateToArray(state));
    }

    @Test
    void testReadingBigRLEState() {
        var stateFile = getResource("big_state.rle");
        var initializer = new FileInitializer(reader, stateFile);

        var state = initializer.createState();

        assertArrayEquals(BIG_STATE_ARRAY, stateToArray(state));
    }

    @Test
    void testReadingSmallCellsState() {
        var stateFile = getResource("small_state.cells");
        var initializer = new FileInitializer(reader, stateFile);

        var state = initializer.createState();

        assertArrayEquals(SMALL_STATE_ARRAY, stateToArray(state));
    }

    @Test
    void testReadingBigCellsState() {
        var stateFile = getResource("big_state.cells");
        var initializer = new FileInitializer(reader, stateFile);

        var state = initializer.createState();

        assertArrayEquals(BIG_STATE_ARRAY, stateToArray(state));
    }

    @Test
    void testEmptyState() {
        var height = 200;
        var width = 1;

        var initializer = new EmptyInitializer(height, width);

        var state = initializer.createState();

        assertArrayEquals(new boolean[height][width], stateToArray(state));
    }

    @Test
    void testEmptyRandomState() {
        var height = 480;
        var width = 640;

        try {
            var initializer = initializeRandomizer(height, width, 0.0);

            var state = initializer.createState();

            assertEquals(height, state.getHeight());
            assertEquals(width, state.getWidth());

            assertArrayEquals(new boolean[height][width], stateToArray(state));
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testFullRandomState() {
        var height = 320;
        var width = 480;

        try {
            var initializer = initializeRandomizer(height, width, 1.0);

            var state = initializer.createState();

            assertEquals(height, state.getHeight());
            assertEquals(width, state.getWidth());

            var expected = new boolean[height][width];
            for (var row : expected) {
                Arrays.fill(row, true);
            }

            assertArrayEquals(expected, stateToArray(state));
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testRandomState() {
        var height = 20;
        var width = 30;
        var rate = 0.2;

        try {
            var initializer = initializeRandomizer(height, width, rate);

            var meanRatio = 0.0;
            var cellHistogram = new int[height][width];
            for (var i = 0; i < RANDOM_TRIALS; i++) {
                var state = initializer.createState();

                assertEquals(height, state.getHeight());
                assertEquals(width, state.getWidth());

                meanRatio += TestUtils.aliveRatio(state) / RANDOM_TRIALS;
                addToHistogram(cellHistogram, state);
            }

            assertEquals(rate, meanRatio, 1e-2);

            for (var row = 0; row < height; row++) {
                for (var col = 0; col < width; col++) {
                    assertEquals(
                        rate,
                        (double) cellHistogram[row][col] / RANDOM_TRIALS,
                        1e-2);
                }
            }
        } catch (InitializationException e) {
            fail(e.getMessage());
        }
    }
}
