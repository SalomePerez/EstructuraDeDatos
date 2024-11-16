package edu.co.uniquindio.Controlador;


import edu.co.uniquindio.Modelo.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Modelo.Notificacion.*;
import edu.co.uniquindio.Modelo.Principales.Actividad;
import edu.co.uniquindio.Modelo.Principales.Proceso;
import edu.co.uniquindio.Modelo.Principales.Tarea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

public class NotificacionControlador {
    // Instancia única (Singleton)
    private static NotificacionControlador instancia;

    // Cola para almacenar las notificaciones pendientes de procesar
    private Cola<Notificacion> colaNotificaciones;

    // Función que manejará las notificaciones generadas
    private Consumer<Notificacion> manejadorNotificaciones;

    // Servicios para enviar notificaciones por correo y WhatsApp
    private Email servicioEmail;
    private AsyncEmailService servicioEmailMejorado;
    private WhatsApp servicioWhatsApp;

    // Constructor privado para el patrón Singleton
    public NotificacionControlador() {
        this.colaNotificaciones = new Cola<>();
        this.servicioEmail = Email.getInstance(); // Servicio de correo estándar
        this.servicioEmailMejorado = AsyncEmailService.getInstance(); // Servicio mejorado
        this.servicioWhatsApp = WhatsApp.getInstance(); // Servicio de WhatsApp
    }

    // Método estático para obtener la instancia única (Singleton)
    public static NotificacionControlador getInstance() {
        if (instancia == null) {
            instancia = new NotificacionControlador();
        }
        return instancia;
    }

    // Establece una función manejadora personalizada para las notificaciones
    public void establecerManejadorNotificaciones(Consumer<Notificacion> manejador) {
        this.manejadorNotificaciones = manejador;
    }

    // Registra una notificación cuando se crea una actividad en un proceso
    public void registrarNotificacionActividadCreada(Actividad actividad, Proceso proceso) {
        generarNotificacion(
                "Actividad Creada",
                "La actividad '" + actividad.obtenerNombre() + "' ha sido creada en el proceso '" +
                        proceso.obtenerTitulo() + "'",
                TipoNotificacion.ACTIVIDAD_CREADA,
                PrioridadNotificaciones.valueOf(actividad.esObligatoria() ? PrioridadNotificaciones.ALTA.getDescripcion() : PrioridadNotificaciones.MEDIA.getDescripcion()),
                proceso.obtenerIdentificador().toString()
        );
    }


    // Registra una notificación cuando se crea una tarea en una actividad y proceso
    public void registrarNotificacionTareaCreada(Tarea tarea, Actividad actividad, Proceso proceso) {
        generarNotificacion(
                "Nueva Tarea Creada",
                "Se ha creado la tarea '" + tarea.obtenerDescripcion() + "' en la actividad '" +
                        actividad.obtenerNombre() + "' del proceso '" + proceso.obtenerTitulo() + "'",
                TipoNotificacion.TAREA_CREADA,
                PrioridadNotificaciones.valueOf(tarea.esObligatoria() ? PrioridadNotificaciones.ALTA.getDescripcion() : PrioridadNotificaciones.MEDIA.getDescripcion()),
                proceso.obtenerIdentificador().toString()
        );
    }

    // Método interno para generar y manejar notificaciones
    private void generarNotificacion(String titulo, String mensaje, TipoNotificacion tipo,
                                     PrioridadNotificaciones prioridad, String idReferencia) {
        // Crear la notificación con los datos proporcionados
        Notificacion notificacion = new Notificacion(titulo, idReferencia, prioridad, tipo, mensaje);
        colaNotificaciones.encolar(notificacion); // Agregar a la cola

        // Si hay un manejador definido, procesar la notificación
        if (manejadorNotificaciones != null) {
            manejadorNotificaciones.accept(notificacion);
            distribuirNotificaciones(titulo, mensaje); // Enviar notificación a los servicios
        }
    }

    // Método para distribuir notificaciones a través de los servicios configurados
    private void distribuirNotificaciones(String titulo, String mensaje) {
        servicioEmailMejorado.sendEmailAsync(titulo, mensaje); // Enviar correo
        enviarNotificacionWhatsApp(titulo, mensaje); // Enviar WhatsApp
    }

