package estructura.model;


import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;

/**
 * Clase que representa un proceso en la aplicación.
 */
public class Proceso implements Serializable {
    private String id;
    private String nombre;
    private int numActividades;
    private ListaDobleEnlazada<Actividad> listaActividades = new ListaDobleEnlazada<>();
    private static ListaDobleEnlazada<Integer> ids = new ListaDobleEnlazada<>();
    private int duracionTotal;

    public Proceso(String nombre) {
        this.id = String.valueOf(generarID());
        this.nombre = nombre;
        this.listaActividades = new ListaDobleEnlazada<>();
        this.duracionTotal = 0;
    }

    public Proceso() {
        super();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    private void actualizarDuracion(int totalDurationMinutes) {
        this.duracionTotal += totalDurationMinutes;
    }

    public int getDuracionTotal() {
        return this.duracionTotal;
    }

    public ListaDobleEnlazada<Actividad> getActividades() {
        return listaActividades;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setActividades(ListaDobleEnlazada<Actividad> actividades) {
        this.listaActividades = actividades;
    }

    public void setDuracionTotal(int duracionTotal) {
        this.duracionTotal = duracionTotal;
    }

    public int getNumActividades() {
        return getActividades().getSize();
    }

    public void setNumActividades(int numActividades) {
        this.numActividades = numActividades;
    }

    public static int generarID() {
        Random random = new Random();
        int nuevoID;
        do {
            nuevoID = random.nextInt(100) + 1;
        } while (idExisteEnLista(nuevoID));
        return nuevoID;
    }

    private static boolean idExisteEnLista(int id) {
        // Utiliza el iterador para recorrer la lista de IDs y verifica si el ID ya existe
        for (Integer existingID : ids) {
            if (existingID == id) {
                return true; // El ID ya existe en la lista
            }
        }
        return false; // El ID no existe en la lista
    }

    public int getTiempoTotalProceso() {
        int tiempoTotalProceso = 0;
        for (Actividad actividad : listaActividades) {
            tiempoTotalProceso += Integer.parseInt(actividad.getTiempoMaximo());
        }
        return tiempoTotalProceso;
    }

    public void incrementarNumActividades() {
        this.numActividades++;
    }

    public void decrementarNumActividades() {
        this.numActividades--;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return this == o;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + numActividades;
        result = 31 * result + (listaActividades != null ? listaActividades.hashCode() : 0);
        result = 31 * result + duracionTotal;
        return result;
    }
}
