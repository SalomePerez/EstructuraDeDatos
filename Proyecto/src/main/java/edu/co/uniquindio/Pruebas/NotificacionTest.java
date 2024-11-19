package edu.co.uniquindio.Pruebas;

import static org.junit.Assert.*;

import edu.co.uniquindio.Model.Notificacion.Notificacion;
import edu.co.uniquindio.Model.Notificacion.PrioridadNotificaciones;
import edu.co.uniquindio.Model.Notificacion.TipoNotificacion;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;

/*
Requisito funcionales 9: Notificacion y recordatorios
 */
public class NotificacionTest {

    private Notificacion notificacion;

    @Before
    public void setUp() {
        // Inicialización de los objetos antes de cada prueba
        notificacion = new Notificacion("Recordatorio de tarea pendiente", "T123", PrioridadNotificaciones.ALTA, TipoNotificacion.TAREA_PROXIMA, "Tarea próxima");
    }

    @Test
    public void testNotificacionCreadaCorrectamente() {
        assertNotNull("La notificación no debe ser nula", notificacion);
        assertEquals("El contenido de la notificación es incorrecto", "Recordatorio de tarea pendiente", notificacion.getContenido());
        assertEquals("La referencia de la notificación es incorrecta", "T123", notificacion.getIdReferencia());
        assertEquals("La prioridad de la notificación es incorrecta", PrioridadNotificaciones.ALTA, notificacion.getPrioridad());
        assertEquals("El tipo de notificación es incorrecto", TipoNotificacion.TAREA_PROXIMA, notificacion.getTipoNotificacion());
        assertEquals("El título de la notificación es incorrecto", "Tarea próxima", notificacion.getTituloNotificacion());
    }

    @Test
    public void testNotificacionFechaCreacion() {
        LocalDateTime fechaCreacion = notificacion.getFechaCreacion();
        assertNotNull("La fecha de creación no debe ser nula", fechaCreacion);
        assertTrue("La fecha de creación debe ser posterior a una fecha fija", fechaCreacion.isAfter(LocalDateTime.of(2023, 1, 1, 0, 0)));
    }
}
