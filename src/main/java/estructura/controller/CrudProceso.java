package estructura.controller;

import estructura.MainApp;
import estructura.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class CrudProceso {

    private MainApp app;

    private ModelFactory modelFactory;

    private Proceso procesoSeleccionado;

    private Actividad actividadSeleccionada;

    private Tarea tareaSeleccionada;


    //Items Proceso
    @FXML
    private TableColumn<?, ?> colIdProceso;

    @FXML
    private TableColumn<?, ?> colNombreProceso;

    @FXML
    private TableColumn<?, ?> colNumActividades;

    @FXML
    private Tab tabProcesos;
    @FXML
    private Tab tabActividades;
    @FXML
    private Tab tabTareas;

    @FXML
    private TableView<Proceso> tblProcesos;

    @FXML
    private TextField txtNombreProceso;
    @FXML
    private TextField txtAux;

    //items actividades
    @FXML
    private SplitMenuButton SplitCrear;
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private TableColumn<?, ?> colProcesoTarea;
    @FXML
    private TableView<Proceso> tblProcesosTareas;
    @FXML
    private MenuButton crearTarea;

    @FXML
    private CheckBox checkObligatorio;

    @FXML
    private TextField txtPosicionTarea;
    @FXML
    private TextField txtDescripcionTarea;
    @FXML
    private TextField txtDuracionTarea;
    @FXML
    private CheckBox checkBoxTarea;
    @FXML
    private TableColumn<?, ?> colNombreActividad;

    @FXML
    private TableColumn<?, ?> colDescripcionActividad;

    @FXML
    private TableColumn<?, ?> colEstado;

    @FXML
    private TableColumn<?, ?> colNombreProceso2;

    @FXML
    private TableView<Actividad> tblActividades;

    @FXML
    private TableView<Proceso> tblProcesos2;

    @FXML
    private TableColumn<?, ?> colDescripcionTarea;
    @FXML
    private TableColumn<?, ?> colDuracionTarea;
    @FXML
    private TableColumn<?, ?> colObligatorioTarea;
    @FXML
    private TableView<Tarea> tblTareas;

    @FXML
    private TableColumn<?, ?> colNombreActividadTarea;
    @FXML
    private TableColumn<?, ?> colDescripcionActividadTarea;
    @FXML
    private TableColumn<?, ?> colEstadoActividadTarea;
    @FXML
    private TableView<Actividad> tblActividadesTareas;

    @FXML
    private TableColumn<?, ?>  colActividadDetalleActividad;
    @FXML
    private TableColumn<?, ?> colDescripcionDetalleActividad;
    @FXML
    private TableColumn<?, ?> colTareasDetalleActividad;
    @FXML
    private TableColumn<?, ?> colTiempoMinimoDetalleActividad;
    @FXML
    private TableColumn<?, ?> colTiempoMaximoDetalleActividad;
    @FXML
    private TableColumn<?, ?> colProcesosDetalleActividad;
    @FXML
    private TableView<Actividad> tablaDetalleActividades;
    @FXML
    private TextField txtNombreActividad;

    @FXML
    private TextField txtDescripcionActividad;
    @FXML
    MenuItem crearUltimoActividad;
    @FXML
    MenuItem crearDespuesActividad;
    @FXML
    MenuItem crearOrdenActividad;

    private ObservableList<Proceso> listaProcesos = FXCollections.observableArrayList();
    private ObservableList<Proceso> listaProcesosBuscar = FXCollections.observableArrayList();
    private ObservableList<Proceso> listaProcesosAct = FXCollections.observableArrayList();
    private ObservableList<Actividad> listaActividades = FXCollections.observableArrayList();
    private ObservableList<Actividad> listaActividadesDetalle = FXCollections.observableArrayList();
    private ObservableList<Tarea> listaTareas = FXCollections.observableArrayList();


    @FXML
    void onCrearDespuesDe(ActionEvent event) {
        if (procesoSeleccionado != null) {
            String nombreActividad = txtNombreActividad.getText();
            String descripcionActividad = txtDescripcionActividad.getText();
            if (verificarCamposActividad()) {
                if (actividadSeleccionada != null) {
                    Actividad nuevaActividad = null;
                    Actividad actividad = new Actividad();
                    actividad.setNombre(nombreActividad);
                    actividad.setDescripcion(descripcionActividad);
                    actividad.setObligatoria(obtenerEstado());
                    nuevaActividad = modelFactory.crearActividadDespuesDe(procesoSeleccionado, actividadSeleccionada,
                            actividad);
                    if (nuevaActividad != null) {
                        listaActividades.add(nuevaActividad);
                        cargarActividadesEnTabla();
                        limpiarCamposActividad();
                        mostrarMensaje("Actividad Registrada");
                    } else {
                        mostrarMensaje("Actividad no registrada");
                    }
                } else {
                    mostrarMensaje("Debe seleccionar una actividad para insertar " + txtNombreActividad.getText() + " después" +
                            " de esta");
                }
            } else {
                mostrarMensaje("Las actividades deben tener nombre y descripción");
            }
        } else {
            mostrarMensaje("Debe seleccionar un proceso");
        }
    }

    @FXML
    void crearTareaFinal(ActionEvent event) {
        if (verificarCamposActividadTarea()) {
            String descripcionTarea = txtDescripcionTarea.getText();
            int duracion = Integer.parseInt(txtDuracionTarea.getText());

            Tarea nuevaTarea = null;
            Tarea tarea = new Tarea();
            tarea.setEstado(Estado.valueOf(obtenerEstadoTarea()));
            tarea.setDescripcion(descripcionTarea);
            tarea.setDuracionMinutos(duracion);
            nuevaTarea = modelFactory.crearTareaFinal(procesoSeleccionado, actividadSeleccionada, tarea);
            if (nuevaTarea != null) {
                listaTareas.add(nuevaTarea);
                cargarTareasEnTabla();
                limpiarCamposTarea();
                mostrarMensaje("Tarea Registrada");
            } else {
                mostrarMensaje("Tarea no registrada");
            }
        } else {
            mostrarMensaje("Las Tareas deben tener descripción y duración");
        }
    }

    @FXML
    void onCrearUltimo(ActionEvent event) {
        if (procesoSeleccionado != null) {
            String nombreActividad = txtNombreActividad.getText();
            String descripcionActividad = txtDescripcionActividad.getText();

            if (verificarCamposActividad()) {
                Actividad nuevaActividad = null;
                Actividad actividad = new Actividad();
                actividad.setNombre(nombreActividad);
                actividad.setDescripcion(descripcionActividad);
                actividad.setObligatoria(obtenerEstado());
                nuevaActividad = modelFactory.crearActividadFinal(procesoSeleccionado, actividad);
                if (nuevaActividad != null) {
                    listaActividades.add(nuevaActividad);
                    cargarActividadesEnTabla();
                    cargarProcesosEnTabla();
                    limpiarCamposActividad();
                    mostrarMensaje("Actividad Registrada");
                } else {
                    mostrarMensaje("Actividad no registrada");
                }
            } else {
                mostrarMensaje("Las actividades deben tener nombre y descripción");
            }
        } else {
            mostrarMensaje("Debe seleccionar un proceso");
        }
    }

    private boolean verificarCamposActividad() {
        return !txtNombreActividad.getText().isEmpty() || !txtDescripcionActividad.getText().isEmpty();
    }

    private boolean verificarCamposActividadTarea() {
        return !txtDuracionTarea.getText().isEmpty() || !txtDescripcionActividad.getText().isEmpty();
    }

    @FXML
    void onCrearOrden(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        modelFactory = ModelFactory.getInstance();
        //inicializar la tabla de procesos
        colIdProceso.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreProceso.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNumActividades.setCellValueFactory(new PropertyValueFactory<>("numActividades"));
        colNombreProceso2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombreActividad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionActividad.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("obligatoria"));

        tblProcesos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                procesoSeleccionado = (Proceso) newSelection;
            }
        });
        tblProcesos2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                procesoSeleccionado = (Proceso) newSelection;
                cargarActividadesEnTabla();
            }
        });
        tblActividades.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                actividadSeleccionada = (Actividad) newSelection;
            }
        });
        tblProcesosTareas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                procesoSeleccionado = (Proceso) newSelection;
                cargarActividadesTareas();
            }
        });
        tblActividadesTareas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                actividadSeleccionada = (Actividad) newSelection;
                cargarTareasEnTabla();
            }
        });
        tblTareas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tareaSeleccionada = (Tarea) newSelection;
            }
        });
        cargarProcesosEnTabla();
        cargarProcesosNombreEnTabla();
        cargarActividadesEnTabla();
        cargarProcesosTarea();
        cargarTareasEnTabla();
        cargarActividadesTareas();
    }
    
    public void cargarActividadesDetalle(String actividad) {
        colActividadDetalleActividad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionDetalleActividad.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colTareasDetalleActividad.setCellValueFactory(new PropertyValueFactory<>("tareas"));
        colTiempoMinimoDetalleActividad.setCellValueFactory(new PropertyValueFactory<>("tiempoMinimo"));
        colTiempoMaximoDetalleActividad.setCellValueFactory(new PropertyValueFactory<>("tiempoMaximo"));
        colProcesosDetalleActividad.setCellValueFactory(new PropertyValueFactory<>("proceso"));
        tablaDetalleActividades.getItems().clear();
        tablaDetalleActividades.setItems(buscarActivades(actividad));
        tablaDetalleActividades.refresh();
    }

    private ObservableList<Actividad> buscarActivades(String actividad) {
        listaActividadesDetalle.addAll(modelFactory.getAplicacion().buscarActividadesDetalle(actividad));
        return listaActividadesDetalle;
    }

    private String obtenerEstado() {
        return checkObligatorio.isSelected() ? "Obligatorio" : "No Obligatorio";
    }

    private String obtenerEstadoTarea() {
        return checkBoxTarea.isSelected() ? "OBLIGATORIO" : "OPCIONAL";
    }

    @FXML
    void onRegistrarClick(ActionEvent event) {
        String id = String.valueOf(Proceso.generarID());
        String nombre = txtNombreProceso.getText();
        int numActividades = 0;

        if (nombre.isEmpty()) {
            mostrarMensaje("Por favor escriba el nombre");
            return;
        }
        Proceso nuevoProceso = null;
        Proceso proceso = new Proceso();
        proceso.setId(id);
        proceso.setNombre(nombre);
        proceso.setNumActividades(numActividades);
        nuevoProceso = modelFactory.crearProceso(proceso);
        if (nuevoProceso != null) {
            listaProcesos.add(nuevoProceso);
            cargarProcesosEnTabla();
            limpiarCamposProceso();
            mostrarMensaje("Proceso registrado correctamente.");
        } else {
            mostrarMensaje("Proceso ya existe");
        }
    }

    //eliminar proceso
    @FXML
    void onRemoverClick(ActionEvent event) {
        if (procesoSeleccionado != null) {
            modelFactory.getAplicacion().eliminarProceso(procesoSeleccionado);
            cargarProcesosEnTabla();
            mostrarMensaje("Proceso eliminado correctamente.");
        } else {
            mostrarMensaje("Seleccione un proceso antes de intentar eliminarlo.");
        }
    }

    @FXML
    void onBuscarClick(ActionEvent event) {
        String buscado = txtAux.getText();
        boolean bandera = false;
        for (Proceso proceso : listaProcesos) {
            if (buscado.equals(proceso.getNombre())) {
                bandera = true;
                listaProcesosBuscar.add(proceso);
                cargarProcesosBuscar();
                break;
            }
        }

        if (!bandera) {
            mostrarMensaje("Proceso no encontrado");
        }
    }

    private void cargarProcesosBuscar() {
        colNombreProceso.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdProceso.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNumActividades.setCellValueFactory(new PropertyValueFactory<>("numActividades"));

        tblProcesos.getItems().clear();
        tblProcesos.setItems(getListaProcesosBuscar());
        tblProcesos.refresh();
    }

    private ObservableList<Proceso> getListaProcesosBuscar() {
        return listaProcesosBuscar;
    }

    @FXML
    void onCambiarNombreClick(ActionEvent event) {
        if (procesoSeleccionado != null) {
            String nombre = txtAux.getText();
            procesoSeleccionado.setNombre(nombre); // actualizar en modelFactory
            mostrarMensaje("Proceso cambiado correctamente.");
        } else {
            mostrarMensaje("Proceso no cambiado");
        }
        cargarProcesosEnTabla();
        limpiarCamposProceso();
    }

    @FXML
    void onCancelarClick(ActionEvent event) {
        limpiarCamposProceso();
        cargarProcesosEnTabla();
    }

    @FXML
    void onConsultarTiempoProceso(ActionEvent event) {
        if (procesoSeleccionado != null) {
            int tiempoTotalProceso = procesoSeleccionado.getTiempoTotalProceso();
            mostrarMensaje("Tiempo total del proceso de " + procesoSeleccionado.getNombre() + " : " + tiempoTotalProceso + " minutos");
        } else {
            mostrarMensaje("Por favor, selecciona un proceso antes de consultar el tiempo.");
        }
    }

    public MainApp getApp() {
        return app;
    }

    public void setApp(MainApp app) {
        this.app = app;
    }

    // Cargar procesos en la tabla
    public void cargarProcesosEnTabla() {
        colNombreProceso.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdProceso.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNumActividades.setCellValueFactory(new PropertyValueFactory<>("numActividades"));

        tblProcesos.getItems().clear();
        tblProcesos.setItems(getListaProcesos());
        tblProcesos.refresh();
    }

    public void cargarProcesosNombreEnTabla() {
        colNombreProceso.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tblProcesos2.getItems().clear();
        tblProcesos2.setItems(getListaProcesosAct());
        tblProcesos2.refresh();
    }

    public void cargarProcesosTarea() {
        colProcesoTarea.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tblProcesosTareas.getItems().clear();
        tblProcesosTareas.setItems(getListaProcesosTarea());
        tblProcesosTareas.refresh();
    }

    public void cargarActividadesEnTabla() {
        colNombreActividad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionActividad.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("obligatoria"));
        tblActividades.getItems().clear();
        tblActividades.setItems(getListaActivades(procesoSeleccionado));
        tblActividades.refresh();
    }

    public void cargarActividadesTareas() {
        colNombreActividadTarea.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionActividadTarea.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colEstadoActividadTarea.setCellValueFactory(new PropertyValueFactory<>("obligatoria"));
        tblActividadesTareas.getItems().clear();
        tblActividadesTareas.setItems(getListaActivades(procesoSeleccionado));
        tblActividadesTareas.refresh();
    }

    public void cargarTareasEnTabla() {
        colDescripcionTarea.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colDuracionTarea.setCellValueFactory(new PropertyValueFactory<>("duracionMinutos"));
        colObligatorioTarea.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tblTareas.getItems().clear();
        tblTareas.setItems(getListaTareas(procesoSeleccionado, actividadSeleccionada));
        tblTareas.refresh();
    }

    public ObservableList<Proceso> getListaProcesos() {
        listaProcesos.addAll(modelFactory.getAplicacion().getListaProcesos());
        return listaProcesos;
    }

    public ObservableList<Proceso> getListaProcesosAct() {
        listaProcesosAct.addAll(modelFactory.getAplicacion().getListaProcesos());
        return listaProcesos;
    }

    public ObservableList<Proceso> getListaProcesosTarea() {
        listaProcesosAct.addAll(modelFactory.getAplicacion().getListaProcesos());
        return listaProcesos;
    }

    public ObservableList<Actividad> getListaActivades(Proceso proceso) {
        if (proceso != null)
            listaActividades.addAll(modelFactory.getAplicacion().buscarActividades(proceso));
        return listaActividades;
    }

    public ObservableList<Tarea> getListaTareas(Proceso proceso, Actividad actividad) {
        if (actividad != null)
            listaTareas.addAll(modelFactory.getAplicacion().buscarTareas(proceso, actividad));
        return listaTareas;
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    void limpiarCamposProceso() {
        txtNombreProceso.clear();
    }

    void limpiarCamposActividad() {
        txtNombreActividad.clear();
        txtDescripcionActividad.clear();
        checkObligatorio.setSelected(false);
    }

    void limpiarCamposTarea() {
        txtDescripcionTarea.clear();
        txtDuracionTarea.clear();
        txtPosicionTarea.clear();
        checkBoxTarea.setSelected(false);
    }

    public void registrarUsuario(ActionEvent actionEvent) {
        String usuario = txtUser.getText();
        String contrasena = pfPassword.getText();
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos.");
            return;
        }
        Usuario user = new Usuario(usuario, contrasena);
        if (modelFactory.validarUsuario(user)) {
            mostrarMensaje("El usuario ya está registrado");
            return;
        } else {
            modelFactory.registrarUsuario(user);
            tabProcesos.setDisable(true);
            tabActividades.setDisable(true);
            tabTareas.setDisable(true);
            txtUser.clear();
            pfPassword.clear();
            mostrarMensaje("Usuario registrado correctamente");
        }
    }

    public void login(ActionEvent actionEvent) {
        String usuario = txtUser.getText();
        String contrasena = pfPassword.getText();
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos.");
            return;
        }
        Usuario user = new Usuario(usuario, contrasena);
        if (modelFactory.validarUsuario(user)) {
            tabProcesos.setDisable(false);
            tabActividades.setDisable(false);
            tabTareas.setDisable(false);
            txtUser.clear();
            pfPassword.clear();
        } else {
            mostrarMensaje("Error de Inicio de Sesión");
        }
    }

    public void crearTareaPosicion(ActionEvent actionEvent) {
        if (verificarCamposActividadTarea()) {
            String descripcionTarea = txtDescripcionTarea.getText();
            int duracion = Integer.parseInt(txtDuracionTarea.getText());
            int posicion = Integer.parseInt(txtPosicionTarea.getText());

            Tarea nuevaTarea = null;
            Tarea tarea = new Tarea();
            tarea.setEstado(Estado.valueOf(obtenerEstadoTarea()));
            tarea.setDescripcion(descripcionTarea);
            tarea.setDuracionMinutos(duracion);
            nuevaTarea = modelFactory.crearTareaPosicion(procesoSeleccionado, actividadSeleccionada, tarea, posicion);
            if (nuevaTarea != null) {
                listaTareas.add(nuevaTarea);
                cargarTareasEnTabla();
                limpiarCamposTarea();
                mostrarMensaje("Tarea Registrada");
            } else {
                mostrarMensaje("Tarea no registrada");
            }
        } else {
            mostrarMensaje("Las Tareas deben tener descripción y duración");
        }
    }

    public void buscarActividad(ActionEvent actionEvent) {
        String actividad = txtNombreActividad.getText();
        cargarActividadesDetalle(actividad);
    }

    public void onEliminarActividad(ActionEvent actionEvent) {
        if (procesoSeleccionado != null && actividadSeleccionada != null) {
            modelFactory.eliminarActividad(procesoSeleccionado, actividadSeleccionada);
            cargarActividadesEnTabla();
            limpiarCamposActividad();
            mostrarMensaje("Actividad eliminada correctamente.");
        } else {
            mostrarMensaje("Selecciona un proceso y una actividad antes de intentar eliminar.");
        }
    }

    public void onConsultarTiempoActividad(ActionEvent actionEvent) {
        if (procesoSeleccionado != null && actividadSeleccionada != null) {
            int tiempoTotalActividad = Integer.parseInt(actividadSeleccionada.getTiempoMaximo());
            mostrarMensaje("Tiempo total de la actividad es: "+actividadSeleccionada.getNombre()+": "+tiempoTotalActividad+ " minutos");
        } else {
            mostrarMensaje("Por favor, selecciona un proceso o actividad antes de consultar.");

        }
    }

    public void cerrarSesion(ActionEvent actionEvent) {
        tabProcesos.setDisable(true);
        tabActividades.setDisable(true);
        tabTareas.setDisable(true);
    }
}
