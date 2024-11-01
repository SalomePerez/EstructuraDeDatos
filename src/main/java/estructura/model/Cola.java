package estructura.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación personalizada de una estructura de datos cola (FIFO - First In, First Out).
 * Esta clase está diseñada para almacenar elementos de cualquier tipo genérico {@code E}.
 *
 * @param <E> El tipo de elementos almacenados en la cola.
 */
public class Cola<E> implements Serializable {

    private Node<E> inicio; // Referencia al inicio de la cola
    private Node<E> fin;    // Referencia al final de la cola
    private int size;

    /**
     * Clase interna estática que representa un nodo en la cola.
     */
    private static class Node<E> {
        E elemento;
        Node<E> siguiente;

        /**
         * Constructor para crear un nuevo nodo.
         *
         * @param elemento El elemento que se almacenará en el nodo.
         * @param siguiente El siguiente nodo en la cola.
         */
        Node(E elemento, Node<E> siguiente) {
            this.elemento = elemento;
            this.siguiente = siguiente;
        }
    }

    /**
     * Constructor para crear una instancia de Cola.
     * Inicializa la cola con inicio y fin como nulos y tamaño como 0.
     */
    public Cola() {
        inicio = null;
        fin = null;
        size = 0;
    }

    /**
     * Comprueba si la cola está vacía.
     *
     * @return {@code true} si la cola está vacía, {@code false} en caso contrario.
     */
    public boolean isEmpty() {
        return inicio == null;
    }
    /**
     * Agrega un nuevo elemento al final de la cola.
     *
     * @param elemento El elemento que se va a agregar.
     */
    public void enqueue(E elemento) {
        Node<E> nuevoNodo = new Node<>(elemento, null);
        if (fin == null) {
            inicio = fin = nuevoNodo;
        } else {
            fin.siguiente = nuevoNodo;
            fin = nuevoNodo;
        }
        size++;
    }

    /**
     * Elimina y devuelve el elemento en el frente de la cola.
     *
     * @return El elemento en el frente de la cola, o {@code null} si la cola está vacía.
     */
    public E dequeue() {
        if (isEmpty()) {
            return null;
        } else {
            E elemento = inicio.elemento;
            inicio = inicio.siguiente;
            if (inicio == null) {
                fin = null;
            }
            size--;
            return elemento;
        }
    }

    /**
     * Obtiene, pero no elimina, el inicio de esta cola, o devuelve {@code null} si esta cola está vacía.
     *
     * @return El inicio de la cola, o {@code null} si la cola está vacía.
     */
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return inicio.elemento;
    }

    public List<E> toList() {
        List<E> lista = new ArrayList<>();
        Node<E> actual = inicio;
        while (actual != null) {
            lista.add(actual.elemento);
            actual = actual.siguiente;
        }
        return lista;
    }

    public void enqueueAtPosition(E elemento, int posicion) {
        Node<E> nuevoNodo = new Node<>(elemento, null);
        if (posicion < 0 || posicion > size) {
            throw new IllegalArgumentException("Posición inválida");
        }
        if (posicion == 0) {
            nuevoNodo.siguiente = inicio;
            inicio = nuevoNodo;
            if (fin == null) {
                fin = nuevoNodo;
            }
        }
        if(posicion == size) {
            fin.siguiente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            Node<E> actual = inicio;
            for (int i = 0; i < posicion - 1; i++) {
                actual = actual.siguiente;
            }
            nuevoNodo.siguiente = actual.siguiente;
            actual.siguiente = nuevoNodo;
        }
        size++;
    }
}
