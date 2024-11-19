package edu.co.uniquindio.Pruebas;

import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import org.junit.Test;
import java.util.UUID;
import static org.junit.Assert.*;


/*
Requisito funcional 1:
 crear procesos:
La aplicación debe permitir crear y configurar cada proceso, un proceso debe tener un id y un nombre y  un conjunto de actividades.
 */
public class ProcesoTest {

    @Test
    public void testCrearProceso() {
        // Crear un proceso con nombre "Proceso de Prueba"
        String nombreProceso = "Proceso de Prueba";
        Proceso proceso = new Proceso(nombreProceso);

        // Verificar que el nombre del proceso es correcto
        assertEquals(nombreProceso, proceso.obtenerTitulo());

        // Verificar que el proceso tiene un ID único
        UUID idProceso = proceso.obtenerIdentificador();
        assertNotNull(idProceso);

        // Verificar que el ID es único (realizando una segunda creación)
        Proceso otroProceso = new Proceso("Otro Proceso");
        UUID otroIdProceso = otroProceso.obtenerIdentificador();
        assertNotEquals(idProceso, otroIdProceso);

        // Verificar que el proceso tiene un conjunto de actividades vacío al inicio
        assertEquals(0, proceso.obtenerlistaDeActividades().getTamanio());
    }

    @Test
    public void testAgregarActividadAlProceso() {
        // Crear un proceso y agregarle una actividad
        Proceso proceso = new Proceso("Proceso con Actividades");
        Actividad actividad = new Actividad("Actividad 1", "Descripción de actividad", true);

        proceso.agregarActividad(actividad);

        // Verificar que la actividad fue agregada al proceso
        assertEquals(1, proceso.obtenerlistaDeActividades().getTamanio());

        // Verificar que la actividad agregada tiene el nombre correcto
        assertEquals("Actividad 1", proceso.obtenerlistaDeActividades().getElementoEnPosicion(0).obtenerNombre());
    }
}

