package edu.co.uniquindio.Pruebas;

import edu.co.uniquindio.Model.Administradores.AdministradorProcesos;
import edu.co.uniquindio.Model.Auxiliares.GestorDatosExcel;
import edu.co.uniquindio.Model.Principales.Proceso;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;

public class GestorDeDatosTest {

    private AdministradorProcesos administradorProcesos;
    private GestorDatosExcel gestorDatosExcel;
    private File archivoTemporal;

    @Before
    public void setUp() {
        administradorProcesos = new AdministradorProcesos();
        gestorDatosExcel = new GestorDatosExcel(administradorProcesos);

        // Crear un archivo temporal para pruebas
        try {
            archivoTemporal = File.createTempFile("test_data", ".xlsx");
        } catch (IOException e) {
            fail("No se pudo crear el archivo temporal para pruebas");
        }
    }

    @After
    public void tearDown() {
        // Eliminar el archivo temporal al finalizar
        if (archivoTemporal.exists()) {
            archivoTemporal.delete();
        }
    }

    @Test
    public void testExportarProceso() throws IOException {
        // Configuración de datos de prueba
        Proceso proceso = new Proceso("Proceso de prueba");
        UUID idProceso = UUID.randomUUID();
        proceso.establecerIdentificador(idProceso);
        proceso.establecerFechaDeInicio(LocalDateTime.now());

        // Registrar el nuevo proceso
        administradorProcesos.registrarNuevoProceso(proceso.getNombre()); // Cambiado a pasar el objeto directamente

        // Ejecutar el método de exportación
        gestorDatosExcel.exportarProceso(archivoTemporal.getAbsolutePath(), idProceso);

        // Verificar que el archivo Excel se haya generado correctamente
        assertTrue("El archivo no fue generado", archivoTemporal.exists());
        assertTrue("El archivo Excel está vacío", archivoTemporal.length() > 0);

        // Verificar el contenido del archivo (esto depende de cómo implementes la lectura del Excel)
        try (FileInputStream fis = new FileInputStream(archivoTemporal);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            // Asumimos que hay al menos una hoja de trabajo y la primera es la que contiene los procesos
            XSSFSheet sheet = workbook.getSheetAt(0);
            assertNotNull("La hoja del archivo está vacía", sheet);

            // Verificar si hay datos en la primera fila
            XSSFRow row = sheet.getRow(0); // Primer fila (encabezados)
            assertNotNull("La primera fila está vacía", row);
            assertTrue("No hay suficientes celdas en la fila", row.getPhysicalNumberOfCells() > 0);

            // Aquí deberías tener lógica que verifique que los valores en las celdas coincidan con los del proceso registrado
            String valorIdProceso = row.getCell(0).getStringCellValue(); // Ajusta según el formato de la celda
            assertEquals("El ID del proceso exportado no coincide", idProceso.toString(), valorIdProceso);

            String valorNombreProceso = row.getCell(1).getStringCellValue(); // Asumimos que es la segunda columna
            assertEquals("El nombre del proceso exportado no coincide", proceso.getNombre(), valorNombreProceso);

            // Aquí puedes agregar más validaciones según las propiedades del proceso que exportas.
        } catch (IOException e) {
            fail("Error al leer el archivo Excel: " + e.getMessage());
        }
    }




    @Test
    public void testImportarDatos() throws IOException {
        // Asegurar que hay al menos un proceso para exportar
        if (administradorProcesos.obtenerTodosLosProcesos().getTamanio() == 0) {
            Proceso procesoNuevo = new Proceso("Proceso de prueba");
            procesoNuevo.establecerIdentificador(UUID.randomUUID());
            procesoNuevo.establecerFechaDeInicio(LocalDateTime.now());
            administradorProcesos.registrarNuevoProceso(String.valueOf(procesoNuevo));
        }

        // Exportar el primer proceso
        Proceso procesoExportado = administradorProcesos.obtenerTodosLosProcesos().getElementoEnPosicion(0);

        // Verificar que el proceso no sea nulo antes de continuar con la exportación
        if (procesoExportado != null) {
            // Exportar el proceso
            gestorDatosExcel.exportarProceso(archivoTemporal.getAbsolutePath(), procesoExportado.obtenerIdentificador());

            // Limpiar el administrador de procesos antes de importar
            administradorProcesos.eliminarProcesoPorId(procesoExportado.obtenerIdentificador());

            // Importar los datos desde el archivo Excel
            gestorDatosExcel.importarDatos(archivoTemporal.getAbsolutePath());

            // Verificar que los datos se hayan importado correctamente
            assertFalse("No se importaron procesos", administradorProcesos.obtenerTodosLosProcesos().estaVacia());
            assertEquals("Cantidad incorrecta de procesos importados", 1, administradorProcesos.obtenerTodosLosProcesos().getTamanio());
        } else {
            fail("No hay procesos para exportar");
        }
    }

}
