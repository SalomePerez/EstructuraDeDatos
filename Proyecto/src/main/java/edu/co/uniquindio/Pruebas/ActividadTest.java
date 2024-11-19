package edu.co.uniquindio.Pruebas;

import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/*
Requisito funcional 2:

 */
public class ActividadTest {

    private Proceso proceso;

    @Before
    public void setUp() {
        proceso = new Proceso("Proceso de ejemplo");
    }

    @Test
    public void testCrearActividadConPrecedencia() {
        // Crear actividades iniciales
        Actividad diseño = new Actividad("Diseño", "Diseñar el sistema", true);
        Actividad pruebas = new Actividad("Pruebas", "Realizar pruebas de calidad", false);
        proceso.agregarActividad(diseño);
        proceso.agregarActividad(pruebas);

        // Crear actividad con precedencia (Diseño)
        ListaEnlazada<Actividad> lista = proceso.obtenerlistaDeActividades();

        // Crear la actividad a insertar
        Actividad implementacion = new Actividad("Implementación", "Implementar el sistema", true);

        // Llamar al método insertarDespuesDe
        lista.insertarDespuesDe(diseño, implementacion);

        // Validar que las actividades estén en el orden correcto
        assertEquals("Diseño", lista.getElementoEnPosicion(0).obtenerNombre());
        assertEquals("Implementación", lista.getElementoEnPosicion(1).obtenerNombre());
        assertEquals("Pruebas", lista.getElementoEnPosicion(2).obtenerNombre());
    }




    @Test
    public void testCrearActividadAlFinal() {
        // Crear actividades iniciales
        proceso.agregarActividad(new Actividad("Diseño", "Diseñar el sistema", true));
        proceso.agregarActividad(new Actividad("Implementación", "Implementar el sistema", true));

        // Crear actividad al final
        proceso.agregarActividad(new Actividad("Mantenimiento", "Dar mantenimiento al sistema", false));

        // Validar que la actividad esté al final
        ListaEnlazada<Actividad> lista = proceso.obtenerlistaDeActividades();
        assertEquals("Mantenimiento", lista.getElementoEnPosicion(lista.getTamanio() - 1).obtenerNombre());
    }

    @Test
    public void testCrearActividadDespuesDeUltimaCreada() {
        // Crear actividades iniciales
        proceso.agregarActividad(new Actividad("Diseño", "Diseñar el sistema", true));
        proceso.agregarActividad(new Actividad("Implementación", "Implementar el sistema", true));

        // Crear actividad después de la última
        ListaEnlazada<Actividad> lista = proceso.obtenerlistaDeActividades();
        lista.insertar(new Actividad("Documentación", "Documentar el sistema", true));

        // Validar que la actividad se haya insertado al final
        assertEquals("Documentación", lista.getElementoEnPosicion(lista.getTamanio() - 1).obtenerNombre());
    }

    @Test
    public void testNoPermitirActividadesDuplicadas() {
        // Crear actividad inicial
        proceso.agregarActividad(new Actividad("Diseño", "Diseñar el sistema", true));

        // Intentar agregar actividad con el mismo nombre
        assertThrows(IllegalArgumentException.class, () -> {
            ListaEnlazada<Actividad> lista = proceso.obtenerlistaDeActividades();
            for (int i = 0; i < lista.getTamanio(); i++) {
                if (lista.getElementoEnPosicion(i).obtenerNombre().equals("Diseño")) {
                    throw new IllegalArgumentException("Ya existe una actividad con este nombre: Diseño");
                }
            }
            proceso.agregarActividad(new Actividad("Diseño", "Intentar duplicar", false));
        });
    }
}
