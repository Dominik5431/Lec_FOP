package life.reader;

import life.automaton.initializer.FileInitializer;

/**
 * Dieses Interface definiert ein Textformat, in dem Zustände von zellulären
 * Automaten serialisiert und in Textdateien gespeichert werden können.
 * Es dient der Übersetzung zwischen zweidimensionalen {@code boolean}-Arrays
 * und {@code String}s, die aus dem Dateiinhalt generiert werden.
 * <br>
 * Bei den Arrays repräsentiert der erste Index eine Zeile und der zweite die
 * Zelle in der jeweiligen Spalte, wobei die Koordinaten {@code [0][0]} der
 * Zelle in der oberen Linken ecke des Spielfeldes entsprechen.
 * 
 * @author Kim Berninger
 * @version 1.0.2
 *
 * @see AutomatonFileReader
 * @see FileInitializer
 */
public interface AutomatonFileFormat {
    /**
     * Wandelt den vom übergebenen Array repräsentierten Zustand entsprechend
     * dieses Dateiformates in einen {@code String} um.
     *
     * @param state der Zustand, der als {@code String} kodiert werden soll
     *
     * @return die Repräsentation des Zustandes als {@code String}
     */
    String encode(boolean[][] state);

    /**
     * Liest den Zustand, der im übergebenen {@code String} kodiert ist diesem
     * Format entsprechend als {@code boolean}-Array ein.
     *
     * @param content der {@code String}, der den Zustand repräsentiert
     * @return der dekodierte Zustand
     *
     * @throws IllegalArgumentException falls der übergebene {@code String}
     *                                  nicht diesem Format entspricht
     */
    boolean[][] decode(String content);
}
