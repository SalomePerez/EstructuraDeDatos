package edu.co.uniquindio.Model.Notificacion;

public enum PrioridadNotificaciones {

    BAJA("Baja importancia"),
    MEDIA("Importancia moderada"),
    ALTA("Alta importancia"),
    URGENTE("Atención inmediata");

    private final String descripcion;

    // Constructor
    PrioridadNotificaciones(String descripcion) {
        this.descripcion = descripcion;
    }

    // Método para obtener la descripción
    public String getDescripcion() {
        return descripcion;
    }
}
