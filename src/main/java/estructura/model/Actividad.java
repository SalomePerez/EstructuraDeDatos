package estructura.model;

import javafx.scene.control.CheckBox;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
public class Actividad implements Serializable {
    private String nombre;
    private String descripcion;
    private String obligatoria;
    private String proceso;
    private String tareas;
    private String tiempoMinimo;
    private String tiempoMaximo;
    private Cola<Tarea> tareasPendientes = new Cola<>(); // Cola de tareas pendientes
    private Cola<Tarea> completarTarea = new Cola<>(); // Cola de tareas completadas

    public Actividad(String nombre,String descripcion,String obligatoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.obligatoria = obligatoria;
        this.tareasPendientes = new Cola<>();
        this.completarTarea = new Cola<>();
    }
    public Actividad(){
        super();
    }

    public void addTarea(Tarea tarea) {
        tareasPendientes.enqueue(tarea);
    }

    public String getObligatoria() {
        return obligatoria;
    }

    public String getTareas() {
        String tareas = "";
        for(Tarea tarea: getTareasPendientes().toList()) {
            tareas += tarea.getDescripcion()+", ";
        }
        return tareas;
    }

    public void setTareas(String tareas) {
        this.tareas = tareas;
    }

    public String getTiempoMinimo() {
        int tiempo = 0;
        for(Tarea tarea: getTareasPendientes().toList()) {
            Estado e = tarea.getEstado();
            if(e.equals(Estado.OBLIGATORIO))
                tiempo += tarea.getDuracionMinutos();
        }
        return tiempo+"";
    }

    public void setTiempoMinimo(String tiempoMinimo) {
        this.tiempoMinimo = tiempoMinimo;
    }

    public String getTiempoMaximo() {
        int tiempo = 0;
        for(Tarea tarea: getTareasPendientes().toList()) {
            tiempo += tarea.getDuracionMinutos();
        }
        return tiempo+"";
    }

    public void setTiempoMaximo(String tiempoMaximo) {
        this.tiempoMaximo = tiempoMaximo;
    }

    public Tarea completetarTarea() {
        Tarea completetarTarea = tareasPendientes.dequeue();
        if (completetarTarea != null) {
            this.completarTarea.enqueue(completetarTarea);
            completetarTarea.completarTarea();
        }
        return completetarTarea;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public Cola<Tarea> getTareasPendientes() {
        return tareasPendientes;
    }

    public Cola<Tarea> getCompletarTarea() {
        return completarTarea;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Tarea> getPendingTasksAsList() {
        return tareasPendientes.toList();
    }

    public List<Tarea> getCompletedTasksAsList(){
            return completarTarea.toList();
    }

    public int getTotalDuracionMinutes() {
        int totalDuration = 0;
        for (Tarea tarea : tareasPendientes.toList()) totalDuration += tarea.getDuracionMinutos();

        for (Tarea tarea : completarTarea.toList()) totalDuration += tarea.getDuracionMinutos();

        return totalDuration;
    }
    public String calcularEstado(CheckBox estado){
        if(estado.isSelected()){
            return "OBLIGATORIO";
        }
        else{
            return "NO OBLIGATORIO";
        }
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String isObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(String obligatoria) {
        this.obligatoria = obligatoria;
    }

    public void setTareasPendientes(Cola<Tarea> tareasPendientes) {
        this.tareasPendientes = tareasPendientes;
    }

    public void setCompletarTarea(Cola<Tarea> completarTarea) {
        this.completarTarea = completarTarea;
    }

}
