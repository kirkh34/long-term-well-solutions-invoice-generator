module com.ltwsinvoicesystem.ltwsinvoicesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires mysql.connector.java;

    opens com.ui to javafx.fxml;
    opens com.ltws to javafx.fxml;

    exports com.ui;
    exports com.ltws;
}