    // Enviar notificaciones por WhatsApp en un hilo separado
    private void enviarNotificacionWhatsApp(String titulo, String mensaje) {
        new Thread(() -> {
            try {
                DatosContactoUsuario datosContacto = obtenerDatosContactoUsuario(); // Obtener datos del usuario actual
                if (datosContacto != null && datosContacto.getTelefono() != null &&
                        !datosContacto.getTelefono().isEmpty()) {

                    String telefono = formatearNumeroTelefono(datosContacto.getTelefono()); // Formatear número
                    servicioWhatsApp.sendNotification(
                            telefono,
                            titulo,
                            datosContacto.getNombre(),
                            mensaje
                    );
                }
            } catch (IOException e) {
                registrarErrorNotificacion("Error al leer datos de contacto", e);
            } catch (Exception e) {
                registrarErrorNotificacion("Error al enviar mensaje WhatsApp", e);
            }
        }).start();
    }

    // Lee los datos de contacto del usuario actual desde un archivo
    private DatosContactoUsuario obtenerDatosContactoUsuario() throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader("src/main/resources/Login_Archivo/UsuarioActual"))) {
            br.readLine(); // Saltar primera línea (irrelevante)
            String telefono = br.readLine(); // Leer teléfono
            String nombre = br.readLine(); // Leer nombre
            return new DatosContactoUsuario(telefono, nombre); // Crear y retornar datos de contacto
        }
    }

    // Formatea el número de teléfono añadiendo el código de país si no está presente
    private String formatearNumeroTelefono(String telefono) {
        return !telefono.startsWith("57") ? "57" + telefono : telefono;
    }

    // Registra errores en el sistema
    private void registrarErrorNotificacion(String mensaje, Exception e) {
        System.err.println(mensaje + ": " + e.getMessage()); // Imprimir error
        e.printStackTrace(); // Mostrar traza del error
    }


    // Registra una notificación al iniciar un proceso
    public void registrarNotificacionProcesoIniciado(Proceso proceso) {
        generarNotificacion(
                "Nuevo Proceso",
                "Se ha creado el proceso: " + proceso.obtenerTitulo(),
                TipoNotificacion.PROCESO_CREADO,
                PrioridadNotificaciones.BAJA,
                proceso.obtenerIdentificador().toString()
        );
    }

    // Registra una notificación cuando una tarea vence
    public void registrarNotificacionTareaVencida(Tarea tarea, Actividad actividad, Proceso proceso) {
        generarNotificacion(
                "Tarea Vencida",
                "La tarea '" + tarea.obtenerDescripcion() + "' en la actividad '" +
                        actividad.obtenerNombre() + "' ha superado su duración estimada de " +
                        tarea.obtenerDuracion() + " minutos",
                TipoNotificacion.TAREA_VENCIDA,
                PrioridadNotificaciones.ALTA,
                proceso.obtenerIdentificador().toString()
        );
    }

    // Registra una notificación cuando una tarea está próxima a vencer
    public void registrarNotificacionTareaProximaVencer(Tarea tarea, Actividad actividad,
                                                        Proceso proceso, long minutosRestantes) {
        generarNotificacion(
                "Tarea Próxima a Vencer",
                "La tarea '" + tarea.obtenerDescripcion() + "' vencerá en " +
                        minutosRestantes + " minutos" + " en la actividad '" + actividad.obtenerNombre() + "'",
                TipoNotificacion.TAREA_PROXIMA,
                PrioridadNotificaciones.MEDIA,
                proceso.obtenerIdentificador().toString()
        );
    }

    // Registra una notificación cuando una actividad está en riesgo por tareas vencidas
    public void registrarNotificacionActividadEnRiesgo(Actividad actividad, Proceso proceso) {
        generarNotificacion(
                "Actividad en Riesgo",
                "La actividad '" + actividad.obtenerNombre() + "' tiene tareas vencidas",
                TipoNotificacion.ACTIVIDAD_EN_RIESGO,
                PrioridadNotificaciones.ALTA,
                proceso.obtenerIdentificador().toString()
        );
    }


}
