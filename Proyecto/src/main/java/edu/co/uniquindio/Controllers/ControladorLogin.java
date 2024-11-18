package edu.co.uniquindio.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import edu.co.uniquindio.Model.Administradores.AdministradorArchivos;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Usuario;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorLogin implements Initializable {
    @FXML
    private TextField textUsuario;

    @FXML
    private PasswordField textContrasenia;

    @FXML
    private Button bntIngresar;

    @FXML
    private Button bntRegistrar;

    @FXML
    private CheckBox box1;

    @FXML
    private Label textRecuperar;

    private ListaEnlazada<Usuario> usuarios;
    private Usuario usuarioActual;

    public ControladorLogin() {
        usuarios = new ListaEnlazada<>();
        cargarUsuarios();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar los event handlers
        configurarCamposTexto();
        configurarBotones();
        configurarCheckBox();
        configurarRecuperarContrasenia();
    }

    private void configurarCamposTexto() {
        // Limpiar el texto por defecto al hacer focus
        textUsuario.setOnMouseClicked(event -> {
            if (textUsuario.getText().equals("Usuario")) {
                textUsuario.clear();
            }
        });

        textContrasenia.setOnMouseClicked(event -> {
            if (textContrasenia.getText().equals("Contraseña")) {
                textContrasenia.clear();
            }
        });
    }

    private void configurarBotones() {
        bntIngresar.setOnAction(event -> manejarIngresoUsuario());
        bntRegistrar.setOnAction(event -> abrirVentanaRegistro());
    }

    private void configurarCheckBox() {
        box1.setOnAction(event -> {
            if (box1.isSelected()) {
                // Implementar lógica para recordar usuario
                guardarPreferenciasUsuario();
            }
        });
    }

    private void configurarRecuperarContrasenia() {
        textRecuperar.setOnMouseClicked(event -> abrirVentanaRecuperacion());
    }

    @FXML
    private void handleButtonHover(javafx.scene.input.MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button.getId().equals("bntIngresar")) {
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand; -fx-scale-x: 1.1; -fx-scale-y: 1.1;");
        } else if (button.getId().equals("bntRegistrar")) {
            button.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand; -fx-scale-x: 1.1; -fx-scale-y: 1.1;");
        }
    }

    @FXML
    private void handleButtonExit(javafx.scene.input.MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-cursor: hand; -fx-scale-x: 1; -fx-scale-y: 1;");
    }

    private void manejarIngresoUsuario() {
        String identificacion = textUsuario.getText();
        String contrasenia = textContrasenia.getText();

        if (identificacion.isEmpty() || contrasenia.isEmpty() ||
                identificacion.equals("Usuario") || contrasenia.equals("Contraseña")) {
            mostrarAlerta("Error", "Por favor complete todos los campos.", AlertType.ERROR);
            return;
        }

        if (autenticarUsuario(identificacion, contrasenia)) {
            mostrarAlerta("Éxito", "Bienvenido " + usuarioActual.getNombre(), AlertType.INFORMATION);
            // Aquí puedes agregar la lógica para abrir la ventana principal de tu aplicación
        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.", AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String contenido, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void abrirVentanaRegistro() {
        // Implementar la lógica para abrir la ventana de registro
        mostrarAlerta("Registro", "Función de registro en desarrollo", AlertType.INFORMATION);
    }

    private void abrirVentanaRecuperacion() {
        // Implementar la lógica para abrir la ventana de recuperación de contraseña
        mostrarAlerta("Recuperación", "Función de recuperación en desarrollo", AlertType.INFORMATION);
    }

    private void guardarPreferenciasUsuario() {
        // Implementar la lógica para guardar las preferencias del usuario
        // Por ejemplo, usando Properties o un archivo de configuración
    }

    private void cargarUsuarios() {
        try {
            usuarios = AdministradorArchivos.cargarUsuarios();
            if (usuarios.getTamanio() == 0) {
                registrarUsuario("Admin", "1234", "admin@test.com", "admin123");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
            usuarios = new ListaEnlazada<>();
            registrarUsuario("Admin", "1234", "admin@test.com", "admin123");
        }
    }

    private void guardarCambios() {
        try {
            AdministradorArchivos.guardarUsuarios(usuarios);
        } catch (Exception e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    public void registrarUsuario(String nombre, String identificacion, String correo, String contrasenia) {
        if (existeUsuario(identificacion)) {
            return;
        }
        Usuario nuevoUsuario = new Usuario(nombre, identificacion, correo, contrasenia);
        usuarios.insertar(nuevoUsuario);
        guardarCambios();
    }

    private boolean existeUsuario(String identificacion) {
        for (int i = 0; i < usuarios.getTamanio(); i++) {
            if (usuarios.getElementoEnPosicion(i).getIdentificacion().equals(identificacion)) {
                return true;
            }
        }
        return false;
    }

    public boolean autenticarUsuario(String identificacion, String contrasenia) {
        for (int i = 0; i < usuarios.getTamanio(); i++) {
            Usuario usuario = usuarios.getElementoEnPosicion(i);
            if (usuario.getIdentificacion().equals(identificacion) &&
                    usuario.getContrasenia().equals(contrasenia)) {
                usuarioActual = usuario;
                return true;
            }
        }
        return false;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}