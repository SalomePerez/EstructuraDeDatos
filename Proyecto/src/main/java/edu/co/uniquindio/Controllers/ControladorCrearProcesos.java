package edu.co.uniquindio.Controllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.co.uniquindio.Model.Auxiliares.Persistencia;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Tarea;
import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorCrearProcesos implements Initializable {

    public JFXButton btnAgregarTarea;
    private Proceso procesoActual;
    private Actividad actividadActual;
    private Cola<Tarea> tareasTemporales;

    // Campos para el proceso
    @FXML
    private JFXTextField txtNombreProceso;

    // Campos para actividades
    @FXML
    private JFXTextField txtNombreActividad;
    @FXML
    private JFXTextArea txtDescripcionActividad;
    @FXML
    private JFXCheckBox chkActividadObligatoria;
    @FXML
    private JFXComboBox<String> cmbPosicionActividad;
    @FXML
    private JFXTextField txtActividadPrevia;
    @FXML
    private VBox actividadesContent;
    @FXML
    private JFXButton btnToggleActividades;

    // Campos para tareas
    @FXML
    private JFXTextField txtNombreTarea;
    @FXML
    private JFXTextArea txtDescripcionTarea;
    @FXML
    private JFXCheckBox chkTareaObligatoria;
    @FXML
    private JFXTextField txtDuracionTarea;
    @FXML
    private JFXTextField txtPosicionTarea;
    @FXML
    private VBox tareasContent;
    @FXML
    private JFXButton btnToggleTareas;

    // Botones de acción
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXComboBox<String> cmbActividades;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComponents();
        setupValidations();
//        setupListeners();
        tareasTemporales = new Cola<>();
    }

    private void initializeComponents() {
        cmbPosicionActividad.getItems().addAll("Al inicio", "Al final", "Después de...");
        actividadesContent.setVisible(false);
        tareasContent.setVisible(false);
        deshabilitarSeccionTareas(true);
    }

    private void deshabilitarSeccionTareas(boolean deshabilitar) {
        txtNombreTarea.setDisable(deshabilitar);
        txtDescripcionTarea.setDisable(deshabilitar);
        chkTareaObligatoria.setDisable(deshabilitar);
        txtDuracionTarea.setDisable(deshabilitar);
        txtPosicionTarea.setDisable(deshabilitar);
        btnAgregarTarea.setDisable(deshabilitar);
    }
    
    private void setupValidations() {
        txtNombreProceso.setValidators(
                new RequiredFieldValidator("El nombre del proceso es requerido")
        );

        txtDuracionTarea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtDuracionTarea.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void toggleActividades() {
        actividadesContent.setVisible(!actividadesContent.isVisible());
        btnToggleActividades.setText(actividadesContent.isVisible() ? "-" : "+");
    }

    @FXML
    private void toggleTareas() {
        tareasContent.setVisible(!tareasContent.isVisible());
        btnToggleTareas.setText(tareasContent.isVisible() ? "-" : "+");
    }

    @FXML
    private void agregarActividad() {
        if (!validarCamposActividad()) {
            return;
        }

        // Crear el proceso si no existe
        if (procesoActual == null) {
            if (txtNombreProceso.getText().trim().isEmpty()) {
                mostrarError("Debe especificar un nombre para el proceso primero");
                return;
            }
            procesoActual = new Proceso(txtNombreProceso.getText());
        }

        Actividad nuevaActividad = new Actividad(
                txtNombreActividad.getText(),
                txtDescripcionActividad.getText(),
                chkActividadObligatoria.isSelected()
        );

        // Agregar las tareas temporales a la nueva actividad
        while (!tareasTemporales.estaVacia()) {
            nuevaActividad.agregarTarea(tareasTemporales.desencolar());
        }

        // Manejar la posición de la actividad
        String posicion = cmbPosicionActividad.getValue();
        if (posicion == null) posicion = "Al final";

        switch (posicion) {
            case "Al inicio":
                if (!procesoActual.obtenerlistaDeActividades().estaVacia()) {
                    Actividad primera = procesoActual.obtenerlistaDeActividades().getElementoEnPosicion(0);
                    nuevaActividad.establecerActividadSiguiente(primera);
                    primera.establecerActividadAnterior(nuevaActividad);
                }
                procesoActual.obtenerlistaDeActividades().insertarAlInicio(nuevaActividad);
                break;

            case "Después de...":
                String nombreActividadPrevia = txtActividadPrevia.getText().trim();
                if (!nombreActividadPrevia.isEmpty()) {
                    boolean encontrada = false;
                    for (int i = 0; i < procesoActual.obtenerlistaDeActividades().getTamanio(); i++) {
                        Actividad actividadPrevia = procesoActual.obtenerlistaDeActividades().getElementoEnPosicion(i);
                        if (actividadPrevia.obtenerNombre().equals(nombreActividadPrevia)) {
                            Actividad siguiente = actividadPrevia.obtenerActividadSiguiente();
                            nuevaActividad.establecerActividadAnterior(actividadPrevia);
                            nuevaActividad.establecerActividadSiguiente(siguiente);
                            actividadPrevia.establecerActividadSiguiente(nuevaActividad);

                            if (siguiente != null) {
                                siguiente.establecerActividadAnterior(nuevaActividad);
                            }

                            procesoActual.obtenerlistaDeActividades().insertarDespuesDe(actividadPrevia, nuevaActividad);
                            encontrada = true;
                            break;
                        }
                    }
                    if (!encontrada) {
                        mostrarError("No se encontró la actividad previa especificada");
                        return;
                    }
                }
                break;

            default: // "Al final" o cualquier otro caso
                if (!procesoActual.obtenerlistaDeActividades().estaVacia()) {
                    int ultimoIndice = procesoActual.obtenerlistaDeActividades().getTamanio() - 1;
                    Actividad ultima = procesoActual.obtenerlistaDeActividades().getElementoEnPosicion(ultimoIndice);
                    ultima.establecerActividadSiguiente(nuevaActividad);
                    nuevaActividad.establecerActividadAnterior(ultima);
                }
                procesoActual.obtenerlistaDeActividades().insertar(nuevaActividad);
                break;
        }

        actividadActual = nuevaActividad;

        // Debug: Imprimir estado actual
        System.out.println("\n=== Estado actual del proceso ===");
        System.out.println("Número de actividades: " +
                procesoActual.obtenerlistaDeActividades().getTamanio());
        for (int i = 0; i < procesoActual.obtenerlistaDeActividades().getTamanio(); i++) {
            Actividad act = procesoActual.obtenerlistaDeActividades().getElementoEnPosicion(i);
            System.out.println("Actividad " + i + ": " + act.obtenerNombre());
            if (act.obtenerActividadAnterior() != null) {
                System.out.println("  - Anterior: " + act.obtenerActividadAnterior().obtenerNombre());
            }
            if (act.obtenerActividadSiguiente() != null) {
                System.out.println("  - Siguiente: " + act.obtenerActividadSiguiente().obtenerNombre());
            }
        }
        System.out.println("===============================\n");

        actualizarComboBoxActividades();

        // Limpiar campos y mostrar mensaje
        limpiarCamposActividad();
        mostrarMensajeExito("Actividad agregada exitosamente");
        deshabilitarSeccionTareas(false);
    }

    private void actualizarComboBoxActividades() {
        cmbActividades.getItems().clear();
        if (procesoActual != null) {
            for (int i = 0; i < procesoActual.obtenerlistaDeActividades().getTamanio(); i++) {
                Actividad actividad = procesoActual.obtenerlistaDeActividades().getElementoEnPosicion(i);
                cmbActividades.getItems().add(actividad.obtenerNombre());
            }
        }
    }

    private void seleccionarActividadPorNombre(String nombreActividad) {
        if (procesoActual != null) {
            for (int i = 0; i < procesoActual.obtenerlistaDeActividades().getTamanio(); i++) {
                Actividad actividad = procesoActual.obtenerlistaDeActividades().getElementoEnPosicion(i);
                if (actividad.obtenerNombre().equals(nombreActividad)) {
                    actividadActual = actividad;
                    break;
                }
            }
        }
    }

    @FXML
    private void agregarTarea() {
        if (actividadActual == null) {
            mostrarError("Debe seleccionar una actividad antes de agregar una tarea");
            return;
        }

        if (!validarCamposTarea()) {
            return;
        }

        Tarea nuevaTarea = new Tarea(
                txtDescripcionTarea.getText(),
                Integer.parseInt(txtDuracionTarea.getText()),
                chkTareaObligatoria.isSelected()
        );

        actividadActual.agregarTarea(nuevaTarea);

        limpiarCamposTarea();
        mostrarMensajeExito("Tarea agregada exitosamente a la actividad: " + actividadActual.obtenerNombre());
    }

    @FXML
    private void guardarProceso() {
        if (!validarProcesoCompleto()) {
            return;
        }

        if (procesoActual == null) {
            procesoActual = new Proceso(txtNombreProceso.getText());
        }

        // Verificar que haya al menos una actividad
        if (procesoActual.obtenerlistaDeActividades().estaVacia()) {
            mostrarError("El proceso debe tener al menos una actividad");
            return;
        }

        // Guardar el proceso en XML
        Persistencia.guardarProceso(procesoActual);

        System.out.println(procesoActual.toString());

        mostrarMensajeExito("Proceso guardado exitosamente");
        limpiarTodo();
    }

    private void limpiarTodo() {
        txtNombreProceso.clear();
        limpiarCamposActividad();
        limpiarCamposTarea();
        procesoActual = null;
        actividadActual = null;
        tareasTemporales = new Cola<>();
    }

    private boolean validarCamposActividad() {
        if (txtNombreActividad.getText().trim().isEmpty()) {
            mostrarError("El nombre de la actividad es requerido");
            return false;
        }
        if (txtDescripcionActividad.getText().trim().isEmpty()) {
            mostrarError("La descripción de la actividad es requerida");
            return false;
        }
        return true;
    }

    private boolean validarCamposTarea() {
        if (txtNombreTarea.getText().trim().isEmpty()) {
            mostrarError("El nombre de la tarea es requerido");
            return false;
        }
        if (txtDescripcionTarea.getText().trim().isEmpty()) {
            mostrarError("La descripción de la tarea es requerida");
            return false;
        }
        if (txtDuracionTarea.getText().trim().isEmpty()) {
            mostrarError("La duración de la tarea es requerida");
            return false;
        }
        try {
            Integer.parseInt(txtDuracionTarea.getText());
        } catch (NumberFormatException e) {
            mostrarError("La duración debe ser un número válido");
            return false;
        }
        return true;
    }

    private boolean validarProcesoCompleto() {
        if (txtNombreProceso.getText().trim().isEmpty()) {
            mostrarError("El nombre del proceso es requerido");
            return false;
        }
        return true;
    }

    private void limpiarCamposActividad() {
        txtNombreActividad.clear();
        txtDescripcionActividad.clear();
        chkActividadObligatoria.setSelected(false);
        txtActividadPrevia.clear();
    }

    private void limpiarCamposTarea() {
        txtNombreTarea.clear();
        txtDescripcionTarea.clear();
        chkTareaObligatoria.setSelected(false);
        txtDuracionTarea.clear();
        txtPosicionTarea.clear();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarMensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cancelar() {
        // Aquí puedes agregar la lógica para cerrar la ventana
        limpiarTodo();
    }
}