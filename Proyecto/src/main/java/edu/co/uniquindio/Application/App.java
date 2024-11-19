package edu.co.uniquindio.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar el archivo FXML (login.fxml)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/co/uniquindio/Application/vista/Principal.fxml"));

        // Crear una escena con el contenido cargado desde el archivo FXML
        Scene scene = new Scene(fxmlLoader.load());

        // Establecer el título de la ventana
        stage.setTitle("Inicio de sesión");

        // Establecer la escena en el escenario principal
        stage.setScene(scene);

        // Mostrar la ventana
        stage.show();
    }
}