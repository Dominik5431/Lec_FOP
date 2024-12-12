package life.reader;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Diese Klasse ermöglichst das Lesen und Schreiben von Zuständen im
 * <a href="https://www.conwaylife.com/wiki/Plaintext">
 * {@code .cells}-Format</a>.
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
public class CellsFormat implements AutomatonFileFormat {
    @Override
    public String encode(boolean[][] state) {
        var lines = Arrays.stream(state).map(row -> {
            var builder = new StringBuilder();
            for (var cell : row) {
                builder.append(cell ? 'O' : '.');
            }
            var s = builder.toString();
            return s.substring(0, Math.max(0, s.lastIndexOf('O')+1));
        }).collect(Collectors.toList());

        return String.join("\n", lines) + "\n";
    }

    @Override
    public boolean[][] decode(String content) {
        var lines = content.lines()
            .map(line -> line.replaceAll("\\s+", ""))
            .filter(line -> !line.startsWith("!"))
            .collect(Collectors.toList());

        var height = lines.size();

        var width = lines.stream()
            .mapToInt(String::length)
            .max()
            .orElseThrow(IllegalArgumentException::new);

        var state = new boolean[height][width];

        for (var row = 0; row < height; row++) {
            for (var col = 0; col < lines.get(row).length(); col++) {
                state[row][col] = lines.get(row).charAt(col) == 'O';
            }
        }
        return state;
    }
}
