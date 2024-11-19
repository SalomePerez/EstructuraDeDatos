package edu.co.uniquindio.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.co.uniquindio.Model.Administradores.AdministradorProcesos;
import edu.co.uniquindio.Model.Auxiliares.GestorDatosExcel;
import edu.co.uniquindio.Model.Auxiliares.Persistencia;
import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Tarea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ControladorAdminProcesos {

    @FXML private JFXTextField txtBusquedaProceso;
    @FXML private JFXButton btnBuscar;
    @FXML private JFXButton btnVisualizarArbol;
    @FXML private MenuItem menuImportarProceso;
    @FXML private MenuItem menuImportarActividad;
    @FXML private MenuItem menuImportarTarea;
    @FXML private MenuItem menuImportarTodo;
    @FXML private MenuItem menuExportar;
    @FXML private VBox detalleProceso;
    @FXML private VBox arbolContainer;
    @FXML private TreeView<String> treeArbolGenerado;
    @FXML private Label lblNombreProceso;
    @FXML private Label lblDescripcionProceso;
    @FXML private Label lblFechaInicio;
    @FXML private Label lblCantidadActividades;
    @FXML private Label lblCantidadTareas;

    private AdministradorProcesos administradorProcesos;
    private GestorDatosExcel gestorExcel;
    private Proceso procesoSeleccionado;

    @FXML
    public void initialize() {
        administradorProcesos = new AdministradorProcesos();
        gestorExcel = new GestorDatosExcel(administradorProcesos);

        // Inicializar el TreeView
        treeArbolGenerado = new TreeView<>();
        treeArbolGenerado.setShowRoot(true);

        // Ocultar inicialmente el contenedor del árbol
        arbolContainer.setVisible(false);
        arbolContainer.setManaged(false);

        // Cargar procesos iniciales desde XML
        cargarProcesosIniciales();
    }

    @FXML
    private void buscarProceso() {
        String nombreBuscado = txtBusquedaProceso.getText().trim();
        if (nombreBuscado.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar un nombre de proceso para buscar");
            return;
        }

        procesoSeleccionado = buscarProcesoPorNombre(nombreBuscado);
        if (procesoSeleccionado != null) {
            actualizarDetallesProceso();
        } else {
            mostrarAlerta("No encontrado", "No se encontró ningún proceso con ese nombre");
        }
    }

    @FXML
    private void generarArbolArchivo() {
        if (procesoSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un proceso primero");
            return;
        }

        // Crear el árbol usando el título del proceso como raíz
        TreeItem<String> root = new TreeItem<>(procesoSeleccionado.obtenerTitulo());
        generarArbolProceso(root, procesoSeleccionado);

        // Mostrar el árbol
        treeArbolGenerado.setRoot(root);
        root.setExpanded(true);

        // Hacer visible el contenedor del árbol
        arbolContainer.setVisible(true);
        arbolContainer.setManaged(true);
    }

    private void generarArbolProceso(TreeItem<String> nodoRaiz, Proceso proceso) {
        ListaEnlazada<Actividad> actividades = proceso.obtenerlistaDeActividades();

        for (int i = 0; i < actividades.getTamanio(); i++) {
            Actividad actividad = actividades.getElementoEnPosicion(i);
            TreeItem<String> nodoActividad = new TreeItem<>(
                    String.format("Actividad: %s (%s)",
                            actividad.obtenerNombre(),
                            actividad.esObligatoria() ? "Obligatoria" : "Opcional")
            );

            // Agregar información de conexiones de actividades
            if (actividad.obtenerActividadAnterior() != null) {
                nodoActividad.getChildren().add(new TreeItem<>(
                        "Anterior: " + actividad.obtenerActividadAnterior().obtenerNombre()));
            }
            if (actividad.obtenerActividadSiguiente() != null) {
                nodoActividad.getChildren().add(new TreeItem<>(
                        "Siguiente: " + actividad.obtenerActividadSiguiente().obtenerNombre()));
            }

            // Agregar tareas de la actividad
            Cola<Tarea> tareas = actividad.obtenerTareas();
            if (!tareas.estaVacia()) {
                int tareasRestantes = tareas.obtenerTamano();
                while (tareasRestantes > 0) {
                    Tarea tarea = tareas.desencolar();
                    TreeItem<String> nodoTarea = new TreeItem<>(
                            String.format("Tarea: %s (%d min) (%s) [%s]",
                                    tarea.obtenerNombre(),
                                    tarea.obtenerDuracion(),
                                    tarea.esObligatoria() ? "Obligatoria" : "Opcional",
                                    tarea.estaCompletada() ? "Completada" : "Pendiente")
                    );
                    nodoActividad.getChildren().add(nodoTarea);
                    tareas.encolar(tarea);
                    tareasRestantes--;
                }
            } else {
                nodoActividad.getChildren().add(new TreeItem<>("Sin tareas asignadas"));
            }

            nodoRaiz.getChildren().add(nodoActividad);
        }
    }

    private void actualizarDetallesProceso() {
        if (procesoSeleccionado != null) {
            lblNombreProceso.setText(procesoSeleccionado.obtenerTitulo());
            // Usando el método toString() de Proceso para mostrar información detallada
            lblDescripcionProceso.setText(procesoSeleccionado.toString());
            lblFechaInicio.setText(procesoSeleccionado.obtenerFechaDeInicio().toString());
            lblCantidadActividades.setText(
                    String.valueOf(procesoSeleccionado.obtenerlistaDeActividades().getTamanio())
            );
            lblCantidadTareas.setText(
                    String.valueOf(contarTareasTotales(procesoSeleccionado))
            );
        }
    }

    private Proceso buscarProcesoPorNombre(String nombre) {
        ListaEnlazada<Proceso> procesos = administradorProcesos.obtenerTodosLosProcesos();
        for (int i = 0; i < procesos.getTamanio(); i++) {
            Proceso proceso = procesos.getElementoEnPosicion(i);
            if (proceso.obtenerTitulo().equalsIgnoreCase(nombre)) {
                return proceso;
            }
        }
        return null;
    }

    @FXML
    private void importarProceso() {
        File archivo = seleccionarArchivo("Importar Proceso", "*.xlsx");
        if (archivo != null) {
            try {
                gestorExcel.importarDatos(archivo.getAbsolutePath());
                mostrarAlerta("Éxito", "Proceso importado correctamente");
                cargarProcesosIniciales(); // Recargar la lista de procesos
            } catch (IOException e) {
                mostrarAlerta("Error", "Error al importar el proceso: " + e.getMessage());
            }
        }
    }

    @FXML
    private void exportarTodo() {
        if (procesoSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un proceso primero");
            return;
        }

        File archivo = seleccionarArchivoGuardar("Exportar Proceso", "*.xlsx");
        if (archivo != null) {
            try {
                gestorExcel.exportarProceso(archivo.getAbsolutePath(),
                        procesoSeleccionado.obtenerIdentificador());
                mostrarAlerta("Éxito", "Proceso exportado correctamente");
            } catch (IOException e) {
                mostrarAlerta("Error", "Error al exportar el proceso: " + e.getMessage());
            }
        }
    }

    private void cargarProcesosIniciales() {
        try {
            ListaEnlazada<Proceso> procesos = Persistencia.cargarProcesos();
            for (int i = 0; i < procesos.getTamanio(); i++) {
                Proceso proceso = procesos.getElementoEnPosicion(i);
                administradorProcesos.registrarNuevoProceso(proceso.obtenerTitulo());
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar los procesos iniciales: " + e.getMessage());
        }
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

    private File seleccionarArchivo(String titulo, String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titulo);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", extension)
        );
        return fileChooser.showOpenDialog(new Stage());
    }

    private File seleccionarArchivoGuardar(String titulo, String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titulo);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", extension)
        );
        return fileChooser.showSaveDialog(new Stage());
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
}