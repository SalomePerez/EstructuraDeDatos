package edu.co.uniquindio.Pruebas;

import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Tarea;
import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.Administradores.AdministradorProcesos;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/*
Requisistos Funcionales 7: Intercambiar Actividades
 */
public class IntercambiarActividadTest {

    private Actividad actividad1;
    private Actividad actividad2;
    private Tarea tarea1;
    private Tarea tarea2;
    private AdministradorProcesos administradorProcesos;

    @Before
    public void setUp() {
        // Inicializamos las actividades
        actividad1 = new Actividad("Actividad 1", "Descripción de Actividad 1", true);
        actividad2 = new Actividad("Actividad 2", "Descripción de Actividad 2", false);

        // Inicializamos tareas para las actividades
        tarea1 = new Tarea("Descripción de tarea 1", 20, false);
        tarea2 = new Tarea("Descripción de tarea 2", 20, true);

        // Asignamos tareas a las actividades
        actividad1.agregarTarea(tarea1);
        actividad2.agregarTarea(tarea2);

        // Inicializamos el administrador de procesos (puedes ajustarlo si tienes un proceso específico)
        administradorProcesos = new AdministradorProcesos();
    }

    @Test
    public void testIntercambiarActividadesConTareas() {
        // Intercambiamos las actividades con tareas
        administradorProcesos.intercambiarActividades(actividad1, actividad2, true);

        // Verificamos que los nombres se hayan intercambiado
        assertEquals("Actividad 2", actividad1.obtenerNombre());
        assertEquals("Actividad 1", actividad2.obtenerNombre());

        // Verificamos que las tareas se hayan intercambiado
        assertSame(tarea1, actividad2.obtenerTareas().obtenerElementoEnPosicion(0));
        assertSame(tarea2, actividad1.obtenerTareas().obtenerElementoEnPosicion(0));
    }

    @Test
    public void testIntercambiarActividadesSinTareas() {
        // Intercambiamos las actividades sin tareas
        administradorProcesos.intercambiarActividades(actividad1, actividad2, false);

        // Verificamos que los nombres se hayan intercambiado
        assertEquals("Actividad 2", actividad1.obtenerNombre());
        assertEquals("Actividad 1", actividad2.obtenerNombre());

        // Verificamos que las tareas no se hayan intercambiado
        assertEquals(1, actividad1.obtenerTareas().obtenerTamano());
        assertEquals(1, actividad2.obtenerTareas().obtenerTamano());
    }
}
