module edu.co.uniquindio.proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires okhttp3;
    requires jakarta.mail;
    requires org.apache.poi.ooxml;
    opens edu.co.uniquindio.Application to javafx.fxml;
    exports edu.co.uniquindio.Application;
}