package edu.co.uniquindio.Pruebas;

import edu.co.uniquindio.Controllers.ControladorLogin;
import edu.co.uniquindio.Model.Principales.Usuario;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/*
Requisito funcional 8: Gestion de usuarios
 */
public class GestionDeUsuariosTest {
    private ControladorLogin controladorLogin;

    @Before
    public void setUp() {
        // Crear una nueva instancia de ControladorLogin antes de cada prueba
        controladorLogin = new ControladorLogin();
    }

    // Prueba para registrar un usuario
    @Test
    public void testRegistrarUsuario() {
        controladorLogin.registrarUsuario("Nelson", "12345", "nelson@test.com", "password123");

        // Verificar que el usuario fue registrado correctamente
        Usuario usuario = controladorLogin.getUsuarioActual();
        assertNotNull(usuario);
        assertEquals("Nelson", usuario.getNombre());
        assertEquals("12345", usuario.getIdentificacion());
        assertEquals("nelson@test.com", usuario.getCorreo());
    }




    @Test
    public void testAutenticacionCorrecta() {
        // Registra un usuario primero
        controladorLogin.registrarUsuario("Carlos", "67890", "carlos@test.com", "password123");

        // Autentica al usuario con credenciales correctas
        boolean autenticado = controladorLogin.autenticarUsuario("67890", "password123");

        // Verifica que la autenticación fue exitosa
        assertTrue(autenticado);
        assertNotNull(controladorLogin.getUsuarioActual());
        assertEquals("Carlos", controladorLogin.getUsuarioActual().getNombre());
    }

    @Test
    public void testAutenticacionIncorrecta() {
        // Registra un usuario primero
        controladorLogin.registrarUsuario("Ana", "54321", "ana@test.com", "password123");

        // Intenta autenticar con credenciales incorrectas
        boolean autenticado = controladorLogin.autenticarUsuario("54321", "wrongpassword");

        // Verifica que la autenticación falló
        assertFalse(autenticado);
        assertNull(controladorLogin.getUsuarioActual());
    }

    @Test
    public void testRegistroUsuarioDuplicado() {
        // Registra un usuario
        controladorLogin.registrarUsuario("Juan", "11111", "juan@test.com", "password123");

        // Intenta registrar un usuario con la misma identificación
        controladorLogin.registrarUsuario("Juan", "11111", "juan2@test.com", "password456");

        // Verifica que no se haya agregado un usuario duplicado
        Usuario usuarioActual = controladorLogin.getUsuarioActual();
        assertNotNull(usuarioActual);
        assertEquals("Juan", usuarioActual.getNombre());
        assertEquals("11111", usuarioActual.getIdentificacion());
        assertNotEquals("juan2@test.com", usuarioActual.getCorreo()); // El correo debe ser el original
    }

    // Prueba para verificar la carga de un usuario por defecto
    @Test
    public void testCargaUsuariosPorDefecto() {
        controladorLogin = new ControladorLogin();  // Se crea una nueva instancia para probar la carga por defecto
        Usuario usuario = controladorLogin.getUsuarioActual();

        // Verifica que el usuario por defecto fue creado
        assertNotNull(usuario);
        assertEquals("Admin", usuario.getNombre());
        assertEquals("1234", usuario.getIdentificacion());
    }


}

