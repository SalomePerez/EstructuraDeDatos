module Proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    exports estructura;
    exports estructura.controller;

    opens estructura.model to javafx.base;

    opens estructura.controller to javafx.fxml;
    opens estructura to javafx.fxml;
}