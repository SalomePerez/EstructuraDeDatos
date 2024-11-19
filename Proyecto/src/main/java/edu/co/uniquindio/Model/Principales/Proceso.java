package edu.co.uniquindio.Model.Principales;

import edu.co.uniquindio.Model.Auxiliares.TiempoProceso;
import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;

import java.time.LocalDateTime;
import java.util.UUID;

public class Proceso {
    private TiempoProceso tiempoDuracion;
    private static Proceso instancia;
    private String nombre; // Título del proceso
    private UUID identificador; // Identificador único del proceso
    private ListaEnlazada<Actividad> listaDeActividades; // Lista de Actividads asociadas al proceso
    private LocalDateTime fechaDeInicio; // Fecha de inicio del proyecto

    // Constructor para crear un nuevo proyecto con el título proporcionado
    public Proceso(String nombre) {
        this.identificador = UUID.randomUUID(); // Genera un UUID único para el proyecto
        this.nombre = nombre; // Asigna el título al proyecto
        this.listaDeActividades = new ListaEnlazada<>(); // Inicializa la lista de Actividads vacía
        this.fechaDeInicio = LocalDateTime.now(); // Asigna la fecha actual como fecha de inicio
    }

    public Proceso(TiempoProceso tiempoDuracion) {
        if (tiempoDuracion == null) {
            throw new IllegalArgumentException("El tiempo de duración no puede ser nulo.");
        }
        this.tiempoDuracion = tiempoDuracion;
    }

    public String getNombre() {
        return nombre;
    }

    // Método para agregar una Actividad al proceso
    public void agregarActividad(Actividad actividad) {
        listaDeActividades.insertar(actividad); // Inserta la Actividad en la lista
    }

    // Getter para obtener el identificador del proceso
    public UUID obtenerIdentificador() {
        return identificador;
    }

    // Setter para cambiar el identificador del proceso
    public void establecerIdentificador(UUID identificador) {
        this.identificador = identificador;
    }

    // Getter para obtener el nombre del proceso
    public String obtenerTitulo() {
        return nombre;
    }

    // Setter para cambiar el título del proyecto
    public void establecerTitulo(String titulo) {
        this.nombre = titulo;
    }

    // Getter para obtener la lista de Actividads del proyecto
    public ListaEnlazada<Actividad> obtenerlistaDeActividades() {
        return listaDeActividades;
    }

    // Setter para cambiar la lista de Actividads del proceso
    public void establecerlistaDeActividades(ListaEnlazada<Actividad> listaDeActividades) {
        this.listaDeActividades = listaDeActividades;
    }

    // Getter para obtener la fecha de inicio del proceso
    public LocalDateTime obtenerFechaDeInicio() {
        return fechaDeInicio;
    }

    // Setter para cambiar la fecha de inicio del proceso
    public void establecerFechaDeInicio(LocalDateTime fechaDeInicio) {
        this.fechaDeInicio = fechaDeInicio;
    }

    // Método para obtener el tiempo de duración
    public TiempoProceso obtenerTiempoDuracion() {
        return tiempoDuracion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Proceso: ").append(this.obtenerTitulo()).append("\n");
        sb.append("Actividades:\n");

        for (int i = 0; i < obtenerlistaDeActividades().getTamanio(); i++) {
            Actividad actividad = obtenerlistaDeActividades().getElementoEnPosicion(i);
            sb.append(i + 1).append(". ").append(actividad.obtenerNombre()).append("\n");
            sb.append("   Descripción: ").append(actividad.obtenerDescripcion()).append("\n");
            sb.append("   Obligatoria: ").append(actividad.esObligatoria()).append("\n");

            // Agregar información de conexiones
            if (actividad.obtenerActividadAnterior() != null) {
                sb.append("   Anterior: ").append(actividad.obtenerActividadAnterior().obtenerNombre()).append("\n");
            }
            if (actividad.obtenerActividadSiguiente() != null) {
                sb.append("   Siguiente: ").append(actividad.obtenerActividadSiguiente().obtenerNombre()).append("\n");
            }

            // Agregar información de las tareas
            sb.append("   Tareas:\n");
            Cola<Tarea> tareas = actividad.obtenerTareas();
            if (tareas.estaVacia()) {
                sb.append("      No hay tareas definidas\n");
            } else {
                int numTarea = 1;
                for (Tarea tarea : tareas) {
                    sb.append("      ").append(numTarea).append(". ")
                            .append(tarea.obtenerNombre()).append("\n")
                            .append("         Descripción: ").append(tarea.obtenerDescripcion()).append("\n")
                            .append("         Duración: ").append(tarea.obtenerDuracion()).append(" minutos\n")
                            .append("         Obligatoria: ").append(tarea.esObligatoria()).append("\n")
                            .append("         Estado: ").append(tarea.estaCompletada() ? "Completada" : "Pendiente").append("\n");
                    numTarea++;
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}