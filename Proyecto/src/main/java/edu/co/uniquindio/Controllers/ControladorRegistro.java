package edu.co.uniquindio.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import edu.co.uniquindio.Model.Administradores.AdministradorArchivos;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Usuario;

import java.io.IOException;

public class ControladorRegistro {

    @FXML
    private TextField textNombre;

    @FXML
    private TextField textCorreo;

    @FXML
    private TextField textCelular;

    @FXML
    private TextField textContraseniaRegistro;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private Button btnVolverLogin;

    private ListaEnlazada<Usuario> usuarios;

    public ControladorRegistro() {
        usuarios = new ListaEnlazada<>();
        cargarUsuarios();
    }

    @FXML
    private void initialize() {
        // Configurar los eventos de los botones
        btnRegistrarse.setOnAction(event -> registrarUsuario());
        btnVolverLogin.setOnAction(event -> volverLogin());
    }

    private void cargarUsuarios() {
        try {
            usuarios = AdministradorArchivos.cargarUsuarios();
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
            usuarios = new ListaEnlazada<>();
        }
    }

    private void registrarUsuario() {
        String nombre = textNombre.getText();
        String correo = textCorreo.getText();
        String celular = textCelular.getText();
        String contrasenia = textContraseniaRegistro.getText();

        if (nombre.isEmpty() || correo.isEmpty() || celular.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta("Error", "Por favor complete todos los campos.", AlertType.ERROR);
            return;
        }

        if (existeUsuario(correo)) {
            mostrarAlerta("Error", "Ya existe un usuario con ese correo.", AlertType.ERROR);
            return;
        }

        Usuario nuevoUsuario = new Usuario(nombre, correo, celular, contrasenia);
        usuarios.insertar(nuevoUsuario);
        guardarUsuarios();
        mostrarAlerta("Éxito", "Registro exitoso. Ahora puede iniciar sesión.", AlertType.INFORMATION);
        volverLogin();
    }

    private boolean existeUsuario(String correo) {
        for (int i = 0; i < usuarios.getTamanio(); i++) {
            if (usuarios.getElementoEnPosicion(i).getCorreo().equals(correo)) {
                return true;
            }
        }
        return false;
    }

    private void mostrarAlerta(String titulo, String contenido, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void guardarUsuarios() {
        try {
            AdministradorArchivos.guardarUsuarios(usuarios);
        } catch (Exception e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    private void volverLogin() {
        // Redirigir a la pantalla de login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/login.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Obtén la ventana (Stage) actual y cámbiala por la nueva escena
            Stage stage = (Stage) btnVolverLogin.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la pantalla de inicio de sesión.", AlertType.ERROR);
            e.printStackTrace();
        }
    }
}