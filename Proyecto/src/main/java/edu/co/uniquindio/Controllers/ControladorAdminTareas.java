package edu.co.uniquindio.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.co.uniquindio.Model.Auxiliares.Persistencia;
import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Tarea;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.EstructuraDeDatos.Nodo;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ControladorAdminTareas {

    @FXML private JFXComboBox<String> comboBusqueda;
    @FXML private JFXTextField txtBusquedaTarea;
    @FXML private JFXTextField txtBusquedaActividad;
    @FXML private JFXButton btnBuscar;
    @FXML private HBox busquedaActividadBox;
    @FXML private TableView<TareaResultado> tablaTareas;
    @FXML private TableColumn<TareaResultado, String> colProceso;
    @FXML private TableColumn<TareaResultado, String> colActividad;
    @FXML private TableColumn<TareaResultado, String> colNombreTarea;
    @FXML private TableColumn<TareaResultado, String> colDescripcion;
    @FXML private TableColumn<TareaResultado, Integer> colDuracion;
    @FXML private TableColumn<TareaResultado, Boolean> colObligatoria;

    @FXML private VBox detalleTarea;
    @FXML private Label lblNombreTarea;
    @FXML private Label lblDescripcionTarea;
    @FXML private Label lblActividad;
    @FXML private Label lblDuracion;
    @FXML private Label lblEstadoTarea;

    public void initialize() {
        // Configurar comportamiento del ComboBox de búsqueda
        configurarComboBusqueda();

        // Configurar columnas de la tabla
        configurarColumnas();
    }

    private void configurarComboBusqueda() {
        comboBusqueda.setOnAction(event -> {
            String seleccion = comboBusqueda.getValue();
            busquedaActividadBox.setVisible(seleccion != null && seleccion.equals("Desde actividad específica"));
        });
    }

    private void configurarColumnas() {
        colProceso.setCellValueFactory(new PropertyValueFactory<>("proceso"));
        colActividad.setCellValueFactory(new PropertyValueFactory<>("actividad"));
        colNombreTarea.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcionTarea"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionTarea"));
        colObligatoria.setCellValueFactory(new PropertyValueFactory<>("esObligatoria"));
    }

    @FXML
    private void buscarTarea() {
        String tipoBusqueda = comboBusqueda.getValue();
        String nombreTarea = txtBusquedaTarea.getText();

        ObservableList<TareaResultado> resultados = FXCollections.observableArrayList();

        switch (tipoBusqueda) {
            case "Desde inicio del proceso":
                resultados = buscarTareaEnTodosProcesos(nombreTarea);
                break;
            case "Desde actividad actual":
                // En este punto, necesitarías lógica para determinar la actividad actual
                // Por ahora, busca en todos los procesos
                resultados = buscarTareaEnTodosProcesos(nombreTarea);
                break;
            case "Desde actividad específica":
                String nombreActividad = txtBusquedaActividad.getText();
                resultados = buscarTareaEnActividad(nombreActividad, nombreTarea);
                break;
        }

        tablaTareas.setItems(resultados);

        // Si hay resultados, configura el listener para mostrar detalles
        if (!resultados.isEmpty()) {
            configurarSeleccionTarea();
        }
    }

    private ObservableList<TareaResultado> buscarTareaEnTodosProcesos(String nombreTarea) {
        ObservableList<TareaResultado> resultados = FXCollections.observableArrayList();
        ListaEnlazada<Proceso> procesos = Persistencia.cargarProcesos();

        Nodo<Proceso> nodoProceso = procesos.getCabeza();
        while (nodoProceso != null) {
            Proceso proceso = nodoProceso.getDato();
            ListaEnlazada<Actividad> actividades = proceso.obtenerlistaDeActividades();

            Nodo<Actividad> nodoActividad = actividades.getCabeza();
            while (nodoActividad != null) {
                Actividad actividad = nodoActividad.getDato();
                Cola<Tarea> tareas = actividad.obtenerTareas();

                Nodo<Tarea> nodoTarea = tareas.obtenerPrimero();
                while (nodoTarea != null) {
                    Tarea tarea = nodoTarea.getDato();
                    if (tarea.obtenerNombre().toLowerCase().contains(nombreTarea.toLowerCase())) {
                        resultados.add(new TareaResultado(
                                proceso.obtenerTitulo(),
                                actividad.obtenerNombre(),
                                tarea.obtenerNombre(),
                                tarea.obtenerDescripcion(),
                                tarea.obtenerDuracion(),
                                tarea.esObligatoria()
                        ));
                    }
                    nodoTarea = nodoTarea.getSiguiente();
                }
                nodoActividad = nodoActividad.getSiguiente();
            }
            nodoProceso = nodoProceso.getSiguiente();
        }

        return resultados;
    }

    private ObservableList<TareaResultado> buscarTareaEnActividad(String nombreActividad, String nombreTarea) {
        ObservableList<TareaResultado> resultados = FXCollections.observableArrayList();
        ListaEnlazada<Proceso> procesos = Persistencia.cargarProcesos();

        Nodo<Proceso> nodoProceso = procesos.getCabeza();
        while (nodoProceso != null) {
            Proceso proceso = nodoProceso.getDato();
            ListaEnlazada<Actividad> actividades = proceso.obtenerlistaDeActividades();

            Nodo<Actividad> nodoActividad = actividades.getCabeza();
            while (nodoActividad != null) {
                Actividad actividad = nodoActividad.getDato();
                if (actividad.obtenerNombre().toLowerCase().contains(nombreActividad.toLowerCase())) {
                    Cola<Tarea> tareas = actividad.obtenerTareas();

                    Nodo<Tarea> nodoTarea = tareas.obtenerPrimero();
                    while (nodoTarea != null) {
                        Tarea tarea = nodoTarea.getDato();
                        if (tarea.obtenerNombre().toLowerCase().contains(nombreTarea.toLowerCase())) {
                            resultados.add(new TareaResultado(
                                    proceso.obtenerTitulo(),
                                    actividad.obtenerNombre(),
                                    tarea.obtenerNombre(),
                                    tarea.obtenerDescripcion(),
                                    tarea.obtenerDuracion(),
                                    tarea.esObligatoria()
                            ));
                        }
                        nodoTarea = nodoTarea.getSiguiente();
                    }
                }
                nodoActividad = nodoActividad.getSiguiente();
            }
            nodoProceso = nodoProceso.getSiguiente();
        }

        return resultados;
    }

    private void configurarSeleccionTarea() {
        tablaTareas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleTarea(newSelection);
            }
        });
    }

    private void mostrarDetalleTarea(TareaResultado tareaSeleccionada) {
        detalleTarea.setVisible(true);
        lblNombreTarea.setText(tareaSeleccionada.getNombreTarea());
        lblDescripcionTarea.setText(tareaSeleccionada.getDescripcionTarea());
        lblActividad.setText(tareaSeleccionada.getActividad());
        lblDuracion.setText(tareaSeleccionada.getDuracionTarea() + " minutos");
        lblEstadoTarea.setText(tareaSeleccionada.isEsObligatoria() ? "Obligatoria" : "Opcional");
    }

    // Clase interna para representar resultados de búsqueda
    public static class TareaResultado {
        private String proceso;
        private String actividad;
        private String nombreTarea;
        private String descripcionTarea;
        private int duracionTarea;
        private boolean esObligatoria;

        public TareaResultado(String proceso, String actividad, String nombreTarea,
                              String descripcionTarea, int duracionTarea, boolean esObligatoria) {
            this.proceso = proceso;
            this.actividad = actividad;
            this.nombreTarea = nombreTarea;
            this.descripcionTarea = descripcionTarea;
            this.duracionTarea = duracionTarea;
            this.esObligatoria = esObligatoria;
        }

        // Getters para permitir el acceso a los campos en la tabla
        public String getProceso() { return proceso; }
        public String getActividad() { return actividad; }
        public String getNombreTarea() { return nombreTarea; }
        public String getDescripcionTarea() { return descripcionTarea; }
        public int getDuracionTarea() { return duracionTarea; }
        public boolean isEsObligatoria() { return esObligatoria; }
    }
}