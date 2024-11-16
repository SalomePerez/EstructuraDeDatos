package edu.co.uniquindio.Modelo.Administradores;

import edu.co.uniquindio.Modelo.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Modelo.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Modelo.Principales.Actividad;
import edu.co.uniquindio.Controlador.NotificacionControlador;
import edu.co.uniquindio.Modelo.Principales.Proceso;
import edu.co.uniquindio.Modelo.Principales.Tarea;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AdministradorNotificaciones {

    /**
     * Clase que administra las notificaciones del sistema implementando el patrón Singleton.
     * Se encarga de monitorear los procesos activos y generar notificaciones según el estado de las tareas.
     */
    private static AdministradorNotificaciones instancia;
    private final ScheduledExecutorService scheduler;
    private final ListaEnlazada<Proceso> procesosActivos;

    private AdministradorNotificaciones() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.procesosActivos = new ListaEnlazada<>();
        iniciarMonitoreoAutomatico();
    }

    /**
     * Obtiene la instancia única del administrador de notificaciones.
     * @return La instancia única de AdministradorNotificaciones
     */
    public static AdministradorNotificaciones getInstance() {
        if (instancia == null) {
            instancia = new AdministradorNotificaciones();
        }
        return instancia;
    }

    /**
     * Inicia el monitoreo automático de los procesos en intervalos regulares.
     */
    private void iniciarMonitoreoAutomatico() {
        scheduler.scheduleAtFixedRate(this::procesarVerificacionEstados, 0, 5, TimeUnit.MINUTES);
    }

    /**
     * Agrega un nuevo proceso al sistema de monitoreo.
     * @param proceso El proceso a monitorear
     */
    private void agregarProcesoMonitoreo(Proceso proceso) {
        if (proceso != null) {
            procesosActivos.insertar(proceso);
            NotificacionControlador.getInstance().registrarNotificacionProcesoIniciado(proceso);
        }
    }

    /**
     * Procesa la verificación de estados de todos los procesos activos.
     */
    private void procesarVerificacionEstados() {
        LocalDateTime momentoActual = LocalDateTime.now();
        for (int i = 0; i < procesosActivos.getTamanio(); i++) {
            verificarEstadoProceso(procesosActivos.getElementoEnPosicion(i), momentoActual);
        }
    }

    /**
     * Verifica el estado de un proceso específico.
     * @param proceso El proceso a verificar
     * @param momentoActual El momento actual para los cálculos de tiempo
     */
    private void verificarEstadoProceso(Proceso proceso, LocalDateTime momentoActual) {
        if (proceso != null && proceso.obtenerlistaDeActividades() != null) {
            ListaEnlazada<Actividad> actividades = proceso.obtenerlistaDeActividades();
            for (int i = 0; i < actividades.getTamanio(); i++) {
                Actividad actividad = actividades.getElementoEnPosicion(i);
                procesarVerificacionTareas(actividad, proceso, momentoActual);
            }
        }
    }

    /**
     * Procesa la verificación de las tareas de una actividad.
     * @param actividad La actividad a verificar
     * @param proceso El proceso al que pertenece la actividad
     * @param momentoActual El momento actual para los cálculos de tiempo
     */
    private void procesarVerificacionTareas(Actividad actividad, Proceso proceso, LocalDateTime momentoActual) {
        if (actividad == null || actividad.obtenerTareas() == null) return;

        Cola<Tarea> copiaTareas = new Cola<>();
        boolean existenTareasVencidas = false;

        while (!actividad.obtenerTareas().estaVacia()) {
            Tarea tareaActual = actividad.obtenerTareas().desencolar();
            existenTareasVencidas |= procesarTareaIndividual(tareaActual, actividad, proceso, momentoActual, copiaTareas);
        }

        restaurarTareas(actividad, copiaTareas);

        if (existenTareasVencidas && actividad.esObligatoria()) {
            NotificacionControlador.getInstance().registrarNotificacionActividadEnRiesgo(actividad, proceso);
        }
    }

    /**
     * Procesa una tarea individual y verifica su estado.
     * @param tarea La tarea a procesar
     * @param actividad La actividad a la que pertenece la tarea
     * @param proceso El proceso principal
     * @param momentoActual El momento actual
     * @param copiaTareas Cola para almacenar temporalmente las tareas
     * @return true si la tarea está vencida, false en caso contrario
     */
    private boolean procesarTareaIndividual(Tarea tarea, Actividad actividad, Proceso proceso,
                                            LocalDateTime momentoActual, Cola<Tarea> copiaTareas) {
        if (tarea == null) return false;

        long tiempoTranscurrido = ChronoUnit.MINUTES.between(tarea.obtenerFechaCreacion(), momentoActual);
        long tiempoRestante = tarea.obtenerDuracion() - tiempoTranscurrido;

        if (tiempoRestante <= 0 && tarea.esObligatoria()) {
            NotificacionControlador.getInstance().registrarNotificacionTareaVencida(tarea, actividad, proceso);
            return true;
        } else {
            copiaTareas.encolar(tarea);
            if (tiempoRestante > 0 && tiempoRestante <= 30 && tarea.esObligatoria()) {
                NotificacionControlador.getInstance().registrarNotificacionTareaProximaVencer(tarea, actividad, proceso, tiempoRestante);
            }
            return false;
        }
    }

    /**
     * Restaura las tareas a su cola original.
     * @param actividad La actividad que contiene la cola de tareas
     * @param copiaTareas La cola temporal con las tareas a restaurar
     */
    private void restaurarTareas(Actividad actividad, Cola<Tarea> copiaTareas) {
        while (!copiaTareas.estaVacia()) {
            actividad.obtenerTareas().encolar(copiaTareas.desencolar());
        }
    }

    /**
     * Finaliza el monitoreo y libera los recursos.
     */
    private void finalizarMonitoreo() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}