package life.reader;

import java.util.Map;
import java.util.TreeMap;

import life.automaton.initializer.FileInitializer;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

/**
 * Wird verwendet um den Zustand eines zellulären Automaten aus einer Textdatei
 * zu lesen.
 *
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see AutomatonFileFormat
 * @see FileInitializer
 */
public class AutomatonFileReader {
    private Map<String, AutomatonFileFormat> formats = new TreeMap<>();

    /**
     * Fügt ein weiteres Dateiformat zu den unterstützten Formaten hinzu.
     * Ist die entsprechende Dateiendung bereits registriert, wird das
     * zugehörige {@link AutomatonFileFormat} aktualisiert.
     *
     * @param extension die Endung der der Dateien mit dem neuen Format
     * @param format    das {@code AutomatonFileFormat}, das verwendet wird um
     *                  die entsprechenden Dateien einzulesen
     *
     * @return diesen {@code AutomatonFileReader}
     */
    public AutomatonFileReader registerFormat(
        String extension, AutomatonFileFormat format
    ) {
        formats.put(extension.toLowerCase(), format);
        return this;
    }

    /**
     * Liefert ein Array mit den Dateiendungen der von diesem
     * {@code AutomatonFileReader} unterstützten Formaten.
     *
     * @return die Datei-Endungen der unterstützten Formate
     */
    public String[] getSupportedFormats() {
        return formats.keySet().toArray(String[]::new);
    }

    /**
     * Liest die übergebene Datei entsprechend ihrer Dateiendung ein.
     *
     * @param file die einzulesende Datei
     *
     * @return ein zweidimensionales {@code boolean}-Array, das den eingelesenen
     *         Zustand repräsentiert
     *
     * @throws UncheckedIOException falls beim Öffnen der Datei ein Fehler
     *                              auftritt oder die Endung der Datei diesem
     *                              {@code AutomatonFileReader} nicht bekannt
     *                              ist
     *
     * @see AutomatonFileFormat
     * @see AutomatonFileFormat#decode(String)
     */
    public boolean[][] readFile(File file) {
        if (file.isFile()) {
            var index = file.getName().lastIndexOf('.');
            if (index >= 0) {
                var extension = file.getName().substring(index+1).toLowerCase();
                if (formats.containsKey(extension)) {
                    try {
                        var content = Files.readString(file.toPath());
                        return formats.get(extension).decode(content);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
                throw new UncheckedIOException(new IOException(
                    "Unbekannte Dateiendung " + extension));
            }
            throw new UncheckedIOException(new IOException(
                "Keine Dateiendung vorhanden"));
        }
        throw new UncheckedIOException(new IOException(
            file.getName() + " ist keine gültige Datei"));
    }
}
