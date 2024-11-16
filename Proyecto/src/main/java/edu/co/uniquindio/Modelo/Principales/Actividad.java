package edu.co.uniquindio.Modelo.Principales;

import edu.co.uniquindio.Modelo.EstructuraDeDatos.Cola;

import java.time.LocalDateTime;

public class Actividad {
    private String nombre;
    private String descripcion;
    private boolean obligatoria;
    private Cola<Tarea> tareas;
    private Actividad anterior;
    private Actividad siguiente;
    private LocalDateTime fechaInicio;

    // Constructor para inicializar una actividad
    public Actividad(String nombre, String descripcion, boolean obligatoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.obligatoria = obligatoria;
        this.tareas = new Cola<>(); // Crea una nueva cola de tareas vacía
        this.fechaInicio = LocalDateTime.now(); // Establece la fecha de inicio actual
    }

    // Establecer la fecha de inicio de la actividad
    public void asignarFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    // Agregar una tarea a la cola de tareas de la actividad
    public void agregarTarea(Tarea tarea) {
        tareas.encolar(tarea); // Encola una nueva tarea
    }

    // Obtener la fecha de inicio de la actividad
    public LocalDateTime obtenerFechaInicio() {
        return fechaInicio;
    }

    // Obtener la actividad anterior en la secuencia
    public Actividad obtenerActividadAnterior() {
        return anterior;
    }

    // Establecer la actividad anterior en la secuencia
    public void establecerActividadAnterior(Actividad anterior) {
        this.anterior = anterior;
    }

    // Obtener la descripción de la actividad
    public String obtenerDescripcion() {
        return descripcion;
    }

    // Establecer la descripción de la actividad
    public void establecerDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Obtener el nombre de la actividad
    public String obtenerNombre() {
        return nombre;
    }

    // Establecer el nombre de la actividad
    public void establecerNombre(String nombre) {
        this.nombre = nombre;
    }

    // Verificar si la actividad es obligatoria
    public boolean esObligatoria() {
        return obligatoria;
    }

    // Establecer si la actividad es obligatoria
    public void establecerObligatoria(boolean obligatoria) {
        this.obligatoria = obligatoria;
    }

    // Obtener la siguiente actividad en la secuencia
    public Actividad obtenerActividadSiguiente() {
        return siguiente;
    }

    // Establecer la siguiente actividad en la secuencia
    public void establecerActividadSiguiente(Actividad siguiente) {
        this.siguiente = siguiente;
    }

    // Obtener la cola de tareas asociada a la actividad
    public Cola<Tarea> obtenerTareas() {
        return tareas;
    }

    // Establecer la cola de tareas asociada a la actividad
    public void establecerTareas(Cola<Tarea> tareas) {
        this.tareas = tareas;
    }
}