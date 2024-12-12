/**
 * Enthält den Code zur Hausaufgabe des achten Übungsblattes.
 *
 * @author Kim Berninger
 * @version 1.1.0
 */
module h08 {
    requires transitive java.desktop;
    
    exports classification;

    exports classification.data;
    exports classification.models;

    exports classification.linalg;
    exports classification.io;
    exports classification.ui;

    exports classification.examples.iris;
    exports classification.examples.sms;
    exports classification.examples.twodimensional;
}
