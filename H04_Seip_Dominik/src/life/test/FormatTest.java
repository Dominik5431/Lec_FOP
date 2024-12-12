package life.test;

import org.junit.jupiter.api.Test;

import life.reader.PlainTextFormat;
import life.reader.RLEFormat;
import life.reader.CellsFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static life.test.TestUtils.SMALL_STATE_ARRAY;
import static life.test.TestUtils.BIG_STATE_ARRAY;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
class FormatTest {
    @Test
    void testPlainTextEncodeSmallState() {
        var format = new PlainTextFormat();
        assertEquals(
            "..\n..\n.o\n..\n",
            format.encode(SMALL_STATE_ARRAY));
    }

    @Test
    void testPlainTextEncodeBigState() {
        var format = new PlainTextFormat();
        assertEquals(
            "...............\n" +
            ".o...oo........\n" +
            ".....oo..ooo...\n" +
            ".........oo....\n" +
            "..oo......oo...\n" +
            ".o.o....o.o....\n" +
            "...o..o......oo\n" +
            "......o...oo.o.\n" +
            "......o....o...\n" +
            "oo.........o...\n",
            format.encode(BIG_STATE_ARRAY));
    }
    @Test
    void testPlainTextDecodeSmallState() {
        var format = new PlainTextFormat();
        assertArrayEquals(
            SMALL_STATE_ARRAY,
            format.decode("..\n..\n.o\n.."));
    }

    @Test
    void testPlainTextDecodeBigState() {
        var format = new PlainTextFormat();
        assertArrayEquals(
            BIG_STATE_ARRAY,
            format.decode(
                "...............\n" +
                ".o...oo........\n" +
                ".....oo..ooo...\n" +
                ".........oo....\n" +
                "..oo......oo...\n" +
                ".o.o....o.o....\n" +
                "...o..o......oo\n" +
                "......o...oo.o.\n" +
                "......o....o...\n" +
                "oo.........o...\n"));
    }

    @Test
    void testPlainTextDecodeEmpty() {
        var format = new PlainTextFormat();
        assertThrows(
            IllegalArgumentException.class,
            () -> format.decode(""));
    }

    @Test
    void testPlainTextDecodeIllegalSymbol() {
        var format = new PlainTextFormat();
        assertThrows(
            IllegalArgumentException.class,
            () -> format.decode("..x.\n.o.."));
    }

    @Test
    void testPlainTextDecodeAlternativeEncoding() {
        var format = new PlainTextFormat('a', 'd');
        assertArrayEquals(
            SMALL_STATE_ARRAY,
            format.decode("d d\n\nd d \nd a \n\nd  d\n"));
    }

    @Test
    void testRLEEncodeSmallState() {
        var format = new RLEFormat();
        assertEquals(
            "x = 2, y = 4\n2$bo$!\n",
            format.encode(SMALL_STATE_ARRAY));
    }

    @Test
    void testRLEEncodeBigState() {
        var format = new RLEFormat();
        assertEquals(
            "x = 15, y = 10\n" +
            "$bo3b2o$5b2o2b3o$9b2o$2b2o6b2o$bobo4bobo$3bo2bo6b2o$6bo3b2ob" +
            "o$6bo4bo$2o9bo!\n",
            format.encode(BIG_STATE_ARRAY));
    }

    @Test
    void testRLEDecodeSmallState() {
        var format = new RLEFormat();
        assertArrayEquals(
            SMALL_STATE_ARRAY,
            format.decode(
                "#N Big State\n#O Kim Berninger\n" +
                "#C A simple state of smaller size used for unit testing\n" +
                "x = 2, y = 4, rule = B3/S23\n\n2$bo$!"));
    }

    @Test
    void testRLEDecodeBigState() {
        var format = new RLEFormat();
        assertArrayEquals(
            BIG_STATE_ARRAY,
            format.decode(
                "#N Big State\n#O Kim Berninger\n" +
                "#C A simple state of bigger size used for unit testing\n" +
                "x = 15, y = 10, rule = B3/S23\n" +
                "$bo3b2o$5b2o2b3o$9b2o$2b2o6b2o$bobo4bobo$3bo2bo6b2o$6bo3b2ob" +
                "o$6bo4bo$2o9bo!\n"));
    }

    @Test
    void testCellsEncodeSmallState() {
        var format = new CellsFormat();
        assertEquals(
            "\n\n.O\n\n",
            format.encode(SMALL_STATE_ARRAY));
    }

    @Test
    void testCellsEncodeBigState() {
        var format = new CellsFormat();
        assertEquals(
            "\n" +
            ".O...OO\n" +
            ".....OO..OOO\n" +
            ".........OO\n" +
            "..OO......OO\n" +
            ".O.O....O.O\n" +
            "...O..O......OO\n" +
            "......O...OO.O\n" +
            "......O....O\n" +
            "OO.........O\n",
            format.encode(BIG_STATE_ARRAY));
    }

    @Test
    void testCellsDecodeSmallState() {
        var format = new CellsFormat();
        assertArrayEquals(
            SMALL_STATE_ARRAY,
            format.decode("\n\n.O\n\n"));
    }

    @Test
    void testCellsDecodeBigState() {
        var format = new CellsFormat();
        assertArrayEquals(
            BIG_STATE_ARRAY,
            format.decode(
                "\n" +
                ".O...OO\n" +
                ".....OO..OOO\n" +
                ".........OO\n" +
                "..OO......OO\n" +
                ".O.O....O.O\n" +
                "...O..O......OO\n" +
                "......O...OO.O\n" +
                "......O....O\n" +
                "OO.........O\n"));
    }
}
