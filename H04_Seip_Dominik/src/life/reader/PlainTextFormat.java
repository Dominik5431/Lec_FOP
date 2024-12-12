package life.reader;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 * Diese Klasse ermöglichst das Lesen und Schreiben von Zuständen in einem
 * simplen Textformat, mit frei wählbaren Symbolen für lebendige und tote
 * Zellen. 
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
public class PlainTextFormat implements AutomatonFileFormat {
    private static final char DEFAULT_TRUE_SYMBOL = 'o';
    private static final char DEFAULT_FALSE_SYMBOL = '.';

    private char trueSymbol;
    private char falseSymbol;

    /**
     * Erzeugt ein {@code PlainTextFormat} mit {@value #DEFAULT_TRUE_SYMBOL} als
     * Symbol für lebendige und {@value #DEFAULT_FALSE_SYMBOL} als Symbol für
     * tote Zellen.
     */
    public PlainTextFormat() {
        this(DEFAULT_TRUE_SYMBOL, DEFAULT_FALSE_SYMBOL);
    }

    /**
     * Erzeugt ein {@code PlainTextFormat} mit den übergebenen Symbolen als
     * Repräsentation für lebendige bzw. tote Zellen.
     * 
     * @param trueSymbol  das Symbol für lebendige Zellen
     * @param falseSymbol das Symbol für tote Zellen
     *
     * @throws IllegalArgumentException falls es sich bei einem der beiden
     *                                  Symbole um white space handelt
     */
    public PlainTextFormat(char trueSymbol, char falseSymbol) {
        if (Character.isWhitespace(trueSymbol)
            || Character.isWhitespace(falseSymbol)) {
            throw new IllegalArgumentException(
                "cell encoding characters may not be whitespace");
        }
        this.trueSymbol = trueSymbol;
        this.falseSymbol = falseSymbol;
    }

    @Override
    public String encode(boolean[][] state) {
        var builder = new StringBuilder();
        for (var row : state) {
            for (var cell : row) {
                builder.append(cell ? trueSymbol : falseSymbol);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public boolean[][] decode(String content) {
        List<String> lines;

        lines = content.lines()
            .filter(not(String::isBlank))
            .map(line -> line.replaceAll("\\s+", ""))
            .collect(Collectors.toList());

        var height = lines.size();

        if (height == 0) {
            throw new IllegalArgumentException(
                "Die Datei darf nicht leer sein");
        }

        var width = lines.get(0).length();

        if (!lines.stream().allMatch(line -> line.length() == width)) {
            throw new IllegalArgumentException(
                "Die Zeilen müssen eine konsistente Länge haben");
        }

        var state = new boolean[height][width];

        for (var col = 0; col < height; col++) {
            for (var row = 0; row < width; row++) {
                var c = lines.get(col).charAt(row);
                if (c == trueSymbol) {
                    state[col][row] = true;
                } else if (c != falseSymbol) {
                    throw new IllegalArgumentException(
                        "Unbekanntes symbol " + c);
                }
            }
        }

        return state;
    }
}
