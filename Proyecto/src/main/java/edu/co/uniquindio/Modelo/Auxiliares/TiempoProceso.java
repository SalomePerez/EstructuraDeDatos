package edu.co.uniquindio.Modelo.Auxiliares;


/**
 * Clase auxiliar que representa el tiempo mínimo y máximo restante para completar un proceso.
 */

public class TiempoProceso {

    private final int tiempoMinimo;
    private final int tiempoMaximo;

    /**
     * Constructor de la clase TiempoProceso.
     *
     * @param tiempoMinimo Tiempo mínimo restante.
     * @param tiempoMaximo Tiempo máximo restante.
     */
    public TiempoProceso(int tiempoMinimo, int tiempoMaximo) {
        this.tiempoMinimo = tiempoMinimo;
        this.tiempoMaximo = tiempoMaximo;
    }

    public int getTiempoMinimo() {
        return tiempoMinimo;
    }

    public int getTiempoMaximo() {
        return tiempoMaximo;
    }
}
