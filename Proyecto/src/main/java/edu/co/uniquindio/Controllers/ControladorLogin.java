package edu.co.uniquindio.Controllers;

import edu.co.uniquindio.Model.Auxiliares.Persistencia;
import edu.co.uniquindio.Model.EstructuraDeDatos.Nodo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import edu.co.uniquindio.Model.Administradores.AdministradorArchivos;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Usuario;

import java.io.IOException;
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
        usuarios = Persistencia.cargarUsuarios(); // Cargar usuarios desde la persistencia
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
        String correo = textUsuario.getText().trim(); // Usar el correo
        System.out.println(correo);
        String contrasenia = textContrasenia.getText().trim();
        System.out.println(contrasenia);

        if (correo.isEmpty() || contrasenia.isEmpty() ||
                correo.equals("Usuario") || contrasenia.equals("Contraseña")) {
            mostrarAlerta("Error", "Por favor complete todos los campos.", AlertType.ERROR);
            return;
        }

        // Verificar autenticación
        if (autenticarUsuario(correo, contrasenia)) {
            mostrarAlerta("Éxito", "Bienvenido " + usuarioActual.getNombre(), AlertType.INFORMATION);
            abrirVentanaDashboard();
        } else {
            mostrarAlerta("Error", "Correo o contraseña incorrectos.", AlertType.ERROR);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/co/uniquindio/Application/vista/Registro.fxml"));
            Scene registroScene = new Scene(loader.load());

            // Obtener la ventana (Stage) actual y cambiar la escena
            Stage stage = (Stage) bntRegistrar.getScene().getWindow();
            stage.setScene(registroScene);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de registro.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void abrirVentanaDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/co/uniquindio/Application/vista/Principal.fxml"));
            Scene registroScene = new Scene(loader.load());

            // Obtener la ventana (Stage) actual y cambiar la escena
            Stage stage = (Stage) bntRegistrar.getScene().getWindow();
            stage.setScene(registroScene);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de registro.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void abrirVentanaRecuperacion() {
        // Mostrar mensaje indicando que se enviará un código
        mostrarAlerta("Recuperación", "Se enviará un código de recuperación a su correo.", AlertType.INFORMATION);

        try {
            // Intentar cargar la ventana de recuperación
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/co/uniquindio/vista/recuperacion.fxml"));
            Scene recuperacionScene = new Scene(loader.load());

            // Obtener la ventana (Stage) actual y cambiar la escena
            Stage stage = (Stage) textRecuperar.getScene().getWindow();
            stage.setScene(recuperacionScene);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de recuperación.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void guardarPreferenciasUsuario() {
        // Implementar la lógica para guardar las preferencias del usuario
        // Por ejemplo, usando Properties o un archivo de configuración
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
        // Asegúrate de que los usuarios ya están cargados
        ListaEnlazada<Usuario> usuarios = Persistencia.cargarUsuarios();

        Nodo<Usuario> nodoActual = usuarios.getCabeza(); // Obtener el primer nodo de la lista
        while (nodoActual != null) { // Mientras haya un nodo
            if (nodoActual.getDato().getIdentificacion().equals(identificacion)) {
                return true; // Si encontramos el usuario con la identificacion, retornamos true
            }
            nodoActual = nodoActual.getSiguiente(); // Avanzamos al siguiente nodo
        }
        return false; // Si no encontramos el usuario, retornamos false
    }
    public boolean autenticarUsuario(String correo, String contrasenia) {
        if (usuarios == null || usuarios.getTamanio() == 0) {
            System.out.println("Paso");
            return false; // Si no hay usuarios cargados, no se puede autenticar
        }

        Nodo<Usuario> nodoActual = usuarios.getCabeza(); // Iniciar desde el primer nodo
        while (nodoActual != null) {
            Usuario usuario = nodoActual.getDato();
            System.out.println(usuario.getCorreo());
            System.out.println(usuario.getContrasenia());
            // Verificar el correo y la contraseña
            if (usuario.getCorreo().equalsIgnoreCase(correo) &&
                    usuario.getContrasenia().equals(contrasenia)) {
                usuarioActual = usuario;
                return true; // Autenticación exitosa
            }
            nodoActual = nodoActual.getSiguiente(); // Avanzar al siguiente nodo
        }

        return false; // Si no se encontró ningún usuario coincidente
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}