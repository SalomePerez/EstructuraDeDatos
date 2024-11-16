package edu.co.uniquindio.Modelo.Principales;

import edu.co.uniquindio.Modelo.EstructuraDeDatos.ListaEnlazada;
import java.time.LocalDateTime;

/**
 * Clase que representa una tarea con posibles subtareas.
 */
public class Tarea implements Cloneable {
    private String descripcionTarea;  // Descripción de la tarea
    private boolean esObligatoria;    // Si la tarea es obligatoria
    private int tiempoEstimado;       // Duración en minutos
    private LocalDateTime fechaCreacion; // Fecha y hora de creación
    private ListaEnlazada<Tarea> listaSubtareas; // Lista de subtareas
    private boolean estaCompletada;  // Indica si la tarea está completada

    /**
     * Constructor de la tarea.
     * @param descripcionTarea La descripción de la tarea
     * @param tiempoEstimado El tiempo estimado en minutos
     * @param esObligatoria Indica si la tarea es obligatoria
     */
    public Tarea(String descripcionTarea, int tiempoEstimado, boolean esObligatoria) {
        this.descripcionTarea = descripcionTarea;
        this.tiempoEstimado = tiempoEstimado;
        this.esObligatoria = esObligatoria;
        this.fechaCreacion = LocalDateTime.now();  // Fecha actual
        this.listaSubtareas = new ListaEnlazada<>();
        this.estaCompletada = false;  // Inicialmente no está completada
    }

    /**
     * Constructor de copia para crear una nueva tarea a partir de otra existente.
     * @param otraTarea La tarea a copiar
     */
    public Tarea(Tarea otraTarea) {
        this.descripcionTarea = otraTarea.descripcionTarea;
        this.tiempoEstimado = otraTarea.tiempoEstimado;
        this.esObligatoria = otraTarea.esObligatoria;
        this.fechaCreacion = LocalDateTime.of(
                otraTarea.fechaCreacion.getYear(),
                otraTarea.fechaCreacion.getMonth(),
                otraTarea.fechaCreacion.getDayOfMonth(),
                otraTarea.fechaCreacion.getHour(),
                otraTarea.fechaCreacion.getMinute()
        );
        this.listaSubtareas = new ListaEnlazada<>();
        this.estaCompletada = otraTarea.estaCompletada;
        // Clona las subtareas de la tarea original
        for (int i = 0; i < otraTarea.obtenerSubtareas().getTamanio(); i++) {
            this.listaSubtareas.insertar(new Tarea(otraTarea.obtenerSubtareas().getElementoEnPosicion(i)));
        }
    }

    /**
     * Obtiene la lista de subtareas.
     * @return Lista enlazada con las subtareas
     */
    public ListaEnlazada<Tarea> obtenerSubtareas() {
        ListaEnlazada<Tarea> copia = new ListaEnlazada<>();
        for (int i = 0; i < listaSubtareas.getTamanio(); i++) {
            copia.insertar(listaSubtareas.getElementoEnPosicion(i));
        }
        return copia;
    }

    /**
     * Verifica si la tarea está completada.
     * @return true si la tarea está completada, false en caso contrario
     */
    public boolean estaCompletada() {
        return estaCompletada;
    }

    /**
     * Establece si la tarea está completada.
     * @param estaCompletada El estado de completitud de la tarea
     */
    public void establecerCompletada(boolean estaCompletada) {
        this.estaCompletada = estaCompletada;
    }

    /**
     * Establece la lista de subtareas.
     * @param subtareas Lista de subtareas a establecer
     */
    public void establecerSubtareas(ListaEnlazada<Tarea> subtareas) {
        this.listaSubtareas = new ListaEnlazada<>();
        for (int i = 0; i < subtareas.getTamanio(); i++) {
            this.listaSubtareas.insertar(subtareas.getElementoEnPosicion(i));
        }
    }

    /**
     * Agrega una nueva subtarea a la tarea.
     * @param subtarea La subtarea a agregar
     */
    public void agregarSubtarea(Tarea subtarea) {
        this.listaSubtareas.insertar(new Tarea(subtarea));
    }

    /**
     * Elimina una subtarea en base a su índice.
     * @param index El índice de la subtarea a eliminar
     */
    public void eliminarSubtarea(int index) {
        if (index >= 0 && index < listaSubtareas.getTamanio()) {
            listaSubtareas.eliminarEn(index);
        }
    }

    /**
     * Método para clonar la tarea, incluyendo sus subtareas.
     * @return Una copia profunda de la tarea
     */
    @Override
    public Tarea clone() {
        try {
            Tarea clonada = (Tarea) super.clone();
            clonada.fechaCreacion = LocalDateTime.of(
                    this.fechaCreacion.getYear(),
                    this.fechaCreacion.getMonth(),
                    this.fechaCreacion.getDayOfMonth(),
                    this.fechaCreacion.getHour(),
                    this.fechaCreacion.getMinute()
            );
            clonada.listaSubtareas = new ListaEnlazada<>();
            clonada.estaCompletada = this.estaCompletada;
            // Clona las subtareas de la tarea original
            for (int i = 0; i < this.listaSubtareas.getTamanio(); i++) {
                clonada.listaSubtareas.insertar(this.listaSubtareas.getElementoEnPosicion(i).clone());
            }
            return clonada;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar la tarea", e);
        }
    }

    /**
     * Obtiene la fecha de creación de la tarea.
     * @return La fecha de creación
     */
    public LocalDateTime obtenerFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha de creación de la tarea.
     * @param fechaCreacion La nueva fecha de creación
     */
    public void establecerFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene la descripción de la tarea.
     * @return La descripción de la tarea
     */
    public String obtenerDescripcion() {
        return descripcionTarea;
    }

    /**
     * Obtiene la duración estimada de la tarea.
     * @return La duración en minutos
     */
    public int obtenerDuracion() {
        return tiempoEstimado;
    }

    /**
     * Verifica si la tarea es obligatoria.
     * @return true si la tarea es obligatoria, false en caso contrario
     */
    public boolean esObligatoria() {
        return esObligatoria;
    }

    /**
     * Método para mostrar los detalles de la tarea.
     * @return Una representación en texto de la tarea
     */
    @Override
    public String toString() {
        return "Tarea{" +
                "descripcion='" + descripcionTarea + '\'' +
                ", obligatoria=" + esObligatoria +
                ", duracion=" + tiempoEstimado +
                ", fechaCreacion=" + fechaCreacion +
                ", subtareas=" + listaSubtareas.getTamanio() +
                ", finalizada=" + estaCompletada +
                '}';
    }

    /**
     * Obtiene el nombre (descripción) de la tarea.
     * @return El nombre de la tarea
     */
    public String obtenerNombre() {
        return descripcionTarea;
    }
}