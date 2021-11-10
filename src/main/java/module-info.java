module com.ltwsinvoicesystem.ltwsinvoicesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.ltwsinvoicesystem.ltwsinvoicesystem to javafx.fxml;
    exports com.ltwsinvoicesystem.ltwsinvoicesystem;
}