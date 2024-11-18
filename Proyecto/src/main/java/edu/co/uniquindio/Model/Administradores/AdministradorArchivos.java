package edu.co.uniquindio.Model.Administradores;

import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Usuario;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AdministradorArchivos {
    private static final Path DIRECTORIO_DATOS = Paths.get("datos");
    private static final Path RUTA_USUARIOS = DIRECTORIO_DATOS.resolve("usuarios.dat");
    private static final Path RUTA_PROCESOS = DIRECTORIO_DATOS.resolve("procesos.dat");

    // Inicialización estática para crear directorio
    static {
        try {
            Files.createDirectories(DIRECTORIO_DATOS);
        } catch (IOException e) {
            System.err.println("Error al crear directorio de datos: " + e.getMessage());
        }
    }

    /**
     * Guarda la lista de usuarios en un archivo.
     *
     * @param usuarios Lista de usuarios a guardar
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public static void guardarUsuarios(ListaEnlazada<Usuario> usuarios) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(RUTA_USUARIOS))) {
            Usuario[] arrayUsuarios = new Usuario[usuarios.getTamanio()];
            for (int i = 0; i < usuarios.getTamanio(); i++) {
                arrayUsuarios[i] = usuarios.getElementoEnPosicion(i);
            }
            oos.writeObject(arrayUsuarios);
        }
    }

    /**
     * Carga la lista de usuarios desde un archivo.
     *
     * @return Lista enlazada con los usuarios cargados
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public static ListaEnlazada<Usuario> cargarUsuarios() throws IOException {
        ListaEnlazada<Usuario> usuarios = new ListaEnlazada<>();

        if (Files.exists(RUTA_USUARIOS)) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    Files.newInputStream(RUTA_USUARIOS))) {
                Usuario[] arrayUsuarios = (Usuario[]) ois.readObject();
                for (Usuario usuario : arrayUsuarios) {
                    usuarios.insertar(usuario);
                }
            } catch (ClassNotFoundException e) {
                throw new IOException("Error al deserializar usuarios", e);
            }
        }
        return usuarios;
    }

    /**
     * Guarda la lista de procesos en un archivo.
     *
     * @param procesos Lista de procesos a guardar
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public static void guardarProcesos(ListaEnlazada<Proceso> procesos) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(RUTA_PROCESOS))) {
            Proceso[] arrayProcesos = new Proceso[procesos.getTamanio()];
            for (int i = 0; i < procesos.getTamanio(); i++) {
                arrayProcesos[i] = procesos.getElementoEnPosicion(i);
            }
            oos.writeObject(arrayProcesos);
        }
    }

    /**
     * Carga la lista de procesos desde un archivo.
     *
     * @return Lista enlazada con los procesos cargados
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public static ListaEnlazada<Proceso> cargarProcesos() throws IOException {
        ListaEnlazada<Proceso> procesos = new ListaEnlazada<>();

        if (Files.exists(RUTA_PROCESOS)) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    Files.newInputStream(RUTA_PROCESOS))) {
                Proceso[] arrayProcesos = (Proceso[]) ois.readObject();
                for (Proceso proceso : arrayProcesos) {
                    procesos.insertar(proceso);
                }
            } catch (ClassNotFoundException e) {
                throw new IOException("Error al deserializar procesos", e);
            }
        }
        return procesos;
    }
}