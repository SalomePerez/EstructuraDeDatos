package edu.co.uniquindio.EstructuraDeDatos;

/**
 * Clase que representa un nodo genérico.
 *
 * @param <T> El tipo de dato que almacena el nodo.
 */
public class Nodo<T> {
    public T dato; // Dato almacenado en el nodo
    public Nodo<T> siguiente; // Referencia al siguiente nodo

    /**
     * Constructor para crear un nodo con un dato.
     *
     * @param dato El dato a almacenar en el nodo.
     */
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}