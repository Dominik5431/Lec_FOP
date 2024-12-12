/**
 * Enthält den Code zur Hausaufgabe des vierten Übungsblattes.
 *
 * @author Kim Berninger
 * @version 1.0.2
 */
module h04 {
    requires transitive java.desktop;
    requires org.junit.jupiter.api;

    exports life.automaton;
    exports life.automaton.state;
    exports life.automaton.initializer;
    exports life.automaton.neighborhood;
    exports life.automaton.rules;
    exports life.reader;

    exports life.ui;
    exports life.ui.color;
}
