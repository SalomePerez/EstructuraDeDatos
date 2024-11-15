package edu.co.uniquindio.EstructuraDeDatos;

/**
 * Clase que implementa una lista enlazada simple.
 *
 * @param <T> El tipo de dato almacenado en la lista.
 */
public class ListaEnlazada<T> {
    private Nodo<T> cabeza; // Primer nodo de la lista
    private int tamanio;     // Tamaño de la lista

    /**
     * Constructor que inicializa una lista vacía.
     */
    public ListaEnlazada() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    /**
     * Método para agregar un elemento al final de la lista.
     *
     * @param dato El elemento a agregar.
     */
    public void agregar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamanio++;
    }

    /**
     * Método para eliminar un elemento de la lista.
     *
     * @param dato El elemento a eliminar.
     * @return true si el elemento se eliminó, false si no se encontró.
     */
    public boolean eliminar(T dato) {
        if (cabeza == null) {
            return false;
        }

        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            tamanio--;
            return true;
        }

        Nodo<T> actual = cabeza;
        while (actual.siguiente != null && !actual.siguiente.dato.equals(dato)) {
            actual = actual.siguiente;
        }

        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
            tamanio--;
            return true;
        }

        return false;
    }

    /**
     * Método para buscar un elemento en la lista.
     *
     * @param dato El elemento a buscar.
     * @return true si el elemento se encuentra en la lista, false en caso contrario.
     */
    public boolean contiene(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(dato)) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Método para obtener el tamaño de la lista.
     *
     * @return El número de elementos en la lista.
     */
    public int obtenerTamanio() {
        return tamanio;
    }

    /**
     * Método para imprimir los elementos de la lista.
     */
    public void imprimir() {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            System.out.print(actual.dato + " -> ");
            actual = actual.siguiente;
        }
        System.out.println("null");
    }
}