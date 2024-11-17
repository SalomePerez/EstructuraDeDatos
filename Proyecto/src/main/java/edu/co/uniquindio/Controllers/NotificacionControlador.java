package edu.co.uniquindio.Controllers;

import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.Notificacion.*;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Tarea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;


/**
 * Controlador responsable de la gestión y distribución de notificaciones del sistema.
 * Implementa el patrón Singleton para garantizar una única instancia de gestión de notificaciones.
 */

public class NotificacionControlador {
    private static NotificacionControlador instancia;
    private final Cola<Notificacion> colaNotificaciones;
    private Consumer<Notificacion> manejadorNotificaciones;
    private final AsyncEmailService servicioEmail;
    private final WhatsApp servicioWhatsApp;

    /**
     * Constructor privado que inicializa los servicios de notificación.
     * Parte del patrón Singleton.
     */
    private NotificacionControlador() {
        this.colaNotificaciones = new Cola<>();
        this.servicioEmail = AsyncEmailService.getInstance();
        this.servicioWhatsApp = WhatsApp.getInstance();
    }

    /**
     * Obtiene la instancia única del controlador de notificaciones.
     *
     * @return instancia única del NotificacionControlador
     */
    public static NotificacionControlador getInstance() {
        if (instancia == null) {
            instancia = new NotificacionControlador();
        }
        return instancia;
    }

    /**
     * Establece el manejador personalizado para el procesamiento de notificaciones.
     *
     * @param manejador función que procesará las notificaciones
     */

    public void establecerManejadorNotificaciones(Consumer<Notificacion> manejador) {
        this.manejadorNotificaciones = manejador;
    }
    /**
     * Procesa y distribuye una nueva notificación en el sistema.
     *
     * @param titulo título de la notificación
     * @param mensaje contenido de la notificación
     * @param tipo tipo de la notificación
     * @param prioridad nivel de prioridad de la notificación
     * @param idReferencia identificador de referencia asociado
     */
    public void procesarNotificacion(String titulo, String mensaje, TipoNotificacion tipo,
                                     PrioridadNotificaciones prioridad, String idReferencia) {
        Notificacion notificacion = new Notificacion(titulo, idReferencia, prioridad, tipo, mensaje);
        colaNotificaciones.encolar(notificacion);

        if (manejadorNotificaciones != null) {
            manejadorNotificaciones.accept(notificacion);
            distribuirNotificacionMulticanal(notificacion);
        }
    }
    /**
     * Distribuye una notificación a través de múltiples canales de comunicación.
     *
     * @param notificacion la notificación a distribuir
     */

    private void distribuirNotificacionMulticanal(Notificacion notificacion) {
        servicioEmail.sendEmailAsync(notificacion.getTituloNotificacion(), notificacion.getContenido());
        enviarNotificacionInstantanea(notificacion);
    }

    /**
     * Envía una notificación instantánea a través de WhatsApp.
     *
     * @param notificacion la notificación a enviar
     */

