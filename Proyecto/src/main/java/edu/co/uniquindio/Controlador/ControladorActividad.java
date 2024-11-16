package edu.co.uniquindio.Controlador;

import edu.co.uniquindio.Modelo.Principales.Actividad;
import edu.co.uniquindio.Modelo.Principales.Proceso;
import edu.co.uniquindio.Modelo.Principales.Tarea;

import java.time.LocalDateTime;

public class ControladorActividad {

    // Constructor para inicializar el controlador con un proceso y actividad actual
    public ControladorActividad(Proceso proceso, Actividad actividad) {
    }

    /**
     * Agrega una tarea a una actividad específica.
     *
     * @param actividad      La actividad a la que se le agregará la tarea
     * @param descripcion    La descripción de la tarea
     * @param tiempoEstimado El tiempo estimado para la tarea
     * @param obligatoria    Indica si la tarea es obligatoria
     */
    public void agregarTareaAActividad(Actividad actividad, String descripcion,
                                       int tiempoEstimado, boolean obligatoria) {
        // Crear una nueva tarea con los parámetros proporcionados
        Tarea nuevaTarea = new Tarea(descripcion, tiempoEstimado, obligatoria);
        // Agregar la tarea a la cola de tareas de la actividad
        actividad.agregarTarea(nuevaTarea);
    }

    /**
     * Marca una actividad como completada.
     *
     * @param actividad La actividad a marcar como completada
     */
    public void completarActividad(Actividad actividad) {
        // Aquí podríamos implementar la lógica para marcar la actividad como completada,
        // como actualizar el estado de la actividad o realizar alguna acción relacionada.

        // Este es un ejemplo donde se podría establecer la fecha de finalización de la actividad
        actividad.asignarFechaInicio(LocalDateTime.now()); // Ejemplo de asignación de fecha de finalización
        // Debería actualizar el estado de las tareas también si fuera necesario.
    }

    /**
     * Asigna una actividad anterior en una secuencia de actividades.
     *
     * @param actividad         La actividad para la que se asignará la actividad anterior
     * @param actividadAnterior La actividad anterior a asignar
     */
    public void establecerActividadAnterior(Actividad actividad, Actividad actividadAnterior) {
        // Establece la actividad anterior de la actividad actual
        actividad.establecerActividadAnterior(actividadAnterior);
    }

    /**
     * Asigna una actividad siguiente en una secuencia de actividades.
     *
     * @param actividad          La actividad para la que se asignará la actividad siguiente
     * @param actividadSiguiente La actividad siguiente a asignar
     */
    public void establecerActividadSiguiente(Actividad actividad, Actividad actividadSiguiente) {
        // Establece la actividad siguiente de la actividad actual
        actividad.establecerActividadSiguiente(actividadSiguiente);
    }

    /**
     * Obtiene el estado actual de una actividad.
     *
     * @param actividad La actividad cuyo estado se desea obtener
     * @return Un String que representa el estado de la actividad
     */
    public String obtenerEstadoActividad(Actividad actividad) {
        // Implementar la lógica para obtener el estado de la actividad
        // Por ejemplo, podríamos retornar si la actividad está completada o pendiente
        return actividad.obtenerFechaInicio() != null ? "Completada" : "Pendiente";
    }

    /**
     * Modifica la descripción de una actividad existente.
     *
     * @param actividad   La actividad cuya descripción se desea modificar
     * @param descripcion El nuevo texto para la descripción
     */
    public void modificarDescripcionActividad(Actividad actividad, String descripcion) {
        // Cambia la descripción de la actividad
        actividad.establecerDescripcion(descripcion);
    }

    /**
     * Modifica el nombre de una actividad existente.
     *
     * @param actividad La actividad cuyo nombre se desea modificar
     * @param nombre    El nuevo nombre para la actividad
     */
    public void modificarNombreActividad(Actividad actividad, String nombre) {
        // Cambia el nombre de la actividad
        actividad.establecerNombre(nombre);
    }

    /**
     * Establece si una actividad es obligatoria o no.
     *
     * @param actividad   La actividad que se modificará
     * @param obligatoria Si la actividad es obligatoria o no
     */
    public void establecerObligatoriaActividad(Actividad actividad, boolean obligatoria) {
        // Cambia el estado de obligatoriedad de la actividad
        actividad.establecerObligatoria(obligatoria);
    }
}