package edu.co.uniquindio.Controllers;

import edu.co.uniquindio.Model.Administradores.AdministradorArchivos;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Proceso;

import java.io.IOException;

public class ControladorProceso {

    private ListaEnlazada<Proceso> procesos;
    private Proceso procesoActual;

    public ControladorProceso() {
        procesos = new ListaEnlazada<>();
        cargarProcesos();
    }

    /**
     * Carga los procesos desde el archivo.
     */
    private void cargarProcesos() {
        try {
            procesos = AdministradorArchivos.cargarProcesos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda los cambios en los procesos.
     */
    private void guardarCambios() {
        try {
            AdministradorArchivos.guardarProcesos(procesos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un proceso por su índice.
     *
     * @param index Índice del proceso a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarProceso(int index) {
        if (index >= 0 && index < procesos.getTamanio()) {
            procesos.eliminarEn(index);
            guardarCambios();
            return true;
        }
        return false;
    }

    /**
     * Actualiza la información de un proceso existente.
     */
    public boolean actualizarProceso(int index, String nuevoNombre) {
        if (index >= 0 && index < procesos.getTamanio()) {
            Proceso proceso = procesos.getElementoEnPosicion(index);
            proceso.establecerTitulo(nuevoNombre);
            guardarCambios();
            return true;
        }
        return false;
    }
}
