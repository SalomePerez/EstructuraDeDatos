package edu.co.uniquindio.Pruebas;

import edu.co.uniquindio.Model.Administradores.AdministradorTareas;
import edu.co.uniquindio.Model.Administradores.AdministradorProcesos;
import edu.co.uniquindio.Model.Principales.Tarea;
import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
/*
Requisito Funcional 5: Buscar Tarea
 */
public class BuscarTareaTest {

    private AdministradorTareas administradorTareas;
    private Tarea tarea1, tarea2, tarea3;
    private Cola<Tarea> tareas;

    @Before
    public void setUp() {
        // Crear el AdministradorTareas
        administradorTareas = new AdministradorTareas(new AdministradorProcesos());

        // Crear tareas
        tarea1 = new Tarea("Tarea 1", 30, true);
        tarea2 = new Tarea("Tarea 2", 60, false);
        tarea3 = new Tarea("Tarea 3", 45, true);

        // Inicializamos una cola de tareas
        tareas = new Cola<>();
        tareas.encolar(tarea1);
        tareas.encolar(tarea2);
        tareas.encolar(tarea3);
    }

    @Test
    public void testBuscarTareaDesdeInicio() {
        // Simulamos que buscamos desde el inicio
        Tarea tareaEncontrada = administradorTareas.buscarTareaDesdeActividad(tareas, "nombre e actividad");

        assertEquals("La tarea debería ser la primera en la cola.",tareaEncontrada, tarea1);
    }

    @Test
    public void testBuscarTareaDesdeActividad() {
        // Simulamos que buscamos la tarea desde una actividad
        Tarea tareaEncontrada = administradorTareas.buscarTareaPorNombre(tareas, "Tarea 2");

        assertEquals("La tarea debería ser 'Tarea 2'.",tareaEncontrada, tarea2);
    }

    @Test
    public void testBuscarTareaPorNombre() {
        // Buscar tarea dado su nombre
        Tarea tareaEncontrada = administradorTareas.buscarTareaPorNombre(tareas, "Tarea 3");

        assertEquals("La tarea debería ser 'Tarea 3'.",tareaEncontrada, tarea3);
    }

    @Test
    public void testBuscarTareaNoExistente() {
        // Buscar una tarea que no existe
        Tarea tareaEncontrada = administradorTareas.buscarTareaPorNombre(tareas, "Tarea Inexistente");

        assertNull("La tarea no debería existir.", tareaEncontrada);
    }
}
