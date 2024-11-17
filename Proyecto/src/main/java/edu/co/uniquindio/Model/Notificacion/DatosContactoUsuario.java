package edu.co.uniquindio.Model.Notificacion;

public class DatosContactoUsuario {

    private final String telefono;
    private final String nombre;

    public DatosContactoUsuario(String telefono, String nombre) {
        this.telefono = telefono;
        this.nombre = nombre;
    }

    public String getTelefono() { return telefono; }
    public String getNombre() { return nombre; }

}
