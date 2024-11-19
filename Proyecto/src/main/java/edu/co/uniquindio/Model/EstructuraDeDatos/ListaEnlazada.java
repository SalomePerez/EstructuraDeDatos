package edu.co.uniquindio.Model.EstructuraDeDatos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase que implementa una lista enlazada simple.
 *
 * @param <T> El tipo de dato almacenado en la lista.
 */
public class ListaEnlazada<T> implements Iterable<T> {
    private Nodo<T> cabeza; // Nodo inicial de la lista
    private int tamanio = 0; // Tamaño de la lista

    // Método nuevo para insertar al inicio de la lista
    public void insertarAlInicio(T elemento) {
        if (!contiene(elemento)) {
            Nodo<T> nuevoNodo = new Nodo<>(elemento);
            nuevoNodo.setSiguiente(cabeza);
            cabeza = nuevoNodo;
            tamanio++;
        } else {
            System.out.println("El elemento ya está en la lista. No se añadirá.");
        }
    }

    // Método modificado para insertar después de un elemento
    public void insertarDespuesDe(T elementoExistente, T nuevoElemento) {
        if (!contiene(nuevoElemento)) {
            Nodo<T> actual = cabeza;
            // Busca el nodo con el elemento existente
            while (actual != null) {
                if (actual.getDato() == elementoExistente) { // Comparación por referencia
                    Nodo<T> nuevoNodo = new Nodo<>(nuevoElemento);
                    nuevoNodo.setSiguiente(actual.getSiguiente());
                    actual.setSiguiente(nuevoNodo);
                    tamanio++;
                    return;
                }
                actual = actual.getSiguiente();
            }
            System.out.println("El elemento existente no se encuentra en la lista.");
        } else {
            System.out.println("El elemento ya está en la lista. No se añadirá.");
        }
    }

    // Método modificado para insertar al final
    public void insertar(T elemento) {
        if (!contiene(elemento)) {
            Nodo<T> nuevoNodo = new Nodo<>(elemento);
            if (cabeza == null) {
                cabeza = nuevoNodo;
            } else {
                Nodo<T> actual = cabeza;
                while (actual.getSiguiente() != null) {
                    actual = actual.getSiguiente();
                }
                actual.setSiguiente(nuevoNodo);
            }
            tamanio++;
        } else {
            System.out.println("El elemento ya está en la lista. No se añadirá.");
        }
    }

    // Método modificado para verificar si contiene un elemento
    public boolean contiene(T elemento) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato() == elemento) { // Comparación por referencia
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }


    public int getTamanio() {
        return tamanio;
    }

    // Método para verificar si la lista está vacía
    public boolean estaVacia() {
        return cabeza == null; // La lista está vacía si la cabeza es null
    }

    // Método para eliminar un elemento de la lista
    public void eliminar(T elemento) {
        if (cabeza == null) {
            return; // Si la lista está vacía, no hace nada
        }
        // Si el elemento a eliminar está en la cabeza de la lista
        if (cabeza.getDato().equals(elemento)) {
            cabeza = cabeza.getSiguiente(); // Mueve la cabeza al siguiente nodo
            tamanio--; // Disminuye el tamaño de la lista
            return;
        }
        Nodo<T> actual = cabeza;
        // Busca el elemento en la lista
        while (actual.getSiguiente() != null && !actual.getSiguiente().getDato().equals(elemento)) {
            actual = actual.getSiguiente();
        }
        // Si encuentra el elemento, lo elimina
        if (actual.getSiguiente() != null) {
            actual.setSiguiente(actual.getSiguiente().getSiguiente()); // Salta el nodo a eliminar
            tamanio--; // Disminuye el tamaño de la lista
        }
    }

    // Método para obtener el elemento en una posición específica de la lista
    public T getElementoEnPosicion(int posicion) {
        // Si la posición es inválida, lanza una excepción
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }
        Nodo<T> actual = cabeza;
        // Recorre la lista hasta la posición indicada
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato(); // Retorna el valor en esa posición
    }

    // Método para obtener el índice de un elemento
    public int indiceDe(T dato) {
        Nodo<T> actual = cabeza;
        int indice = 0;
        // Busca el elemento y devuelve su índice
        while (actual != null && !actual.getDato().equals(dato)) {
             actual = actual.getSiguiente();
            indice++;
        }
        return actual != null ? indice : -1; // Si lo encuentra, devuelve el índice; si no, -1
    }


    // Método para eliminar un elemento en una posición específica
    public void eliminarEn(int indice) {
        // Si el índice es inválido, lanza una excepción
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        // Si el índice es 0, elimina la cabeza
        if (indice == 0) {
            cabeza = cabeza.getSiguiente();
            tamanio--; // Disminuye el tamaño de la lista
            return;
        }
        Nodo<T> actual = cabeza;
        // Recorre la lista hasta el nodo antes del índice a eliminar
        for (int i = 0; i < indice - 1; i++) {
            actual = actual.getSiguiente();
        }
        // Elimina el nodo en la posición indicada
        actual.setSiguiente(actual.getSiguiente().getSiguiente());
        tamanio--; // Disminuye el tamaño de la lista
    }

    // Getter para obtener la cabeza de la lista
    public Nodo<T> getCabeza() {
        return cabeza;
    }

    public void setCabeza(Nodo<T> cabeza) {
        this.cabeza = cabeza;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorLista();
    }

    /**
     * Clase interna que implementa el iterador para la lista enlazada.
     */
    private class IteradorLista implements Iterator<T> {
        private Nodo<T> actual = cabeza;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (actual == null) {
                throw new NoSuchElementException("No hay más elementos en la lista.");
            }
            T dato = actual.getDato();
            actual = actual.getSiguiente();
            return dato;
        }
    }
}