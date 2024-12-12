/**
 * @author Kim Berninger
 * @version 1.0.2
 */
module h10 {
    requires transitive java.desktop;
    requires org.junit.jupiter.api;
    requires org.hamcrest.core;
	requires java.naming;

    exports campus.data.domain;
    exports campus.data.query;
    exports campus.data.query.csv;
    exports campus.data.query.csv.io;
    exports campus.data.query.csv.type;
    exports campus.data.repository;

    exports campus.ui;
    exports campus.ui.editor;
    exports campus.ui.form;
    exports campus.ui.model;
    exports campus.ui.renderer;
    exports campus.ui.view;
}
