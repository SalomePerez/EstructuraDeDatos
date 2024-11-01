package estructura.controller;

import estructura.exceptions.ProcesoExisteException;
import estructura.model.*;
import estructura.persistencia.Persistencia;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ModelFactory {
    Aplicacion aplicacion;

    public void eliminarActividad(Proceso procesoSeleccionado, Actividad actividadSeleccionada){
        try {
            getAplicacion().eliminarActividad(procesoSeleccionado, actividadSeleccionada);
            Persistencia.guardarProcesos(getAplicacion().getListaProcesos());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validarUsuario(Usuario usuario) {
        try {
            List<Usuario> usuarios = Persistencia.cargarUsuarios();
            return getAplicacion().validarUsario(usuario, usuarios);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registrarUsuario(Usuario user) {
        try {
            getAplicacion().getListaUsuarios().add(user);
            Persistencia.guardarUsuarios(getAplicacion().getListaUsuarios());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static class SingletonHolder {
        private final static ModelFactory eINSTANCE = new ModelFactory();
    }

    public static ModelFactory getInstance() {
        return SingletonHolder.eINSTANCE;
    }

    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    public ModelFactory() {
        cargarDatos();
    }

    public void cargarDatos() {
        aplicacion = new Aplicacion();
        try {
            Persistencia.cargarDatos(aplicacion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Proceso crearProceso(Proceso proceso) {
        Proceso nuevoProceso = null;
        try {
            nuevoProceso = getAplicacion().crearProceso(proceso);
            Persistencia.guardarProcesos(getAplicacion().getListaProcesos());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ProcesoExisteException e) {
            mostrarMensaje("El proceso ya existe");
            throw new RuntimeException(e);
        }
        return nuevoProceso;
    }

    public Actividad crearActividadFinal(Proceso proceso, Actividad actividad) {
        Actividad nuevaActividad = null;
        try {
            nuevaActividad = getAplicacion().crearActividadFinal(proceso, actividad);
            Persistencia.guardarActividades(getAplicacion().getListaProcesos());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return nuevaActividad;
    }

    private List<Actividad> devolverTodasActividades() {
        List<Actividad> actividades = new ArrayList<>();
        Iterator<Proceso> procesoIterator = getAplicacion().getListaProcesos().iterator();
        while (procesoIterator.hasNext()) {
            Proceso procesoActual = procesoIterator.next();
            actividades.addAll(procesoActual.getActividades().aLista());
        }
        return actividades;
    }

    public Actividad crearActividadDespuesDe(Proceso proceso, Actividad actividadAnterior, Actividad nueva) {
        Actividad nuevaActividad = null;
        try {
            nuevaActividad = getAplicacion().crearActividadDespuesDe(proceso, actividadAnterior, nueva);
            Persistencia.guardarActividades(getAplicacion().getListaProcesos());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return nuevaActividad;
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public Tarea crearTareaFinal(Proceso procesoSeleccionado, Actividad actividadSeleccionada, Tarea tarea) {
        Tarea nuevaTarea = null;
        try {
            nuevaTarea = getAplicacion().crearTareaFinal(procesoSeleccionado, actividadSeleccionada, tarea);
            Persistencia.guardarTareas(getAplicacion().getListaProcesos());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return nuevaTarea;
    }


    public Tarea crearTareaPosicion(Proceso procesoSeleccionado, Actividad actividadSeleccionada, Tarea tarea, int posicion) {
        Tarea nuevaTarea = null;
        try {
            nuevaTarea = getAplicacion().crearTareaPosicion(procesoSeleccionado, actividadSeleccionada, tarea, posicion);
            Persistencia.guardarTareas(getAplicacion().getListaProcesos());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return nuevaTarea;
    }
}