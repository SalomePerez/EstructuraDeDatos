package edu.co.uniquindio.Modelo.Auxiliares;

import edu.co.uniquindio.Modelo.Administradores.AdministradorProcesos;
import edu.co.uniquindio.Modelo.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Modelo.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Modelo.Principales.Actividad;
import edu.co.uniquindio.Modelo.Principales.Proceso;
import edu.co.uniquindio.Modelo.Principales.Tarea;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Clase que maneja la exportación e importación de datos entre la aplicación y archivos Excel.
 * Permite guardar y cargar información sobre procesos, actividades y tareas.
 */
public class GestorDatosExcel {

    private final AdministradorProcesos administradorProcesos;

    /**
     * Constructor de la clase.
     * @param administradorProcesos Gestor que maneja los procesos de la aplicación
     */
    public GestorDatosExcel(AdministradorProcesos administradorProcesos) {
        this.administradorProcesos = administradorProcesos;
    }

    /**
     * Exporta un proceso específico a un archivo Excel.
     * @param rutaArchivo Ruta donde se guardará el archivo Excel
     * @param idProceso Identificador único del proceso a exportar
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void exportarProceso(String rutaArchivo, UUID idProceso) throws IOException {
        Proceso procesoSeleccionado = buscarProcesoPorId(idProceso);
        if (procesoSeleccionado == null) {
            throw new IllegalArgumentException("No se encontró el proceso con ID: " + idProceso);
        }

        try (Workbook libroExcel = new XSSFWorkbook()) {
            Sheet hojaProceso = libroExcel.createSheet("Proceso");
            crearEncabezadosProceso(hojaProceso);
            llenarDatosProceso(hojaProceso, procesoSeleccionado);

            Sheet hojaActividades = libroExcel.createSheet("Actividades");
            crearEncabezadosActividades(hojaActividades);
            llenarDatosActividades(hojaActividades, procesoSeleccionado);

            Sheet hojaTareas = libroExcel.createSheet("Tareas");
            crearEncabezadosTareas(hojaTareas);
            llenarDatosTareas(hojaTareas, procesoSeleccionado);

            try (FileOutputStream archivoSalida = new FileOutputStream(rutaArchivo)) {
                libroExcel.write(archivoSalida);
            }
        }
    }

    /**
     * Importa datos desde un archivo Excel.
     * @param rutaArchivo Ruta del archivo Excel a importar
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public void importarDatos(String rutaArchivo) throws IOException {
        try (FileInputStream entrada = new FileInputStream(rutaArchivo);
             Workbook libroExcel = new XSSFWorkbook(entrada)) {

            ListaEnlazada<Proceso> procesosImportados = importarProcesos(libroExcel.getSheet("Proceso"));
            importarActividades(libroExcel.getSheet("Actividades"), procesosImportados);
            importarTareas(libroExcel.getSheet("Tareas"), procesosImportados);

            actualizarProcesosSistema(procesosImportados);
        }
    }

    // Métodos privados auxiliares para la exportación

    private void llenarDatosProceso(Sheet hoja, Proceso proceso) {
        Row fila = hoja.createRow(1);

        fila.createCell(0).setCellValue(proceso.obtenerIdentificador().toString());
        fila.createCell(1).setCellValue(proceso.obtenerTitulo());

        Cell celdaFecha = fila.createCell(2);
        celdaFecha.setCellValue(
                Date.from(proceso.obtenerFechaDeInicio().atZone(ZoneId.systemDefault()).toInstant())
        );
        aplicarEstiloFecha(hoja, celdaFecha);
    }

    private void llenarDatosActividades(Sheet hoja, Proceso proceso) {
        int numeroFila = 1;
        ListaEnlazada<Actividad> actividades = proceso.obtenerlistaDeActividades();

        for (int i = 0; i < actividades.getTamanio(); i++) {
            Actividad actividad = actividades.getElementoEnPosicion(i);
            Row fila = hoja.createRow(numeroFila++);
            llenarFilaActividad(fila, actividad, proceso.obtenerIdentificador());
        }
    }

    private void llenarDatosTareas(Sheet hoja, Proceso proceso) {
        int numeroFila = 1;
        ListaEnlazada<Actividad> actividades = proceso.obtenerlistaDeActividades();

        for (int i = 0; i < actividades.getTamanio(); i++) {
            Actividad actividad = actividades.getElementoEnPosicion(i);
            numeroFila = llenarTareasActividad(hoja, actividad, numeroFila);
        }
    }

    /**
     * Crea los encabezados para la hoja de Proceso.
     * @param hoja Hoja de Excel donde se crearán los encabezados
     */
    private void crearEncabezadosProceso(Sheet hoja) {
        Row filaEncabezado = hoja.createRow(0);
        filaEncabezado.createCell(0).setCellValue("Identificador");
        filaEncabezado.createCell(1).setCellValue("Nombre");
        filaEncabezado.createCell(2).setCellValue("Fecha de Inicio");
    }

