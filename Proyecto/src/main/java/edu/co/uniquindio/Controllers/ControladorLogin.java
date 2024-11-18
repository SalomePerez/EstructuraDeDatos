package edu.co.uniquindio.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import edu.co.uniquindio.Model.Administradores.AdministradorArchivos;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Usuario;
import javafx.scene.input.MouseEvent;

public class ControladorLogin {
    @FXML
    private Button bntIngresar;

    @FXML
    private Button bntRegistrar;

    private ListaEnlazada<Usuario> usuarios; // Lista de usuarios
    private Usuario usuarioActual; // Usuario actualmente autenticado

    // Constructor
    public ControladorLogin() {
        usuarios = new ListaEnlazada<>();
        cargarUsuarios();
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

    /**
     * Carga los usuarios desde un archivo.
     */
    private void cargarUsuarios() {
        try {
            usuarios = AdministradorArchivos.cargarUsuarios(); // Cargar usuarios desde archivo
            if (usuarios.getTamanio() == 0) {
                // Crear un usuario por defecto si no hay usuarios
                registrarUsuario("Admin", "1234", "admin@test.com", "admin123");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
    }

    /**
     * Guarda los cambios realizados en la lista de usuarios en un archivo.
     */
    private void guardarCambios() {
        try {
            AdministradorArchivos.guardarUsuarios(usuarios); // Guardar usuarios en archivo
        } catch (Exception e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    public void registrarUsuario(String nombre, String identificacion, String correo, String contrasenia) {
        if (existeUsuario(identificacion)) {
            System.out.println("El usuario con esta identificación ya existe.");
            return;
        }

        Usuario nuevoUsuario = new Usuario(nombre, identificacion, correo, contrasenia);
        usuarios.insertar(nuevoUsuario); // Agregar el nuevo usuario a la lista enlazada
        guardarCambios(); // Guardar los cambios
    }

    /**
     * Verifica si un usuario con la identificación dada ya existe.
     *
     * @param identificacion Identificación a verificar.
     * @return true si el usuario existe, false de lo contrario.
     */
    private boolean existeUsuario(String identificacion) {
        for (int i = 0; i < usuarios.getTamanio(); i++) {
            if (usuarios.getElementoEnPosicion(i).getIdentificacion().equals(identificacion)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Autentica un usuario en el sistema.
     *
     * @param identificacion Identificación del usuario.
     * @param contrasenia    Contraseña del usuario.
     * @return true si la autenticación es exitosa, false de lo contrario.
     */
    public boolean autenticarUsuario(String identificacion, String contrasenia) {
        for (int i = 0; i < usuarios.getTamanio(); i++) {
            if (usuarios.getElementoEnPosicion(i).getIdentificacion().equals(identificacion) && usuarios.getElementoEnPosicion(i).getContrasenia().equals(contrasenia)) {
                usuarioActual = usuarios.getElementoEnPosicion(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el usuario actualmente autenticado.
     *
     * @return El usuario autenticado o null si no hay ningún usuario autenticado.
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

}