    private void enviarNotificacionInstantanea(Notificacion notificacion) {
        new Thread(() -> {
            try {
                DatosContactoUsuario datosContacto = obtenerInformacionContactoUsuario();
                if (datosContacto != null && datosContacto.getTelefono() != null) {
                    String telefono = normalizarNumeroTelefono(datosContacto.getTelefono());
                    servicioWhatsApp.sendNotification(
                            telefono,
                            notificacion.getTituloNotificacion(),
                            datosContacto.getNombre(),
                            notificacion.getContenido()
                    );
                }
            } catch (Exception e) {
                System.err.println("Error al enviar notificación WhatsApp: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Obtiene la información de contacto del usuario actual.
     *
     * @return datos de contacto del usuario
     * @throws IOException si hay error al leer el archivo
     */

    private DatosContactoUsuario obtenerInformacionContactoUsuario() throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader("src/main/resources/Login_Archivo/UsuarioActual"))) {
            br.readLine(); // Skip first line
            return new DatosContactoUsuario(br.readLine(), br.readLine());
        }
    }

    /**
     * Normaliza un número de teléfono agregando el prefijo del país si es necesario.
     *
     * @param telefono número de teléfono a normalizar
     * @return número de teléfono normalizado
     */

    private String normalizarNumeroTelefono(String telefono) {
        return !telefono.startsWith("57") ? "57" + telefono : telefono;
    }

    /**
     * Notifica la creación de una nueva actividad en el sistema.
     */
    public void alertarNuevaActividad(Actividad actividad, Proceso proceso) {
        String mensaje = String.format("La actividad '%s' ha sido creada en el proceso '%s'",
                actividad.obtenerNombre(), proceso.obtenerTitulo());

        procesarNotificacion(
                "Actividad Creada",
                mensaje,
                TipoNotificacion.ACTIVIDAD_CREADA,
                actividad.esObligatoria() ? PrioridadNotificaciones.ALTA : PrioridadNotificaciones.MEDIA,
                proceso.obtenerIdentificador().toString()
        );
    }

    /**
     * Notifica la creación de una nueva tarea en el sistema.
     */
    public void alertarNuevaTarea(Tarea tarea, Actividad actividad, Proceso proceso) {
        String mensaje = String.format("Se ha creado la tarea '%s' en la actividad '%s' del proceso '%s'",
                tarea.obtenerDescripcion(), actividad.obtenerNombre(), proceso.obtenerTitulo());

        procesarNotificacion(
                "Nueva Tarea Creada",
                mensaje,
                TipoNotificacion.TAREA_CREADA,
                tarea.esObligatoria() ? PrioridadNotificaciones.ALTA : PrioridadNotificaciones.MEDIA,
                proceso.obtenerIdentificador().toString()
        );
    }

    /**
     * Notifica el inicio de un nuevo proceso en el sistema.
     */
    public void alertarInicioProceso(Proceso proceso) {
        procesarNotificacion(
                "Nuevo Proceso",
                "Se ha creado el proceso: " + proceso.obtenerTitulo(),
                TipoNotificacion.PROCESO_CREADO,
                PrioridadNotificaciones.BAJA,
                proceso.obtenerIdentificador().toString()
        );
    }

    /**
     * Notifica cuando una tarea ha excedido su tiempo estimado.
     */

    public void alertarVencimientoTarea(Tarea tarea, Actividad actividad, Proceso proceso) {
        String mensaje = String.format("La tarea '%s' en la actividad '%s' ha superado su duración estimada de %d minutos",
                tarea.obtenerDescripcion(), actividad.obtenerNombre(), tarea.obtenerDuracion());

        procesarNotificacion(
                "Tarea Vencida",
                mensaje,
                TipoNotificacion.TAREA_VENCIDA,
                PrioridadNotificaciones.ALTA,
                proceso.obtenerIdentificador().toString()
        );
    }

    /**
     * Notifica cuando una tarea está próxima a vencer.
     */
    public void alertarProximoVencimiento(Tarea tarea, Actividad actividad, Proceso proceso, long minutosRestantes) {
        String mensaje = String.format("La tarea '%s' vencerá en %d minutos en la actividad '%s'",
                tarea.obtenerDescripcion(), minutosRestantes, actividad.obtenerNombre());

        procesarNotificacion(
                "Tarea Próxima a Vencer",
                mensaje,
                TipoNotificacion.TAREA_PROXIMA,
                PrioridadNotificaciones.MEDIA,
                proceso.obtenerIdentificador().toString()
        );
    }

    /**
     * Notifica cuando una actividad está en riesgo por tareas vencidas.
     */
    public void alertarRiesgoActividad(Actividad actividad, Proceso proceso) {
        procesarNotificacion(
                "Actividad en Riesgo",
                "La actividad '" + actividad.obtenerNombre() + "' tiene tareas vencidas",
                TipoNotificacion.ACTIVIDAD_EN_RIESGO,
                PrioridadNotificaciones.ALTA,
                proceso.obtenerIdentificador().toString()
        );
    }
}