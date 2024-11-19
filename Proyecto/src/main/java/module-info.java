module edu.co.uniquindio.proyecto {
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires javafx.swing;

    // Exportar el paquete de controladores para que FXML pueda acceder a él
    exports edu.co.uniquindio.Controllers;
    // Abrir el paquete para reflexión
    opens edu.co.uniquindio.Controllers to javafx.fxml;

    // Exportar el paquete de la aplicación principal
    exports edu.co.uniquindio.Application;
    opens edu.co.uniquindio.Application to javafx.fxml;

    // Si tienes más paquetes que necesitan ser accedidos por FXML, agrégalos aquí
    exports edu.co.uniquindio.Model.Principales;
    opens edu.co.uniquindio.Model.Principales to javafx.fxml;

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
    requires com.jfoenix;


}