    /**
     * Crea los encabezados para la hoja de Actividades.
     * @param hoja Hoja de Excel donde se crearán los encabezados
     */
    private void crearEncabezadosActividades(Sheet hoja) {
        Row filaEncabezado = hoja.createRow(0);
        filaEncabezado.createCell(0).setCellValue("Identificador");
        filaEncabezado.createCell(1).setCellValue("ID Proceso");
        filaEncabezado.createCell(2).setCellValue("Nombre");
        filaEncabezado.createCell(3).setCellValue("Descripción");
        filaEncabezado.createCell(4).setCellValue("Obligatoria");
        filaEncabezado.createCell(5).setCellValue("Fecha de Inicio");
    }

    /**
     * Crea los encabezados para la hoja de Tareas.
     * @param hoja Hoja de Excel donde se crearán los encabezados
     */
    private void crearEncabezadosTareas(Sheet hoja) {
        Row filaEncabezado = hoja.createRow(0);
        filaEncabezado.createCell(0).setCellValue("Identificador");
        filaEncabezado.createCell(1).setCellValue("Nombre Actividad");
        filaEncabezado.createCell(2).setCellValue("Descripción");
        filaEncabezado.createCell(3).setCellValue("Duración");
        filaEncabezado.createCell(4).setCellValue("Obligatoria");
        filaEncabezado.createCell(5).setCellValue("Fecha de Creación");
    }

    /**
     * Importa los procesos desde una hoja de Excel.
     * @param hoja Hoja de Excel con los datos de los procesos
     * @return Lista enlazada con los procesos importados
     */
    private ListaEnlazada<Proceso> importarProcesos(Sheet hoja) {
        ListaEnlazada<Proceso> procesosImportados = new ListaEnlazada<>();
        Iterator<Row> iteradorFilas = hoja.iterator();
        iteradorFilas.next(); // Salta la fila de encabezados

        while (iteradorFilas.hasNext()) {
            Row fila = iteradorFilas.next();
            String id = fila.getCell(0).getStringCellValue();
            String nombre = fila.getCell(1).getStringCellValue();
            Date fechaInicio = fila.getCell(2).getDateCellValue();

            Proceso proceso = new Proceso(nombre);
            proceso.establecerIdentificador(UUID.fromString(id));
            proceso.establecerFechaDeInicio(LocalDateTime.ofInstant(
                    fechaInicio.toInstant(),
                    ZoneId.systemDefault()
            ));

            procesosImportados.insertar(proceso);
        }

        return procesosImportados;
    }

    /**
     * Importa las actividades y las asocia a sus respectivos procesos.
     * @param hoja Hoja de Excel con los datos de las actividades
     * @param procesosImportados Lista de procesos donde se asociarán las actividades
     */
    private void importarActividades(Sheet hoja, ListaEnlazada<Proceso> procesosImportados) {
        if (hoja == null) return;

        Iterator<Row> iteradorFilas = hoja.iterator();
        iteradorFilas.next(); // Salta la fila de encabezados

        while (iteradorFilas.hasNext()) {
            Row fila = iteradorFilas.next();
            String nombre = fila.getCell(1).getStringCellValue();
            String descripcion = fila.getCell(2).getStringCellValue();
            boolean obligatoria = fila.getCell(3).getBooleanCellValue();
            Date fechaInicio = fila.getCell(4).getDateCellValue();

            // Crear nueva actividad usando el constructor proporcionado
            Actividad actividad = new Actividad(nombre, descripcion, obligatoria);
            actividad.asignarFechaInicio(LocalDateTime.ofInstant(
                    fechaInicio.toInstant(),
                    ZoneId.systemDefault()
            ));

            // Busca el proceso correspondiente y asocia la actividad
            String idProceso = fila.getCell(0).getStringCellValue();
            for (int i = 0; i < procesosImportados.getTamanio(); i++) {
                Proceso proceso = procesosImportados.getElementoEnPosicion(i);
                if (proceso.obtenerIdentificador().toString().equals(idProceso)) {
                    proceso.agregarActividad(actividad);
                    break;
                }
            }
        }
    }


    /**
     * Importa las tareas y las asocia a sus respectivas actividades.
     * @param hoja Hoja de Excel con los datos de las tareas
     * @param procesosImportados Lista de procesos donde se buscarán las actividades
     */
    private void importarTareas(Sheet hoja, ListaEnlazada<Proceso> procesosImportados) {
        if (hoja == null) return;

        Iterator<Row> iteradorFilas = hoja.iterator();
        iteradorFilas.next(); // Salta la fila de encabezados

        while (iteradorFilas.hasNext()) {
            Row fila = iteradorFilas.next();
            String nombreActividad = fila.getCell(1).getStringCellValue();
            String descripcion = fila.getCell(2).getStringCellValue();
            int duracion = (int) fila.getCell(3).getNumericCellValue();
            boolean obligatoria = fila.getCell(4).getBooleanCellValue();
            Date fechaCreacion = fila.getCell(5).getDateCellValue();

            // Crear nueva tarea usando el constructor proporcionado
            Tarea tarea = new Tarea(descripcion, duracion, obligatoria);
            tarea.establecerFechaCreacion(LocalDateTime.ofInstant(
                    fechaCreacion.toInstant(),
                    ZoneId.systemDefault()
            ));

            // Busca la actividad correspondiente y asocia la tarea
            buscarYAsociarTarea(procesosImportados, nombreActividad, tarea);
        }
    }

