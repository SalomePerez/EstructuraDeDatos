package estructura.persistencia;


import estructura.model.*;

import java.io.*;
import java.util.*;

public class Persistencia {
    public static final String RUTA_ARCHIVO_PROCESOS = "src/main/resources/persistencia/procesos.txt";
    public static final String RUTA_ARCHIVO_ACTIVIDADES = "src/main/resources/persistencia/actividades.txt";
    public static final String RUTA_ARCHIVO_TAREAS = "src/main/resources/persistencia/tareas.txt";
    ;
    public static final String RUTA_ARCHIVO_USUARIOS = "src/main/resources/persistencia/usuarios.txt";

    public static void guardarProcesos(List<Proceso> listaProcesos) throws IOException {
        String contenido = "";
        for (Proceso proceso : listaProcesos) {
            contenido += proceso.getId() + "@@" + proceso.getNombre() + "@@" + proceso.getNumActividades() + "\n";
        }
        guardarArchivo(RUTA_ARCHIVO_PROCESOS, contenido);
    }

    public static void guardarActividades(List<Proceso> listaProcesos) throws IOException {
        String contenido = "";
        Iterator<Proceso> iterator = listaProcesos.iterator();
        while (iterator.hasNext()) {
            Proceso procesoActual = iterator.next();
            for (Actividad actividad : procesoActual.getActividades()) {
                contenido += procesoActual.getNombre() + "@@" + actividad.getNombre() + "@@" + actividad.getDescripcion() + "@@" + actividad.isObligatoria() + "\n";
            }
        }
        guardarArchivo(RUTA_ARCHIVO_ACTIVIDADES, contenido);
    }

    public static void guardarTareas(List<Proceso> listaProcesos) throws IOException {
        String contenido = "";

        Iterator<Proceso> iterator = listaProcesos.iterator();
        while (iterator.hasNext()) {
            Proceso procesoActual = iterator.next();
            for (Actividad actividad : procesoActual.getActividades()) {
                for (Tarea tarea : actividad.getTareasPendientes().toList()) {
                    contenido += actividad.getProceso() + "@@" + actividad.getNombre() + "@@" + tarea.getDescripcion() + "@@" + tarea.getEstado() + "@@" + tarea.getDuracionMinutos() + "\n";
                }
            }
        }
        guardarArchivo(RUTA_ARCHIVO_TAREAS, contenido);
    }

    public static void guardarArchivo(String ruta, String contenido) throws IOException {
        FileWriter fw = new FileWriter(ruta, false); //false para no append
        BufferedWriter bfw = new BufferedWriter(fw);
        bfw.write(contenido);
        bfw.close();
        fw.close();
    }

    public static void guardarUsuarios(List<Usuario> listaUsuarios) throws IOException {
        String contenido = "";
        for (Usuario usuario : listaUsuarios) {
            contenido += usuario.getRol() + "@@" + usuario.getUser() + "@@" + usuario.getPassword() + "\n";
        }
        guardarArchivo(RUTA_ARCHIVO_USUARIOS, contenido);
    }

    public static List<Usuario> cargarUsuarios() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        ArrayList<String> contenido = leerArchivo(RUTA_ARCHIVO_USUARIOS);

        for (String linea : contenido) {
            if (!linea.isEmpty()) {
                String[] partes = linea.split("@@");
                Usuario usuario = new Usuario();
                usuario.setRol(partes[0]);
                usuario.setUser(partes[1]);
                usuario.setPassword(partes[2]);
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    public static ListaDobleEnlazada<Proceso> cargarProceso() throws IOException {
        ListaDobleEnlazada<Proceso> procesos = new ListaDobleEnlazada<>();
        ArrayList<String> contenido = leerArchivo(RUTA_ARCHIVO_PROCESOS);
        String linea = "";
        for (int i = 0; i < contenido.size(); i++) {
            linea = contenido.get(i);
            try {
                String[] partes = linea.split("@@");
                Proceso proceso = new Proceso();
                proceso.setId(partes[0]);
                proceso.setNombre(partes[1]);
                proceso.setNumActividades(Integer.parseInt(partes[2]));
                procesos.agregarUltimo(proceso);
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir a entero: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return procesos;
    }

    public static ListaDobleEnlazada<Actividad> cargarActividades() throws IOException {
        ListaDobleEnlazada<Actividad> actividades = new ListaDobleEnlazada<>();
        ArrayList<String> contenido = leerArchivo(RUTA_ARCHIVO_ACTIVIDADES);
        String linea = "";
        for (int i = 0; i < contenido.size(); i++) {
            linea = contenido.get(i);
            Actividad actividad = new Actividad();
            String[] partes = linea.split("@@");
            actividad.setProceso(partes[0]);
            actividad.setNombre(partes[1]);
            actividad.setDescripcion(partes[2]);
            actividad.setObligatoria((partes[3]));
            actividades.agregarUltimo(actividad);
        }
        return actividades;
    }

    public static ListaDobleEnlazada<Tarea> cargarTareas() throws IOException {
        ListaDobleEnlazada<Tarea> tareas = new ListaDobleEnlazada<>();
        ArrayList<String> contenido = leerArchivo(RUTA_ARCHIVO_TAREAS);
        String linea = "";
        for (int i = 0; i < contenido.size(); i++) {
            linea = contenido.get(i);
            Tarea tarea = new Tarea();
            String[] partes = linea.split("@@");
            tarea.setProceso(partes[0]);
            tarea.setActividad(partes[1]);
            tarea.setDescripcion(partes[2]);
            tarea.setEstado(Estado.valueOf(partes[3]));
            tarea.setDuracionMinutos(Integer.parseInt(partes[4]));
            tareas.agregarUltimo(tarea);
        }
        return tareas;
    }

    public static ArrayList<String> leerArchivo(String ruta) throws IOException {
        ArrayList<String> contenido = new ArrayList<String>();
        FileReader fr = new FileReader(ruta);
        BufferedReader bfr = new BufferedReader(fr);
        String linea = "";

        while ((linea = bfr.readLine()) != null)
            contenido.add(linea);

        bfr.close();
        fr.close();
        return contenido;
    }

    public static void cargarDatos(Aplicacion aplicacion) throws IOException {
        ListaDobleEnlazada<Proceso> procesos = cargarProceso();
        ListaDobleEnlazada<Actividad> actividades = cargarActividades();
        ListaDobleEnlazada<Tarea> tareas = cargarTareas();
        List<Usuario> usuarios = cargarUsuarios();

        Iterator<Proceso> procesoIterator = procesos.iterator();
        while (procesoIterator.hasNext()) {
            Proceso procesoActual = procesoIterator.next();
            Iterator<Actividad> actividadIterator = actividades.iterator();
            while (actividadIterator.hasNext()) {
                Actividad actividadActual = actividadIterator.next();
                if (procesoActual.getNombre().equals(actividadActual.getProceso())) {
                    procesoActual.getActividades().agregarUltimo(actividadActual);
                    Iterator<Tarea> tareaIterator = tareas.iterator();
                    while (tareaIterator.hasNext()) {
                        Tarea tareaActual = tareaIterator.next();
                        if (actividadActual.getNombre().equals(tareaActual.getActividad())) {
                            actividadActual.getTareasPendientes().enqueue(tareaActual);
                        }
                    }
                }
            }
        }
        aplicacion.setListaUsuarios(usuarios);
        aplicacion.setListaProcesos(procesos);
    }
}


