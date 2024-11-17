package edu.co.uniquindio.Modelo.Administradores;

import edu.co.uniquindio.Modelo.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Modelo.Principales.Proceso;
import edu.co.uniquindio.Modelo.Principales.Usuario;

import java.io.*;

public class AdministradorArchivos {
    private static final String RUTA_USUARIOS = "datos/usuarios.dat";
    private static final String RUTA_PROCESOS = "datos/procesos.dat";

    /**
     * Guarda la lista de usuarios en un archivo.
     *
     * @param usuarios Lista de usuarios a guardar
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public static void guardarUsuarios(ListaEnlazada<Usuario> usuarios) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(RUTA_USUARIOS))) {
            // Convertir la lista enlazada a array para serialización
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
        File archivo = new File(RUTA_USUARIOS);

        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(archivo))) {
                Usuario[] arrayUsuarios = (Usuario[]) ois.readObject();
                for (Usuario usuario : arrayUsuarios) {
                    usuarios.insertar(usuario);
                }
            } catch (ClassNotFoundException e) {
                throw new IOException("Error al cargar usuarios", e);
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
                new FileOutputStream(RUTA_PROCESOS))) {
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
        File archivo = new File(RUTA_PROCESOS);

        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(archivo))) {
                Proceso[] arrayProcesos = (Proceso[]) ois.readObject();
                for (Proceso proceso : arrayProcesos) {
                    procesos.insertar(proceso);
                }
            } catch (ClassNotFoundException e) {
                throw new IOException("Error al cargar procesos", e);
            }
        }
        return procesos;
    }
}
