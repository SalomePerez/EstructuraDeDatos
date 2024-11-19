package edu.co.uniquindio.Model.Auxiliares;

import edu.co.uniquindio.Model.Principales.Actividad;

public class ActividadResultado {
    private String nombreProceso;
    private Actividad actividad;

    // Constructor
    public ActividadResultado(String nombreProceso, Actividad actividad) {
        this.nombreProceso = nombreProceso;
        this.actividad = actividad;
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
