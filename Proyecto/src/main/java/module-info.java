module org.example.proyecto {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.json;
    requires okhttp3;

    opens org.example.proyecto to javafx.fxml;
}