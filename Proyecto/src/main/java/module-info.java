module org.example.proyecto {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;

    opens org.example.proyecto to javafx.fxml;
    exports org.example.proyecto;
}