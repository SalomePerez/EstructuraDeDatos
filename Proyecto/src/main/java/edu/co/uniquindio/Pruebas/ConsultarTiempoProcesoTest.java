package edu.co.uniquindio.Pruebas;

import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Auxiliares.TiempoProceso;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/*
Requisito Funcional 6: Consultar tiempo duración proceso
 */
public class ConsultarTiempoProcesoTest {

    private Proceso proceso;
    private TiempoProceso tiempoProceso;

    @Before
    public void setUp() {
        // Inicializamos el TiempoProceso con tiempo mínimo y máximo
        tiempoProceso = new TiempoProceso(5, 10); // 5 minutos como mínimo y 10 minutos como máximo
        proceso = new Proceso(tiempoProceso); // Creamos el proceso y le asignamos el tiempo de duración
    }

    @Test
    public void testObtenerTiempoMinimo() {
        // Verificamos que el tiempo mínimo obtenido desde el proceso sea correcto
        int tiempoMinimo = proceso.obtenerTiempoDuracion().getTiempoMinimo();
        assertEquals("El tiempo mínimo debería ser 5 minutos.", 5, tiempoMinimo);
    }

    @Test
    public void testObtenerTiempoMaximo() {
        // Verificamos que el tiempo máximo obtenido desde el proceso sea correcto
        int tiempoMaximo = proceso.obtenerTiempoDuracion().getTiempoMaximo();
        assertEquals("El tiempo máximo debería ser 10 minutos.", 10, tiempoMaximo);
    }

    @Test
    public void testConsultarTiempoDuracion() {
        // Verificamos que el proceso retorna correctamente tanto el tiempo mínimo como el máximo
        int tiempoMinimo = proceso.obtenerTiempoDuracion().getTiempoMinimo();
        int tiempoMaximo = proceso.obtenerTiempoDuracion().getTiempoMaximo();

        // Realizamos las aserciones de forma tradicional
        assertEquals("El tiempo mínimo debería ser 5 minutos.", 5, tiempoMinimo);
        assertEquals("El tiempo máximo debería ser 10 minutos.", 10, tiempoMaximo);
    }
}
