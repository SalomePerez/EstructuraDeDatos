package edu.co.uniquindio.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class ControladorDashboard {

    @FXML
    private JFXButton btn_admin_actividad;

    @FXML
    private JFXButton btn_admin_procesos;

    @FXML
    private JFXButton btn_admin_tarea;

    @FXML
    private JFXButton btn_crear_proceso;

    @FXML
    private JFXButton btn_dashboard;

    @FXML
    private Pane inner_panel;

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblUserRole;

    @FXML
    private Pane most_inner_pane;

    @FXML
    private Pane panel_actividades;

    @FXML
    private Pane panel_contenedor_contenido;

    @FXML
    private Pane panel_contenido;

    @FXML
    private Pane panel_principal;

    @FXML
    private Pane panel_procesos;

    @FXML
    private Pane panel_tareas;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private Label txt_actividades;

    @FXML
    private Label txt_procesos;

    @FXML
    private Label txt_tareas;


    @FXML
    public void menu(ActionEvent event) {
        try {
            JFXButton clickedButton = (JFXButton) event.getSource();
            String fxmlFile;

            // Determinar qué archivo FXML cargar
            switch (clickedButton.getId()) {
                case "btn_dashboard":
                    fxmlFile = "Dashboard.fxml";
                    break;
                case "btn_crear_proceso":
                    fxmlFile = "CrearProceso.fxml";
                    break;
                case "btn_admin_procesos":
                    fxmlFile = "AdminProceso.fxml";
                    break;
                case "btn_admin_actividad":
                    fxmlFile = "AdminActividad.fxml";
                    break;
                case "btn_admin_tarea":
                    fxmlFile = "AdminTarea.fxml";
                    break;
                default:
                    System.err.println("Botón no reconocido: " + clickedButton.getId());
                    return;
            }

            // Obtener la URL del recurso de manera segura
            URL fxmlUrl = getClass().getResource("/edu/co/uniquindio/Application/vista/" + fxmlFile);
            if (fxmlUrl == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: " + fxmlFile);
            }

            // Cargar el nuevo contenido
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Pane newLoadedPane = loader.load();

            // Ajustar el tamaño
            newLoadedPane.prefWidthProperty().bind(panel_principal.widthProperty());
            newLoadedPane.prefHeightProperty().bind(panel_principal.heightProperty());

            // Limpiar y agregar el nuevo contenido
            panel_principal.getChildren().clear();
            panel_principal.getChildren().add(newLoadedPane);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la vista: " + e.getMessage());
        }
    }

}
