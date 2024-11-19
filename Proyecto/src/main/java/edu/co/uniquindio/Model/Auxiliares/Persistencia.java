package edu.co.uniquindio.Model.Auxiliares;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import edu.co.uniquindio.Model.EstructuraDeDatos.Cola;
import edu.co.uniquindio.Model.EstructuraDeDatos.ListaEnlazada;
import edu.co.uniquindio.Model.Principales.Actividad;
import edu.co.uniquindio.Model.Principales.Proceso;
import edu.co.uniquindio.Model.Principales.Tarea;
import org.w3c.dom.*;

public class Persistencia {

    private static final String RUTA_ARCHIVO = "src/main/resources/edu/co/uniquindio/Application/files/procesos.xml";

    public static void guardarProceso(Proceso proceso) {
        try {
            File archivo = new File(RUTA_ARCHIVO);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;

            // Si el archivo existe, cargarlo; de lo contrario, crear uno nuevo
            if (archivo.exists()) {
                doc = builder.parse(archivo);
            } else {
                doc = builder.newDocument();
                Element root = doc.createElement("Procesos");
                doc.appendChild(root);
            }

            // Obtener el nodo raíz
            Element root = doc.getDocumentElement();

            // Crear nodo Proceso
            Element nodoProceso = doc.createElement("Proceso");
            nodoProceso.setAttribute("nombre", proceso.obtenerTitulo());

            // Añadir las actividades
            for (int i = 0; i < proceso.obtenerlistaDeActividades().getTamanio(); i++) {
                Actividad actividad = proceso.obtenerlistaDeActividades().getElementoEnPosicion(i);

                Element nodoActividad = doc.createElement("Actividad");
                nodoActividad.setAttribute("nombre", actividad.obtenerNombre());
                nodoActividad.setAttribute("obligatoria", String.valueOf(actividad.esObligatoria()));

                // Descripción de la actividad
                Element descripcionActividad = doc.createElement("Descripcion");
                descripcionActividad.setTextContent(actividad.obtenerDescripcion());
                nodoActividad.appendChild(descripcionActividad);

                // Tareas
                Element nodoTareas = doc.createElement("Tareas");
                Cola<Tarea> tareas = actividad.obtenerTareas();
                for (Tarea tarea : tareas) {
                    Element nodoTarea = doc.createElement("Tarea");
                    nodoTarea.setAttribute("nombre", tarea.obtenerNombre());
                    nodoTarea.setAttribute("obligatoria", String.valueOf(tarea.esObligatoria()));
                    nodoTarea.setAttribute("estado", tarea.estaCompletada() ? "Completada" : "Pendiente");

                    // Descripción de la tarea
                    Element descripcionTarea = doc.createElement("Descripcion");
                    descripcionTarea.setTextContent(tarea.obtenerDescripcion());
                    nodoTarea.appendChild(descripcionTarea);

                    // Duración de la tarea
                    Element duracionTarea = doc.createElement("Duracion");
                    duracionTarea.setTextContent(String.valueOf(tarea.obtenerDuracion()));
                    nodoTarea.appendChild(duracionTarea);

                    nodoTareas.appendChild(nodoTarea);
                }

                nodoActividad.appendChild(nodoTareas);
                nodoProceso.appendChild(nodoActividad);
            }

            root.appendChild(nodoProceso);

            // Guardar el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(archivo);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ListaEnlazada<Proceso> cargarProcesos() {
        ListaEnlazada<Proceso> procesos = new ListaEnlazada<>();
        try {
            File archivo = new File(RUTA_ARCHIVO);
            if (!archivo.exists()) {
                return procesos;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivo);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            Element procesoActual = getFirstChildElement(root);

            while (procesoActual != null) {
                if (procesoActual.getTagName().equals("Proceso")) {
                    String nombreProceso = procesoActual.getAttribute("nombre");
                    Proceso proceso = new Proceso(nombreProceso);

                    // Procesar actividades
                    Element actividadActual = getFirstChildElement(procesoActual);
                    while (actividadActual != null) {
                        if (actividadActual.getTagName().equals("Actividad")) {
                            String nombreActividad = actividadActual.getAttribute("nombre");
                            boolean obligatoria = Boolean.parseBoolean(actividadActual.getAttribute("obligatoria"));

                            Element descripcionElement = getFirstChildElement(actividadActual);
                            String descripcion = "";
                            if (descripcionElement != null && descripcionElement.getTagName().equals("Descripcion")) {
                                descripcion = descripcionElement.getTextContent();
                            }

                            Actividad actividad = new Actividad(nombreActividad, descripcion, obligatoria);

                            // Procesar tareas
                            Element tareasElement = getNextSiblingElement(descripcionElement);
                            if (tareasElement != null && tareasElement.getTagName().equals("Tareas")) {
                                Element tareaActual = getFirstChildElement(tareasElement);
                                while (tareaActual != null) {
                                    if (tareaActual.getTagName().equals("Tarea")) {
                                        String nombreTarea = tareaActual.getAttribute("nombre");
                                        boolean tareaObligatoria = Boolean.parseBoolean(tareaActual.getAttribute("obligatoria"));

                                        Element descripcionTareaElement = getFirstChildElement(tareaActual);
                                        String descripcionTarea = "";
                                        if (descripcionTareaElement != null) {
                                            descripcionTarea = descripcionTareaElement.getTextContent();
                                        }

                                        Element duracionElement = getNextSiblingElement(descripcionTareaElement);
                                        int duracion = 0;
                                        if (duracionElement != null) {
                                            duracion = Integer.parseInt(duracionElement.getTextContent());
                                        }

                                        Tarea tarea = new Tarea(descripcionTarea, duracion, tareaObligatoria);
                                        if ("Completada".equals(tareaActual.getAttribute("estado"))) {
                                            tarea.establecerCompletada(true);
                                        }
                                        actividad.agregarTarea(tarea);
                                    }
                                    tareaActual = getNextSiblingElement(tareaActual);
                                }
                            }
                            proceso.agregarActividad(actividad);
                        }
                        actividadActual = getNextSiblingElement(actividadActual);
                    }
                    procesos.insertar(proceso);
                }
                procesoActual = getNextSiblingElement(procesoActual);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return procesos;
    }

    // Métodos auxiliares para navegación DOM
    private static Element getFirstChildElement(Element parent) {
        Node node = parent.getFirstChild();
        while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
            node = node.getNextSibling();
        }
        return (Element) node;
    }

    private static Element getNextSiblingElement(Element element) {
        if (element == null) return null;
        Node node = element.getNextSibling();
        while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
            node = node.getNextSibling();
        }
        return (Element) node;

    }
}
