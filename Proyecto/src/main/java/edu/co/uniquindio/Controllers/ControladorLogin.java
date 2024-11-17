package edu.co.uniquindio.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Usuario;
import edu.co.uniquindio.Model.Administradores.AdministradorArchivos;

public class ControladorLogin {
    // FXML injected controls
    @FXML
    private TextField textUsuario;

    @FXML
    private PasswordField textContrasenia;

    @FXML
    private Button bntIngresar;

    @FXML
    private Button bntRegistrar;

    @FXML
    private Label textRecuperar;

    @FXML
    private CheckBox box1;

    @FXML
    private ImageView fondo1, fondo2;

    // Existing fields
    private ListaEnlazada<Usuario> usuarios;
    private Usuario usuarioActual;

    // Initialize method called by FXML loader
    @FXML
    public void initialize() {
        usuarios = new ListaEnlazada<>();
        cargarUsuarios();

        // Configure event handlers
        bntIngresar.setOnAction(event -> handleIngresar());
        bntRegistrar.setOnAction(event -> handleRegistrar());
        textRecuperar.setOnMouseClicked(event -> handleRecuperarContrasenia());

        // Clear default text on click
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

    @FXML
    private void handleIngresar() {
        String identificacion = textUsuario.getText();
        String contrasenia = textContrasenia.getText();

        if (identificacion.isEmpty() || contrasenia.isEmpty() ||
                identificacion.equals("Usuario") || contrasenia.equals("Contraseña")) {
            mostrarAlerta("Error", "Por favor complete todos los campos correctamente.");
            return;
        }

        if (autenticarUsuario(identificacion, contrasenia)) {
            mostrarAlerta("Éxito", "¡Inicio de sesión exitoso!");
            abrirVentanaPrincipal();
        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    private void handleRegistrar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/co/uniquindio/Views/registro.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de registro.");
        }
    }

    private void handleRecuperarContrasenia() {
        // Implementar lógica de recuperación de contraseña
        mostrarAlerta("Información", "Función de recuperar contraseña no implementada aún.");
    }

    private void abrirVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/co/uniquindio/Views/principal.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ventana Principal");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar ventana de login
            ((Stage) bntIngresar.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana principal.");
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    // Los métodos existentes se mantienen igual
    private void cargarUsuarios() {
        try {
            usuarios = AdministradorArchivos.cargarUsuarios();
            if (usuarios.getTamanio() == 0) {
                registrarUsuario("Admin", "1234", "admin@test.com", "admin123");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
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
            System.out.println("El usuario con esta identificación ya existe.");
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
            if (usuarios.getElementoEnPosicion(i).getIdentificacion().equals(identificacion) &&
                    usuarios.getElementoEnPosicion(i).getContrasenia().equals(contrasenia)) {
                usuarioActual = usuarios.getElementoEnPosicion(i);
                return true;
            }
        }
        return false;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}