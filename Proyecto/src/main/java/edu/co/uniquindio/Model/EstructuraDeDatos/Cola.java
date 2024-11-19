package edu.co.uniquindio.Model.EstructuraDeDatos;

import edu.co.uniquindio.Model.Principales.Actividad;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase que implementa una cola genérica utilizando nodos enlazados.
 *
 * @param <T> El tipo de dato que almacenará la cola.
 */
public class Cola<T> implements Iterable<T>{
    private Nodo<T> primero; // Nodo primero de la cola
    private Nodo<T> ultimo;  // Nodo al ultimo de la cola
    private int tamano;     // Número de elementos en la cola

    /**
     * Constructor para inicializar una cola vacía.
     */
    public Cola() {
        this.primero = null;
        this.ultimo = null;
        this.tamano = 0;
    }

    /**
     * Método para verificar si la cola está vacía.
     *
     * @return true si la cola está vacía, false en caso contrario.
     */
    public boolean estaVacia() {
        return primero == null;
    }

    /**
     * Método para agregar un elemento a la cola.
     *
     * @param elemento El elemento a agregar.
     */
    public void encolar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (estaVacia()) {
            primero = nuevoNodo;
        } else {
            ultimo.siguiente = nuevoNodo;
        }
        ultimo = nuevoNodo;
        tamano++;
    }

    /**
     * Método para eliminar y devolver el elemento al frente de la cola.
     *
     * @return El elemento eliminado.
     * @throws IllegalStateException si la cola está vacía.
     */
    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        T elemento = primero.dato;
        primero = primero.siguiente;
        if (primero == null) { // Si la cola quedó vacía
            ultimo = null;
        }
        tamano--;
        return elemento;
    }

    /**
     * Método para obtener el elemento al frente de la cola sin eliminarlo.
     *
     * @return El elemento al frente de la cola.
     * @throws IllegalStateException si la cola está vacía.
     */
    public Nodo<T> obtenerPrimero() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        return primero;
    }

    /**
     * Método para obtener el tamaño de la cola.
     *
     * @return El número de elementos en la cola.
     */
    public int obtenerTamano() {
        return tamano;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = primero;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T dato = actual.dato;
                actual = actual.siguiente;
                return dato;
            }
        };
    }

    /**
     * Método para intercambiar dos actividades en la cola, dado su nombre.
     *
     * @param nombreActividad1 El nombre de la primera actividad.
     * @param nombreActividad2 El nombre de la segunda actividad.
     */
    public void intercambiarNodos(String nombreActividad1, String nombreActividad2) {
        if (nombreActividad1 == null || nombreActividad2 == null) {
            throw new IllegalArgumentException("Los nombres de las actividades no pueden ser nulos.");
        }

        Nodo<T> nodoAnterior1 = null;
        Nodo<T> nodoAnterior2 = null;
        Nodo<T> nodo1 = null;
        Nodo<T> nodo2 = null;

        Nodo<T> actual = primero;

        // Buscar los nodos y sus predecesores
        while (actual != null) {
            if (actual.siguiente != null && actual.siguiente.dato instanceof Actividad) {
                Actividad actividad = (Actividad) actual.siguiente.dato;
                if (actividad.obtenerNombre().equals(nombreActividad1)) {
                    nodoAnterior1 = actual;
                    nodo1 = actual.siguiente;
                }
                if (actividad.obtenerNombre().equals(nombreActividad2)) {
                    nodoAnterior2 = actual;
                    nodo2 = actual.siguiente;
                }
            }
            actual = actual.siguiente;
        }

        // Validar si ambas actividades fueron encontradas
        if (nodo1 == null || nodo2 == null) {
            throw new IllegalArgumentException("Una o ambas actividades no fueron encontradas en la cola.");
        }

        // Si los nodos son consecutivos, manejar el intercambio de referencias
        if (nodoAnterior1 == nodo2) {
            nodoAnterior1.siguiente = nodo1;
            Nodo<T> temp = nodo1.siguiente;
            nodo1.siguiente = nodo2;
            nodo2.siguiente = temp;
        } else if (nodoAnterior2 == nodo1) {
            nodoAnterior2.siguiente = nodo2;
            Nodo<T> temp = nodo2.siguiente;
            nodo2.siguiente = nodo1;
            nodo1.siguiente = temp;
        } else {
            // Intercambiar referencias normales si no son consecutivos
            Nodo<T> temp = nodo1.siguiente;
            nodoAnterior1.siguiente = nodo2;
            nodo1.siguiente = nodo2.siguiente;
            nodoAnterior2.siguiente = nodo1;
            nodo2.siguiente = temp;
        }

        // Ajustar punteros de la cola si se intercambian el primero o el último
        if (nodo1 == primero) {
            primero = nodo2;
        } else if (nodo2 == primero) {
            primero = nodo1;
        }

        if (nodo1 == ultimo) {
            ultimo = nodo2;
        } else if (nodo2 == ultimo) {
            ultimo = nodo1;
        }
    }

    /**
     * Método para obtener un elemento en una posición específica de la cola.
     *
     * @param posicion La posición del elemento a obtener (0 basado en índices).
     * @return El elemento en la posición especificada.
     * @throws IndexOutOfBoundsException Si la posición es inválida (fuera del rango de la cola).
     */
    public T obtenerElementoEnPosicion(int posicion) {
        if (posicion < 0 || posicion >= tamano) {
            throw new IndexOutOfBoundsException("La posición está fuera del rango de la cola.");
        }

        Nodo<T> actual = primero;
        int contador = 0;

        // Recorrer la cola hasta la posición deseada
        while (contador < posicion) {
            actual = actual.siguiente;
            contador++;
        }

        return actual.dato;
    }

}