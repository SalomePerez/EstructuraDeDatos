package edu.co.uniquindio.Modelo.Notificacion;

import java.time.LocalDateTime;

public class Notificacion {


    /**
     * Clase que representa una notificación dentro del sistema.
     * Contiene información sobre el título, contenido, tipo, prioridad,
     * estado de lectura, fecha de creación y un identificador único.
     */


    private String tituloNotificacion;
    private String contenido;
    private TipoNotificacion tipoNotificacion;
    private PrioridadNotificaciones prioridad;
    private LocalDateTime fechaCreacion;
    private boolean estaLeida;
    private String idReferencia;

    public Notificacion(String contenido,String idReferencia, PrioridadNotificaciones prioridad, TipoNotificacion tipoNotificacion, String tituloNotificacion) {
        this.contenido = contenido;
        this.fechaCreacion = LocalDateTime.now();
        this.idReferencia = idReferencia;
        this.prioridad = prioridad;
        this.tipoNotificacion = tipoNotificacion;
        this.tituloNotificacion = tituloNotificacion;
    }


    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isEstaLeida() {
        return estaLeida;
    }

    public void setEstaLeida(boolean estaLeida) {
        this.estaLeida = estaLeida;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(String idReferencia) {
        this.idReferencia = idReferencia;
    }

    public PrioridadNotificaciones getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadNotificaciones prioridad) {
        this.prioridad = prioridad;
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getTituloNotificacion() {
        return tituloNotificacion;
    }

    public void setTituloNotificacion(String tituloNotificacion) {
        this.tituloNotificacion = tituloNotificacion;
    }
}
