package life.automaton.initializer;

import java.io.File;
import java.io.UncheckedIOException;

import life.automaton.state.AutomatonState;
import life.automaton.state.ArrayAutomatonState;

import life.reader.AutomatonFileReader;

/**
 * Initialisiert den Zustand eines zellulären Automaten aus einer Text-Datei.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see AutomatonState
 * @see ArrayAutomatonState
 * @see AutomatonFileReader
 */
public class FileInitializer implements StateInitializer {
    private final File file;
    private final AutomatonFileReader reader;

    /**
     * Initialisiert eine Factory, die imstande ist, den Zustand eines
     * zellulären Automaten aus einer Text-Datei zu lesen.
     *
     * @param reader ein {@code AutomatonFileReader} mit einem passenden Format
     *               um die Datei auszulesen
     * @param file   die Datei, in der der Zustand gespeichert ist
     */
    public FileInitializer(AutomatonFileReader reader, File file) {
        this.reader = reader;
        this.file = file;
    }

    /**
     * Erzeugt den Zustand eines zellulären Automaten aus der im Konstruktor
     * festgelegten Text-Datei.
     *
     * @return den von der Factory erzeugten Zustand
     *
     * @throws UncheckedIOException falls beim Öffnen der Datei ein Fehler
     *                              auftritt
     */
    @Override
    public AutomatonState createState() {
        var matrix = reader.readFile(file);

        var state = new ArrayAutomatonState(matrix.length, matrix[0].length);
        for (var row = 0; row < state.getHeight(); row++) {
            for (var col = 0; col < state.getWidth(); col++) {
                if (matrix[row][col]) {
                    state.giveBirth(row, col);
                }
            }
        }

        return state;
    }
}
