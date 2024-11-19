package edu.co.uniquindio.Model.Administradores;

import edu.co.uniquindio.Controllers.NotificacionControlador;
import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.Notificacion.PrioridadNotificaciones;
import edu.co.uniquindio.Model.Notificacion.TipoNotificacion;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Tarea;

import java.util.UUID;

public class AdministradorTareas {
    private final NotificacionControlador notificacionControlador;
    private final AdministradorProcesos administradorProcesos;
    private final AdministradorNotificaciones administradorNotificaciones;

    public AdministradorTareas(AdministradorProcesos administradorProcesos) {
        this.administradorProcesos = administradorProcesos;
        this.administradorNotificaciones = AdministradorNotificaciones.getInstance();
        this.notificacionControlador = NotificacionControlador.getInstance();
    }

    /**
     * Registra una nueva tarea al final de la cola de tareas de una actividad.
     */
    public void registrarTarea(UUID procesoId, String nombreActividad, String descripcion,
                               int duracion, boolean obligatoria) {
        try {
            Actividad actividad = buscarActividad(procesoId, nombreActividad);
            Tarea nuevaTarea = new Tarea(descripcion, duracion, obligatoria);

            if (validarReglasTarea(actividad.obtenerTareas(), nuevaTarea)) {
                actividad.agregarTarea(nuevaTarea);
                notificacionControlador.alertarNuevaTarea(nuevaTarea, actividad,
                        administradorProcesos.buscarProcesoPorId(procesoId));
            } else {
                throw new IllegalStateException("No se pueden tener dos tareas opcionales consecutivas.");
            }
        } catch (Exception e) {
            notificarError("Error en Registro de Tarea", e.getMessage(), procesoId);
            throw e;
        }
    }

    /**
     * Inserta una tarea en una posición específica de la cola de tareas.
     */
    public void insertarTareaEnPosicion(UUID procesoId, String nombreActividad, int posicion,
                                        String descripcion, int duracion, boolean obligatoria) {
        try {
            validarPosicion(posicion);
            Actividad actividad = buscarActividad(procesoId, nombreActividad);
            Tarea nuevaTarea = new Tarea(descripcion, duracion, obligatoria);

            if (validarReglasTarea(actividad.obtenerTareas(), nuevaTarea)) {
                insertarTareaEnCola(actividad.obtenerTareas(), nuevaTarea, posicion);
                notificacionControlador.alertarNuevaTarea(nuevaTarea, actividad,
                        administradorProcesos.buscarProcesoPorId(procesoId));
            } else {
                throw new IllegalStateException("No se pueden tener dos tareas opcionales consecutivas.");
            }
        } catch (Exception e) {
            notificarError("Error en Inserción de Tarea", e.getMessage(), procesoId);
            throw e;
        }
    }

    /**
     * Busca una actividad en un proceso específico.
     */
    private Actividad buscarActividad(UUID procesoId, String nombreActividad) {
        Proceso proceso = administradorProcesos.buscarProcesoPorId(procesoId);
        if (proceso == null) {
            throw new IllegalStateException("Proceso no encontrado.");
        }

        administradorNotificaciones.agregarProcesoMonitoreo(proceso);

        for (int i = 0; i < proceso.obtenerlistaDeActividades().getTamanio(); i++) {
            Actividad actividad = proceso.obtenerlistaDeActividades().getElementoEnPosicion(i);
            if (actividad.obtenerNombre().equals(nombreActividad)) {
                return actividad;
            }
        }
        throw new IllegalStateException("Actividad no encontrada.");
    }

    /**
     * Valida las reglas de negocio para la inserción de tareas.
     */
    private boolean validarReglasTarea(Cola<Tarea> tareas, Tarea nuevaTarea) {
        if (tareas.estaVacia() || nuevaTarea.esObligatoria()) {
            return true;
        }

        // Si la tarea es opcional, verificamos que no haya otras tareas opcionales adyacentes
        Cola<Tarea> colaTemp = new Cola<>();
        Tarea tareaAnterior = null;
        boolean esValido = true;

        while (!tareas.estaVacia()) {
            Tarea actual = tareas.desencolar();
            if (!actual.esObligatoria() && tareaAnterior != null && !tareaAnterior.esObligatoria()) {
                esValido = false;
            }
            colaTemp.encolar(actual);
            tareaAnterior = actual;
        }

        // Restaurar la cola original
        while (!colaTemp.estaVacia()) {
            tareas.encolar(colaTemp.desencolar());
        }

        return esValido;
    }

    /**
     * Inserta una tarea en una posición específica de la cola.
     */
    private void insertarTareaEnCola(Cola<Tarea> tareas, Tarea nuevaTarea, int posicion) {
        Cola<Tarea> colaTemp = new Cola<>();
        int contador = 0;

        // Mover elementos hasta la posición deseada
        while (!tareas.estaVacia() && contador < posicion) {
            colaTemp.encolar(tareas.desencolar());
            contador++;
        }

        // Insertar la nueva tarea
        colaTemp.encolar(nuevaTarea);

        // Restaurar el resto de elementos
        while (!tareas.estaVacia()) {
            colaTemp.encolar(tareas.desencolar());
        }

        // Reconstruir la cola original
        while (!colaTemp.estaVacia()) {
            tareas.encolar(colaTemp.desencolar());
        }
    }

    private void validarPosicion(int posicion) {
        if (posicion < 0) {
            throw new IllegalStateException("La posición no puede ser negativa.");
        }
    }

    private void notificarError(String titulo, String mensaje, UUID procesoId) {
        notificacionControlador.procesarNotificacion(
                titulo,
                mensaje,
                TipoNotificacion.ERROR,
                PrioridadNotificaciones.ALTA,
                procesoId.toString()
        );
    }

    /**
     * Busca una tarea en la cola según su nombre.
     *
     * @param tareas La cola de tareas donde buscar.
     * @param nombre El nombre de la tarea a buscar.
     * @return La tarea encontrada, o null si no se encuentra.
     */
    public Tarea buscarTareaPorNombre(Cola<Tarea> tareas, String nombre) {
        if (tareas == null || nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("La cola de tareas o el nombre de la tarea no pueden ser nulos o vacíos.");
        }

        for (Tarea tarea : tareas) {
            if (tarea.obtenerDescripcion().equals(nombre)) {
                return tarea;
            }
        }

        return null; // Si no se encuentra, devuelve null
    }

    /**
     * Busca una tarea en la cola de tareas asociadas a una actividad.
     *
     * @param tareas La cola de tareas donde buscar.
     * @param nombreActividad El nombre de la actividad asociada a la tarea.
     * @return La primera tarea asociada a la actividad encontrada, o null si no se encuentra.
     */
    public Tarea buscarTareaDesdeActividad(Cola<Tarea> tareas, String nombreActividad) {
        if (tareas == null || nombreActividad == null || nombreActividad.isEmpty()) {
            throw new IllegalArgumentException("La cola de tareas o el nombre de la actividad no pueden ser nulos o vacíos.");
        }

        for (Tarea tarea : tareas) {
            // Asume que el nombre de la actividad está incluido como parte de la descripción o se asocia de alguna forma
            if (tarea.obtenerDescripcion().contains(nombreActividad)) {
                return tarea;
            }
        }

        return null; // Si no se encuentra, devuelve null
    }

}