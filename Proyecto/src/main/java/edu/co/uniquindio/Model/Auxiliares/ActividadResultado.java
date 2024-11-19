package edu.co.uniquindio.Model.Auxiliares;

import edu.co.uniquindio.Model.Principales.Actividad;

public class ActividadResultado {
    private String nombreProceso;
    private Actividad actividad;

    private String nombreActividad;
    private int cantidadTareas;
    private boolean esObligatoria;

    // Constructor
    public ActividadResultado(String nombreProceso, Actividad actividad) {
        this.nombreProceso = nombreProceso;
        this.actividad = actividad;
    }

    public ActividadResultado(String nombreActividad, int cantidadTareas, boolean esObligatoria) {
        this.nombreActividad = nombreActividad;
        this.cantidadTareas = cantidadTareas;
        this.esObligatoria = esObligatoria;
    }

    public int getCantidadTareas() {
        return cantidadTareas;
    }

    public void setCantidadTareas(int cantidadTareas) {
        this.cantidadTareas = cantidadTareas;
    }

    public boolean isEsObligatoria() {
        return esObligatoria;
    }

    public void setEsObligatoria(boolean esObligatoria) {
        this.esObligatoria = esObligatoria;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    // Getters y setters
    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
}
