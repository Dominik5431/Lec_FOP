package life.reader;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.util.function.Predicate.not;

import java.util.Arrays;

/**
 * Diese Klasse ermöglichst das Lesen und Schreiben von Zuständen im
 * <a href="https://www.conwaylife.com/wiki/Run_Length_Encoded">
 * {@code .rle}-Format</a>.
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
public class RLEFormat implements AutomatonFileFormat {
    @Override
    public String encode(boolean[][] state) {
        var header = String.format(
            "x = %d, y = %d\n", state[0].length, state.length);

        var lines = Arrays.stream(state).map(row -> {
            var builder = new StringBuilder();
            for (var cell : row) {
                builder.append(cell ? 'o' : 'b');
            }
            var s = builder.toString();
            return s.substring(0, Math.max(0, s.lastIndexOf('o')+1));
        }).collect(Collectors.toList());

        var sequence = String.join("$", lines);
        var compressor = new StringBuilder();

        var pattern = Pattern.compile("((.)\\2*)");
        var matcher = pattern.matcher(sequence);

        while (matcher.find()) {
            var group = matcher.group(1);
            if (group.length() > 1) {
                compressor.append(group.length());
            }
            compressor.append(group.charAt(0));
        }

        return header + compressor.toString() + "!\n";
    }

    @Override
    public boolean[][] decode(String content) {
        var lines = content.lines()
            .map(line -> line.replaceAll("\\s+", ""))
            .filter(not(String::isBlank).and(line -> !line.startsWith("#")))
            .collect(Collectors.toList());

        if (lines.size() == 0) {
            throw new IllegalArgumentException(
                "Keine gültige Sequenz gefunden");
        }

        var firstLine = lines.remove(0);
        var pattern = Pattern.compile("x=(?<width>\\d+),y=(?<height>\\d+)");
        var matcher = pattern.matcher(firstLine);

        if (matcher.find()) {
            var height = Integer.parseInt(matcher.group("height"));
            var width = Integer.parseInt(matcher.group("width"));

            var state = new boolean[height][width];

            var sequence = String.join("", lines).chars()
                .takeWhile(c -> c != '!').toArray();
            var n = 0;
            var row = 0;
            var col = 0;
            for (var c : sequence) {
                if (Character.isDigit(c)) {
                    n *= 10;
                    n += Integer.parseInt("" + ((char) c));
                } else if (c == 'o') {
                    for (var i = 0; i < Math.max(1, n); i++) {
                        state[row][col++] = true;
                    }
                    n = 0;
                } else if (c == 'b') {
                    col += Math.max(1, n);
                    n = 0;
                } else if (c == '$') {
                    row += Math.max(1, n);
                    col = 0;
                    n = 0;
                } else {
                    throw new IllegalArgumentException(
                        "Unbekanntes symbol " + c);
                }
            }
            return state;
        }
        throw new IllegalArgumentException(
            "Ungültige Dimensionsangabe: " + firstLine);
    }
}
