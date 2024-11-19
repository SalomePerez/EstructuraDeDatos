package edu.co.uniquindio.Controllers;

import com.jfoenix.controls.*;
import edu.co.uniquindio.Model.Administradores.AdministradorProcesos;
import edu.co.uniquindio.Model.Auxiliares.ActividadResultado;
import edu.co.uniquindio.Model.Auxiliares.Persistencia;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Tarea;
import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.EstructuraDeDatos.Nodo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorAdminActividad implements Initializable {

    @FXML private JFXTextField txtBusquedaActividad;
    @FXML private JFXButton btnBuscar;
    @FXML private TableView<ActividadResultado> tablaActividades;
    @FXML private TableColumn<ActividadResultado, String> colNombreProceso;
    @FXML private TableColumn<ActividadResultado, String> colNombreActividad;
    @FXML private TableColumn<ActividadResultado, String> colDescripcion;
    @FXML private TableColumn<ActividadResultado, String> colObligatoria;
    @FXML private TableColumn<ActividadResultado, String> colTiempoMin;
    @FXML private TableColumn<ActividadResultado, String> colTiempoMax;
    @FXML private VBox detalleActividad;
    @FXML private Label lblNombreActividad;
    @FXML private Label lblDescripcionActividad;
    @FXML private Label lblEstadoActividad;
    @FXML private TableView<Tarea> tablaTareas;
    @FXML private TableColumn<Tarea, String> colNombreTarea;
    @FXML private TableColumn<Tarea, String> colDescripcionTarea;
    @FXML private TableColumn<Tarea, String> colDuracion;
    @FXML private TableColumn<Tarea, String> colObligatoriaTarea;
    @FXML private VBox resultadosContainer;

    private AdministradorProcesos administradorProcesos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeColumns();
        setupListeners();
        detalleActividad.setVisible(false);
        configurarTablas();
    }

    private void configurarTablas() {
        tablaActividades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaTareas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNombreProceso.prefWidthProperty().bind(tablaActividades.widthProperty().multiply(0.15));
        colNombreActividad.prefWidthProperty().bind(tablaActividades.widthProperty().multiply(0.15));
        colDescripcion.prefWidthProperty().bind(tablaActividades.widthProperty().multiply(0.30));
        colObligatoria.prefWidthProperty().bind(tablaActividades.widthProperty().multiply(0.10));
        colTiempoMin.prefWidthProperty().bind(tablaActividades.widthProperty().multiply(0.15));
        colTiempoMax.prefWidthProperty().bind(tablaActividades.widthProperty().multiply(0.15));

        colNombreTarea.prefWidthProperty().bind(tablaTareas.widthProperty().multiply(0.20));
        colDescripcionTarea.prefWidthProperty().bind(tablaTareas.widthProperty().multiply(0.40));
        colDuracion.prefWidthProperty().bind(tablaTareas.widthProperty().multiply(0.20));
        colObligatoriaTarea.prefWidthProperty().bind(tablaTareas.widthProperty().multiply(0.20));
    }

    private void initializeColumns() {
        colNombreProceso.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreProceso()));
        colNombreActividad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActividad().obtenerNombre()));
        colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActividad().obtenerDescripcion()));
        colObligatoria.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getActividad().esObligatoria() ? "Sí" : "No"));
        colTiempoMin.setCellValueFactory(data -> new SimpleStringProperty(calcularTiempoMinimo(data.getValue().getActividad()) + " min"));
        colTiempoMax.setCellValueFactory(data -> new SimpleStringProperty(calcularTiempoMaximo(data.getValue().getActividad()) + " min"));

        colNombreTarea.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().obtenerNombre()));
        colDescripcionTarea.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().obtenerDescripcion()));
        colDuracion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().obtenerDuracion() + " min"));
        colObligatoriaTarea.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().esObligatoria() ? "Sí" : "No"));
    }

    private void setupListeners() {
        tablaActividades.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleActividad(newSelection.getActividad());
            }
        });

        txtBusquedaActividad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 3) {
                buscarActividad();
            }
        });
    }

    @FXML
    private void buscarActividad() {
        String nombreBuscado = txtBusquedaActividad.getText().trim();
        if (nombreBuscado.isEmpty()) {
            mostrarError("Debe ingresar un nombre de actividad para buscar");
            return;
        }

        ListaEnlazada<ActividadResultado> resultados = buscarActividadEnProcesos(nombreBuscado);
        if (resultados.estaVacia()) {
            mostrarMensaje("Búsqueda", "No se encontraron actividades con ese nombre");
            detalleActividad.setVisible(false);
            tablaActividades.setItems(FXCollections.observableArrayList());
        } else {
            ObservableList<ActividadResultado> resultadosObservable = FXCollections.observableArrayList();
            Nodo<ActividadResultado> actual = resultados.getCabeza();
            while (actual != null) {
                resultadosObservable.add(actual.getDato());
                actual = actual.getSiguiente();
            }
            tablaActividades.setItems(resultadosObservable);
            tablaActividades.getSelectionModel().selectFirst();
            detalleActividad.setVisible(true);
        }
    }

    private ListaEnlazada<ActividadResultado> buscarActividadEnProcesos(String nombreActividad) {
        ListaEnlazada<ActividadResultado> resultados = new ListaEnlazada<>();

        ListaEnlazada<Proceso> procesos = Persistencia.cargarProcesos();

        if (!procesos.estaVacia()) {
            Nodo<Proceso> nodoProceso = procesos.getCabeza();
            while (nodoProceso != null) {
                Proceso proceso = nodoProceso.getDato();
                ListaEnlazada<Actividad> actividades = proceso.obtenerlistaDeActividades();

                Nodo<Actividad> nodoActividad = actividades.getCabeza();
                while (nodoActividad != null) {
                    if (nodoActividad.getDato().obtenerNombre().toLowerCase().contains(nombreActividad.toLowerCase())) {
                        resultados.insertar(new ActividadResultado(proceso.obtenerTitulo(), nodoActividad.getDato()));
                    }
                    nodoActividad = nodoActividad.getSiguiente();
                }
                nodoProceso = nodoProceso.getSiguiente();
            }
        }

        return resultados;
    }

    private void mostrarDetalleActividad(Actividad actividad) {
        lblNombreActividad.setText(actividad.obtenerNombre());
        lblDescripcionActividad.setText(actividad.obtenerDescripcion());
        lblEstadoActividad.setText(actividad.esObligatoria() ? "Obligatoria" : "Opcional");

        ObservableList<Tarea> tareas = FXCollections.observableArrayList();
        Cola<Tarea> colaTareas = actividad.obtenerTareas();
        Nodo<Tarea> actual = colaTareas.obtenerPrimero();
        while (actual != null) {
            tareas.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        tablaTareas.setItems(tareas);
    }

    private int calcularTiempoMinimo(Actividad actividad) {
        int tiempoMinimo = 0;
        Cola<Tarea> tareas = actividad.obtenerTareas();
        Nodo<Tarea> actual = tareas.obtenerPrimero();

        while (actual != null) {
            if (actual.getDato().esObligatoria()) {
                tiempoMinimo += actual.getDato().obtenerDuracion();
            }
            actual = actual.getSiguiente();
        }

        return tiempoMinimo;
    }

    private int calcularTiempoMaximo(Actividad actividad) {
        int tiempoMaximo = 0;
        Cola<Tarea> tareas = actividad.obtenerTareas();
        Nodo<Tarea> actual = tareas.obtenerPrimero();

        while (actual != null) {
            tiempoMaximo += actual.getDato().obtenerDuracion();
            actual = actual.getSiguiente();
        }

        return tiempoMaximo;
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
