package estructura.model;

/**
 * Enumeración para las opciones de estado de una tarea.
 */
public enum Estado {
    OBLIGATORIO("OBLIGATORIO"),
    OPCIONAL("OPCIONAL"),
    COMPLETO("COMPLETO");
    private final String status;
    /**
     * Constructor de EstadoTarea.
     *
     * @param status El estado de la tarea.
     */
    Estado(String status) {
        this.status = status;
    }
    /**
     * Obtiene una representación en cadena del estado.
     *
     * @return El estado en forma de cadena.
     */
    @Override
    public String toString() {
        return this.status;
    }
}