    /**
     * Aplica el estilo de fecha a una celda de Excel.
     * @param hoja Hoja de Excel que contiene la celda
     * @param celda Celda a la que se aplicará el estilo
     */
    private void aplicarEstiloFecha(Sheet hoja, Cell celda) {
        CellStyle estiloFecha = hoja.getWorkbook().createCellStyle();
        DataFormat formatoFecha = hoja.getWorkbook().createDataFormat();
        estiloFecha.setDataFormat(formatoFecha.getFormat("dd/mm/yyyy hh:mm"));
        celda.setCellStyle(estiloFecha);
    }

    /**
     * Llena una fila con los datos de una actividad.
     * @param fila Fila de Excel a llenar
     * @param actividad Actividad cuyos datos se escribirán
     * @param idProceso Identificador del proceso al que pertenece la actividad
     */
    private void llenarFilaActividad(Row fila, Actividad actividad, UUID idProceso) {
        fila.createCell(0).setCellValue(idProceso.toString());
        fila.createCell(1).setCellValue(actividad.obtenerNombre());
        fila.createCell(2).setCellValue(actividad.obtenerDescripcion());
        fila.createCell(3).setCellValue(actividad.esObligatoria());

        Cell celdaFecha = fila.createCell(4);
        celdaFecha.setCellValue(Date.from(actividad.obtenerFechaInicio()
                .atZone(ZoneId.systemDefault()).toInstant()));
        aplicarEstiloFecha(fila.getSheet(), celdaFecha);
    }

    /**
     * Llena las tareas de una actividad en la hoja de Excel.
     * @param hoja Hoja de Excel donde se escribirán las tareas
     * @param actividad Actividad cuyas tareas se escribirán
     * @param numeroFila Número de fila inicial donde empezar a escribir
     * @return Número de la siguiente fila disponible
     */
    private int llenarTareasActividad(Sheet hoja, Actividad actividad, int numeroFila) {
        Cola<Tarea> tareas = actividad.obtenerTareas();
        int tareasRestantes = tareas.obtenerTamano();

        while (tareasRestantes > 0) {
            Tarea tarea = tareas.desencolar();
            Row fila = hoja.createRow(numeroFila++);

            fila.createCell(0).setCellValue(UUID.randomUUID().toString());
            fila.createCell(1).setCellValue(actividad.obtenerNombre());
            fila.createCell(2).setCellValue(tarea.obtenerDescripcion());
            fila.createCell(3).setCellValue(tarea.obtenerDuracion());
            fila.createCell(4).setCellValue(tarea.esObligatoria());

            Cell celdaFecha = fila.createCell(5);
            celdaFecha.setCellValue(Date.from(tarea.obtenerFechaCreacion()
                    .atZone(ZoneId.systemDefault()).toInstant()));
            aplicarEstiloFecha(hoja, celdaFecha);

            // Volvemos a encolar la tarea para no perder los datos
            tareas.encolar(tarea);
            tareasRestantes--;
        }

        return numeroFila;
    }


    /**
     * Método auxiliar para buscar una actividad y asociarle una tarea.
     * @param procesosImportados Lista de procesos donde buscar la actividad
     * @param nombreActividad Nombre de la actividad a buscar
     * @param tarea Tarea a asociar
     */
    private void buscarYAsociarTarea(ListaEnlazada<Proceso> procesosImportados,
                                     String nombreActividad,
                                     Tarea tarea) {
        for (int i = 0; i < procesosImportados.getTamanio(); i++) {
            Proceso proceso = procesosImportados.getElementoEnPosicion(i);
            for (int j = 0; j < proceso.obtenerlistaDeActividades().getTamanio(); j++) {
                Actividad actividad = proceso.obtenerlistaDeActividades().getElementoEnPosicion(j);
                if (actividad.obtenerNombre().equals(nombreActividad)) {
                    actividad.agregarTarea(tarea);
                    return;
                }
            }
        }
    }

    /**
     * Busca un proceso por su identificador único.
     * @param id Identificador único del proceso
     * @return Proceso encontrado o null si no existe
     */
    private Proceso buscarProcesoPorId(UUID id) {
        ListaEnlazada<Proceso> procesos = administradorProcesos.obtenerTodosLosProcesos();
        for (int i = 0; i < procesos.getTamanio(); i++) {
            Proceso proceso = procesos.getElementoEnPosicion(i);
            if (proceso.obtenerIdentificador().equals(id)) {
                return proceso;
            }
        }
        return null;
    }

    /**
     * Actualiza los procesos en el sistema con los datos importados.
     * @param procesosImportados Lista de procesos importados del Excel
     */
    private void actualizarProcesosSistema(ListaEnlazada<Proceso> procesosImportados) {
        for (int i = 0; i < procesosImportados.getTamanio(); i++) {
            Proceso procesoImportado = procesosImportados.getElementoEnPosicion(i);
            administradorProcesos.registrarNuevoProceso(procesoImportado.obtenerTitulo());
        }
    }
}
