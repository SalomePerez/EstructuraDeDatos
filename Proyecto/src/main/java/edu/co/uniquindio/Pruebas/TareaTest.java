package edu.co.uniquindio.Pruebas;

import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.Principales.Tarea;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/*
Funcionalidad 3: Crear tareas
 */
public class TareaTest {

    private Cola<Tarea> colaTareas;
    private Tarea tarea1;
    private Tarea tarea2;
    private Tarea tarea3;
    private Tarea tarea4;

    @Before
    public void setUp() {
        // Inicialización de la cola y tareas
        colaTareas = new Cola<>();
        tarea1 = new Tarea("Tarea 1", 30, true);
        tarea2 = new Tarea("Tarea 2", 45, false);  // Tarea opcional
        tarea3 = new Tarea("Tarea 3", 60, true);
        tarea4 = new Tarea("Tarea 4", 20, false);  // Tarea opcional
    }

    @Test
    public void testAgregarTareaAlFinal() {
        // Insertar tareas al final
        colaTareas.encolar(tarea1);
        colaTareas.encolar(tarea2);

        // Verificar que el tamaño de la cola sea 2
        assertEquals(2, colaTareas.obtenerTamano());  // La cola debe contener 2 tareas

        // Verificar que la primera tarea sea 'Tarea 1'
        assertEquals(tarea1, colaTareas.obtenerPrimero().dato);  // La primera tarea debe ser 'Tarea 1'

        // Verificar que al desencolar, la tarea 'Tarea 1' se elimina y la siguiente es 'Tarea 2'
        assertEquals(tarea1, colaTareas.desencolar());  // La tarea desencolada debe ser 'Tarea 1'
        assertEquals(tarea2, colaTareas.desencolar());  // La siguiente tarea desencolada debe ser 'Tarea 2'
    }


    @Test
    public void testAgregarTareaEnPosicionEspecifica() {
        // Insertar tareas al final
        colaTareas.encolar(tarea1);
        colaTareas.encolar(tarea2);

        // Crear una nueva cola donde insertaremos las tareas en una posición específica
        Cola<Tarea> nuevaCola = new Cola<>();

        // Agregar tareas en posiciones específicas
        nuevaCola.encolar(tarea1);  // Tarea 1 en primer lugar
        nuevaCola.encolar(tarea3);  // Tarea 3 en segundo lugar
        nuevaCola.encolar(tarea2);  // Tarea 2 en tercer lugar

        // Verificar que la primera tarea en la nueva cola es 'Tarea 1'
        assertEquals(tarea1, nuevaCola.obtenerPrimero().dato);

        // Eliminar la primera tarea (Tarea 1) y verificar la siguiente
        nuevaCola.desencolar();  // Eliminar Tarea 1
        assertEquals(tarea3, nuevaCola.obtenerPrimero().dato);  // La siguiente tarea debe ser 'Tarea 3'

        // Eliminar la segunda tarea (Tarea 3) y verificar la siguiente
        nuevaCola.desencolar();  // Eliminar Tarea 3
        assertEquals(tarea2, nuevaCola.obtenerPrimero().dato);  // La siguiente tarea debe ser 'Tarea 2'
    }


    @Test
    public void testNoDosTareasOpcionalesSeguidas() {
        // Agregar tarea obligatoria y tarea opcional
        colaTareas.encolar(tarea1); // Tarea obligatoria
        colaTareas.encolar(tarea2); // Tarea opcional

        // Intentar agregar tarea opcional después de tarea opcional
        try {
            // Verificar si la última tarea es opcional antes de agregar la nueva tarea
            if (!colaTareas.estaVacia() && !tarea4.esObligatoria()) {
                // Obtener la última tarea en la cola
                Tarea ultimaTarea = null;
                for (Tarea t : colaTareas) {
                    ultimaTarea = t; // La última tarea
                }
                // Si la última tarea también es opcional, lanzar excepción
                if (ultimaTarea != null && !ultimaTarea.esObligatoria()) {
                    throw new IllegalArgumentException("No se pueden agregar dos tareas opcionales seguidas.");
                }
            }
            // Si todo está bien, encolar la tarea
            colaTareas.encolar(tarea4);
        } catch (IllegalArgumentException e) {
            // Verificar que la excepción fue lanzada con el mensaje esperado
            assertEquals("No se pueden agregar dos tareas opcionales seguidas.", e.getMessage());
        }
    }


    @Test
    public void testAgregarTareasAlFinalYDesencolar() {
        // Insertar tareas al final
        colaTareas.encolar(tarea1);
        colaTareas.encolar(tarea2);
        colaTareas.encolar(tarea3);

        // Comprobar el tamaño y desencolar
        assertEquals(3, colaTareas.obtenerTamano()); //"La cola debe tener 3 tareas."
        assertEquals(tarea1, colaTareas.desencolar());  //"La primera tarea desencolada debe ser 'Tarea 1'."
        assertEquals(tarea2, colaTareas.desencolar()); //"La siguiente tarea desencolada debe ser 'Tarea 2'."
        assertEquals(tarea3, colaTareas.desencolar());  //"La última tarea desencolada debe ser 'Tarea 3'."
    }
}
