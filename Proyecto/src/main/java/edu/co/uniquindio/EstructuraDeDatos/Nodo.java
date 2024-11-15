package edu.co.uniquindio.EstructuraDeDatos;

/**
 * Clase que representa un nodo genérico.
 *
 * @param <T> El tipo de dato que almacena el nodo.
 */
public class Nodo<T> {
    public T dato; // Dato almacenado en el nodo
    public Nodo<T> siguiente; // Referencia al siguiente nodo

    //Constructor
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    //Get y Set
    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

}