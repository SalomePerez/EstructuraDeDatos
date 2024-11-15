package edu.co.uniquindio.Modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una tarea con posibles subtareas.
 */
public class Tarea implements Cloneable {
    private String descripcionTarea;  // Descripción de la tarea
    private boolean esObligatoria;    // Si la tarea es obligatoria
    private int tiempoEstimado;       // Duración en minutos
    private LocalDateTime fechaCreacion; // Fecha y hora de creación
    private List<Tarea> listaSubtareas; // Lista de subtareas
    private boolean estaCompletada;  // Indica si la tarea está completada

    /**
     * Constructor de la tarea.
     */
    public Tarea(String descripcionTarea, int tiempoEstimado, boolean esObligatoria) {
        this.descripcionTarea = descripcionTarea;
        this.tiempoEstimado = tiempoEstimado;
        this.esObligatoria = esObligatoria;
        this.fechaCreacion = LocalDateTime.now();  // Fecha actual
        this.listaSubtareas = new ArrayList<>();
        this.estaCompletada = false;  // Inicialmente no está completada
    }

    /**
     * Constructor de copia para crear una nueva tarea a partir de otra existente.
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
        this.listaSubtareas = new ArrayList<>();
        this.estaCompletada = otraTarea.estaCompletada;
        // Clona las subtareas de la tarea original
        for (Tarea subtarea : otraTarea.obtenerSubtareas()) {
            this.listaSubtareas.add(new Tarea(subtarea));
        }
    }

    /**
     * Obtiene una copia de la lista de subtareas.
     */
    public List<Tarea> obtenerSubtareas() {
        return new ArrayList<>(listaSubtareas); // Retorna una copia de la lista de subtareas
    }

    /**
     * Verifica si la tarea está completada.
     */
    public boolean estaCompletada() {
        return estaCompletada;
    }

    /**
     * Establece si la tarea está completada.
     */
    public void establecerCompletada(boolean estaCompletada) {
        this.estaCompletada = estaCompletada;
    }

    /**
     * Establece la lista de subtareas.
     */
    public void establecerSubtareas(List<Tarea> subtareas) {
        this.listaSubtareas = new ArrayList<>(subtareas); // Crea una copia de la lista de subtareas
    }

    /**
     * Agrega una nueva subtarea a la tarea.
     */
    public void agregarSubtarea(Tarea subtarea) {
        this.listaSubtareas.add(new Tarea(subtarea)); // Agrega una copia de la subtarea
    }

    /**
     * Elimina una subtarea en base a su índice.
     */
    public void eliminarSubtarea(int index) {
        if (index >= 0 && index < listaSubtareas.size()) {
            listaSubtareas.remove(index);
        }
    }

    /**
     * Método para clonar la tarea, incluyendo sus subtareas.
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
            clonada.listaSubtareas = new ArrayList<>();
            clonada.estaCompletada = this.estaCompletada;
            // Clona las subtareas de la tarea original
            for (Tarea subtarea : this.listaSubtareas) {
                clonada.listaSubtareas.add(subtarea.clone());
            }
            return clonada;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar la tarea", e);
        }
    }

    // Métodos para obtener la información de la tarea
    public LocalDateTime obtenerFechaCreacion() {
        return fechaCreacion;
    }

    public void establecerFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String obtenerDescripcion() {
        return descripcionTarea;
    }

    public int obtenerDuracion() {
        return tiempoEstimado;
    }

    public boolean esObligatoria() {
        return esObligatoria;
    }

    /**
     * Método para mostrar los detalles de la tarea.
     */
    @Override
    public String toString() {
        return "Tarea{" +
                "descripcion='" + descripcionTarea + '\'' +
                ", obligatoria=" + esObligatoria +
                ", duracion=" + tiempoEstimado +
                ", fechaCreacion=" + fechaCreacion +
                ", subtareas=" + listaSubtareas.size() +
                ", finalizada=" + estaCompletada +
                '}';
    }

    // Nombre de la tarea
    public String obtenerNombre() {
        return descripcionTarea;
    }
}