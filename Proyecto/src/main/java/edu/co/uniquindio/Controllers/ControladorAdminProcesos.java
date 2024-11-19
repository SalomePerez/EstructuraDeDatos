package edu.co.uniquindio.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.co.uniquindio.Model.Administradores.AdministradorProcesos;
import edu.co.uniquindio.Model.Auxiliares.ActividadResultado;
import edu.co.uniquindio.Model.Auxiliares.GestorDatosExcel;
import edu.co.uniquindio.Model.Auxiliares.Persistencia;
import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.EstructuraDeDatos.Nodo;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Tarea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ControladorAdminProcesos {
    public JFXButton btnVisualizarArbol;
    public VBox arbolContainer;
    @FXML private Label lblNombreProceso;
    @FXML private Label lblCantidadActividades;
    @FXML private Label lblCantidadTareas;
    @FXML private JFXTextField txtBusquedaProceso;
    @FXML private TreeView<String> treeArbolGenerado;

    private ListaEnlazada<Proceso> procesos;
    private Proceso procesoSeleccionado;

    @FXML
    public void initialize() {
        cargarProcesosDesdeXML();
    }

    private void cargarProcesosDesdeXML() {
        try {
            procesos = Persistencia.cargarProcesos();
            if (procesos.estaVacia()) {
                mostrarAlerta("Aviso", "No se encontraron procesos en el archivo XML");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar procesos: " + e.getMessage());
        }
    }

    @FXML
    private void buscarProceso() {
        String nombreBuscado = txtBusquedaProceso.getText().trim();
        if (nombreBuscado.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un nombre de proceso");
            return;
        }

        procesoSeleccionado = buscarProcesoPorNombre(nombreBuscado);
        if (procesoSeleccionado != null) {
            actualizarDetallesProceso();
            generarArbolProceso();
        } else {
            mostrarAlerta("No encontrado", "No se encontró proceso con ese nombre");
        }
    }

    private Proceso buscarProcesoPorNombre(String nombre) {
        for (int i = 0; i < procesos.getTamanio(); i++) {
            Proceso proceso = procesos.getElementoEnPosicion(i);
            if (proceso.obtenerTitulo().equalsIgnoreCase(nombre)) {
                return proceso;
            }
        }
        return null;
    }

    private void actualizarDetallesProceso() {
        if (procesoSeleccionado == null) return;

        lblNombreProceso.setText(procesoSeleccionado.obtenerTitulo());

        ListaEnlazada<Actividad> actividades = procesoSeleccionado.obtenerlistaDeActividades();
        int totalActividades = actividades.getTamanio();
        int totalTareas = contarTareasTotales(procesoSeleccionado);

        lblCantidadActividades.setText(String.valueOf(totalActividades));
        lblCantidadTareas.setText(String.valueOf(totalTareas));
    }

    private int contarTareasTotales(Proceso proceso) {
        int totalTareas = 0;
        ListaEnlazada<Actividad> actividades = proceso.obtenerlistaDeActividades();

        for (int i = 0; i < actividades.getTamanio(); i++) {
            Actividad actividad = actividades.getElementoEnPosicion(i);
            totalTareas += actividad.obtenerTareas().obtenerTamano();
        }

        return totalTareas;
    }

    private void generarArbolProceso() {
        if (procesoSeleccionado == null) return;

        TreeItem<String> root = new TreeItem<>(procesoSeleccionado.obtenerTitulo());
        ListaEnlazada<Actividad> actividades = procesoSeleccionado.obtenerlistaDeActividades();

        for (int i = 0; i < actividades.getTamanio(); i++) {
            Actividad actividad = actividades.getElementoEnPosicion(i);
            TreeItem<String> nodoActividad = crearNodoActividad(actividad);
            root.getChildren().add(nodoActividad);
        }

        treeArbolGenerado.setRoot(root);
        root.setExpanded(true);
    }

    private TreeItem<String> crearNodoActividad(Actividad actividad) {
        TreeItem<String> nodoActividad = new TreeItem<>(
                String.format("Actividad: %s (%s)",
                        actividad.obtenerNombre(),
                        actividad.esObligatoria() ? "Obligatoria" : "Opcional")
        );

        // Agregar tareas
        Cola<Tarea> tareas = actividad.obtenerTareas();
        if (!tareas.estaVacia()) {
            Iterator<Tarea> iteradorTareas = tareas.iterator();
            while (iteradorTareas.hasNext()) {
                Tarea tarea = iteradorTareas.next();
                TreeItem<String> nodoTarea = new TreeItem<>(
                        String.format("Tarea: %s (%d min) (%s)",
                                tarea.obtenerNombre(),
                                tarea.obtenerDuracion(),
                                tarea.esObligatoria() ? "Obligatoria" : "Opcional")
                );
                nodoActividad.getChildren().add(nodoTarea);
            }
        }

        return nodoActividad;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void importarActividad(ActionEvent actionEvent) {
    }

    public void importarTarea(ActionEvent actionEvent) {
    }

    public void importarTodo(ActionEvent actionEvent) {
    }

    public void exportarTodo(ActionEvent actionEvent) {
    }

    public void importarProceso(ActionEvent actionEvent) {
    }

    @FXML
    public void generarArbolArchivo(ActionEvent actionEvent) {
        if (procesoSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un proceso primero");
            return;
        }

        // Crear el nodo raíz con el título del proceso seleccionado
        TreeItem<String> root = new TreeItem<>(procesoSeleccionado.obtenerTitulo());
        ListaEnlazada<Actividad> actividades = procesoSeleccionado.obtenerlistaDeActividades();

        // Recorrer actividades del proceso
        for (int i = 0; i < actividades.getTamanio(); i++) {
            Actividad actividad = actividades.getElementoEnPosicion(i);
            TreeItem<String> nodoActividad = new TreeItem<>(
                    String.format("Actividad: %s (%s)",
                            actividad.obtenerNombre(),
                            actividad.esObligatoria() ? "Obligatoria" : "Opcional")
            );

            // Recorrer tareas de la actividad
            Cola<Tarea> tareas = actividad.obtenerTareas();
            if (!tareas.estaVacia()) {
                Iterator<Tarea> iteradorTareas = tareas.iterator();
                while (iteradorTareas.hasNext()) {
                    Tarea tarea = iteradorTareas.next();
                    TreeItem<String> nodoTarea = new TreeItem<>(
                            String.format("Tarea: %s (%d min) (%s) [%s]",
                                    tarea.obtenerNombre(),
                                    tarea.obtenerDuracion(),
                                    tarea.esObligatoria() ? "Obligatoria" : "Opcional",
                                    tarea.estaCompletada() ? "Completada" : "Pendiente")
                    );
                    nodoActividad.getChildren().add(nodoTarea);
                }
            }

            root.getChildren().add(nodoActividad);
        }

        // Configurar estilo personalizado para los nodos del árbol
        treeArbolGenerado.setCellFactory(treeView -> new TreeCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Crear el nodo visual con círculo y texto
                    Circle circle = new Circle(15);
                    circle.getStyleClass().add("tree-node-circle");

                    Text text = new Text(item);
                    text.getStyleClass().add("tree-node-text");

                    StackPane nodeContent = new StackPane(circle, text);
                    nodeContent.setAlignment(Pos.CENTER);
                    StackPane.setMargin(text, new Insets(0, 5, 0, 5));

                    setGraphic(nodeContent);
                }
            }
        });

        // Configurar el árbol y mostrar el contenedor
        treeArbolGenerado.setRoot(root);
        root.setExpanded(true); // Expandir el nodo raíz

        arbolContainer.setVisible(true);
        arbolContainer.setManaged(true);
    }

}