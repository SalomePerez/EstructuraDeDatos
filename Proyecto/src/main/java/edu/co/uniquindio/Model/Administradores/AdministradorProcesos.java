package edu.co.uniquindio.Model.Administradores;

import edu.co.uniquindio.Controllers.NotificacionControlador;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;

import java.util.UUID;

/**
 * Clase que administra los procesos del sistema, permitiendo su creación,
 * búsqueda, modificación y eliminación.
 * Esta clase implementa la gestión centralizada de procesos, incluyendo:
 * - Manejo de la lista de procesos
 * - Gestión de notificaciones relacionadas con los procesos
 * - Administración de actividades dentro de los procesos
 */
public class AdministradorProcesos {
    private ListaEnlazada<Proceso> listaProcesos;
    private final NotificacionControlador notificacionControlador;

    /**
     * Constructor que inicializa un nuevo administrador de procesos.
     * Configura la lista de procesos vacía y los componentes de notificación.
     */
    public AdministradorProcesos() {
        this.listaProcesos = new ListaEnlazada<>();
        this.notificacionControlador = NotificacionControlador.getInstance();

    }

    /**
     * Registra un nuevo proceso en el sistema con el nombre especificado.
     *
     * @param nombreProceso El nombre que se asignará al nuevo proceso
     * @return El proceso creado y registrado
     * @throws IllegalArgumentException si el nombre es null o está vacío
     */
    public Proceso registrarNuevoProceso(String nombreProceso) {
        if (nombreProceso == null || nombreProceso.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proceso no puede ser null o vacío");
        }
        Proceso nuevoProceso = new Proceso(nombreProceso);
        listaProcesos.insertar(nuevoProceso);
        notificacionControlador.alertarInicioProceso(nuevoProceso);
        return nuevoProceso;
    }

    /**
     * Registra una nueva actividad en un proceso específico.
     *
     * @param idProceso El identificador UUID del proceso
     * @param nuevaActividad La actividad a registrar en el proceso
     * @throws IllegalArgumentException si el idProceso o la actividad son null
     */
    public void registrarActividadEnProceso(UUID idProceso, Actividad nuevaActividad) {
        if (idProceso == null || nuevaActividad == null) {
            throw new IllegalArgumentException("El idProceso y la actividad no pueden ser null");
        }
        Proceso procesoEncontrado = buscarProcesoPorId(idProceso);
        if (procesoEncontrado != null) {
            procesoEncontrado.agregarActividad(nuevaActividad);
        }
    }

    /**
     * Localiza un proceso específico por su identificador único.
     *
     * @param idProceso El identificador UUID del proceso a buscar
     * @return El proceso encontrado o null si no existe
     */
    public Proceso buscarProcesoPorId(UUID idProceso) {
        if (idProceso == null) {
            return null;
        }
        for (int i = 0; i < listaProcesos.getTamanio(); i++) {
            Proceso procesoActual = listaProcesos.getElementoEnPosicion(i);
            if (procesoActual.obtenerIdentificador().equals(idProceso)) {
                return procesoActual;
            }
        }
        return null;
    }

    /**
     * Obtiene la lista completa de todos los procesos registrados.
     *
     * @return ListaEnlazada conteniendo todos los procesos del sistema
     */
    public ListaEnlazada<Proceso> obtenerTodosLosProcesos() {
        return listaProcesos;
    }

    /**
     * Actualiza la lista completa de procesos con una nueva lista.
     *
     * @param nuevaListaProcesos Nueva lista de procesos a establecer
     * @throws IllegalArgumentException si la lista de procesos es null
     */
    public void actualizarListaProcesos(ListaEnlazada<Proceso> nuevaListaProcesos) {
        if (nuevaListaProcesos == null) {
            throw new IllegalArgumentException("La lista de procesos no puede ser null");
        }
        this.listaProcesos = nuevaListaProcesos;
    }

    /**
     * Elimina un proceso específico del sistema por su identificador único.
     *
     * @param idProceso El identificador UUID del proceso a eliminar
     * @throws IllegalArgumentException si el idProceso es null
     */
    public void eliminarProcesoPorId(UUID idProceso) {
        if (idProceso == null) {
            throw new IllegalArgumentException("El idProceso no puede ser null");
        }
        Proceso procesoParaEliminar = buscarProcesoPorId(idProceso);
        if (procesoParaEliminar != null) {
            listaProcesos.eliminar(procesoParaEliminar);
        }
    }
}
