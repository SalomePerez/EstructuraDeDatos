package edu.co.uniquindio.Modelo.Principales;

import edu.co.uniquindio.Modelo.EstructuraDeDatos.ListaEnlazada;

import java.time.LocalDateTime;
import java.util.UUID;

public class Proceso {
    private static Proceso instancia;    private String nombre; // Título del proceso
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
}