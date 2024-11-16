package edu.co.uniquindio.Controlador;

import edu.co.uniquindio.Modelo.Principales.Usuario;
import edu.co.uniquindio.Modelo.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Modelo.Administradores.AdministradorArchivos;

public class ControladorLogin {
    private ListaEnlazada<Usuario> usuarios; // Lista de usuarios
    private Usuario usuarioActual; // Usuario actualmente autenticado

    // Constructor
    public ControladorLogin() {
        usuarios = new ListaEnlazada<>();
        cargarUsuarios();
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
     *
     * @param nombre         Nombre del usuario.
     * @param identificacion Identificación del usuario.
     * @param correo         Correo del usuario.
     * @param contrasenia    Contraseña del usuario.
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
            if (usuarios.get(i).getIdentificacion().equals(identificacion)) {
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
            if (usuarios.get(i).getIdentificacion().equals(identificacion) && usuarios.get(i).getContrasenia().equals(contrasenia)) {
                usuarioActual = usuarios.get(i);
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