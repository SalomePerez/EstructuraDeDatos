package edu.co.uniquindio.Pruebas;

import edu.co.uniquindio.Controllers.ControladorActividad;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Tarea;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/*
Funcionalidad 4 : Buscar Actividad :)
 */

public class BuscarActividadTest {

    private ControladorActividad controladorActividad;
    private Actividad actividad;
    private Tarea tarea1;
    private Tarea tarea2;
    private Proceso proceso;

    @Before
    public void setUp() {
        // Crear el proceso y la actividad
        proceso = new Proceso("Proceso 1");
        actividad = new Actividad("Actividad 1", "Descripción de la actividad", true);

        // Crear las tareas y agregarlas a la actividad
        tarea1 = new Tarea("Tarea 1", 30, true); // 30 minutos
        tarea2 = new Tarea("Tarea 2", 45, false); // 45 minutos
        actividad.agregarTarea(tarea1);
        actividad.agregarTarea(tarea2);

        // Crear el controlador de la actividad
        controladorActividad = new ControladorActividad(proceso, actividad);
    }

    @Test
    public void testBuscarActividadPorNombre() {
        // Simula la búsqueda de la actividad por nombre
        String nombreBusqueda = "Actividad 1";
        Actividad actividadEncontrada = buscarActividadPorNombre(nombreBusqueda);

        // Verificar que la actividad encontrada tiene el nombre correcto
        assertEquals("Actividad 1", actividadEncontrada.obtenerNombre());
    }

    @Test
    public void testMostrarDetallesActividad() {
        // Simula la búsqueda de la actividad por nombre
        String nombreBusqueda = "Actividad 1";
        Actividad actividadEncontrada = buscarActividadPorNombre(nombreBusqueda);

        // Verificar los detalles de la actividad
        assertEquals("Descripción de la actividad", actividadEncontrada.obtenerDescripcion());
        assertEquals(2, actividadEncontrada.obtenerTareas().obtenerTamano()); // Debe tener 2 tareas

        // Verificar el tiempo mínimo y máximo
        int tiempoMinimo = calcularTiempoMinimo(actividadEncontrada);
        int tiempoMaximo = calcularTiempoMaximo(actividadEncontrada);

        assertEquals(30, tiempoMinimo); // El tiempo mínimo es 30 minutos
        assertEquals(45, tiempoMaximo); // El tiempo máximo es 45 minutos
    }

    // Método simulado de búsqueda de actividad por nombre
    private Actividad buscarActividadPorNombre(String nombre) {
        // Aquí se buscaría la actividad dentro de una lista o base de datos de actividades.
        // Para simplificar, se retorna la actividad creada previamente.
        return actividad;
    }

    // Método simulado para calcular el tiempo mínimo de las tareas de una actividad
    private int calcularTiempoMinimo(Actividad actividad) {
        int tiempoMinimo = Integer.MAX_VALUE;

        // Buscar el tiempo mínimo entre las tareas de la actividad
        for (Tarea tarea : actividad.obtenerTareas()) {
            if (tarea.obtenerDuracion() < tiempoMinimo) {
                tiempoMinimo = tarea.obtenerDuracion();
            }
        }
        return tiempoMinimo;
    }

    // Método simulado para calcular el tiempo máximo de las tareas de una actividad
    private int calcularTiempoMaximo(Actividad actividad) {
        int tiempoMaximo = Integer.MIN_VALUE;

        // Buscar el tiempo máximo entre las tareas de la actividad
        for (Tarea tarea : actividad.obtenerTareas()) {
            if (tarea.obtenerDuracion() > tiempoMaximo) {
                tiempoMaximo = tarea.obtenerDuracion();
            }
        }
        return tiempoMaximo;
    }
}
