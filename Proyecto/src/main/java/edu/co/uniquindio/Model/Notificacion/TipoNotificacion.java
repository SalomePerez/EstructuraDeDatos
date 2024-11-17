package edu.co.uniquindio.Model.Notificacion;

public enum TipoNotificacion {
    // Notificaciones de errores o alertas generales
    ERROR,
    ALERTA,

    // Notificaciones relacionadas con tareas
    TAREA_CREADA,
    TAREA_VENCIDA,
    TAREA_PROXIMA,

    // Notificaciones relacionadas con actividades
    ACTIVIDAD_CREADA,
    ACTIVIDAD_INICIADA,
    ACTIVIDAD_COMPLETADA,
    ACTIVIDAD_INTERCAMBIADA,
    ACTIVIDAD_EN_RIESGO,

    // Notificaciones relacionadas con procesos
    PROCESO_CREADO,
    PROCESO_INICIADO,
    PROCESO_COMPLETADO,
    PROCESO_EN_RIESGO
}
