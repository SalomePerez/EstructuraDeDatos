package edu.co.uniquindio.Modelo.EstructuraDeDatos;
/**
 * Clase que implementa una cola genérica utilizando nodos enlazados.
 *
 * @param <T> El tipo de dato que almacenará la cola.
 */
public class Cola<T> {
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
    public T obtenerFrente() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía.");
        }
        return primero.dato;
    }

    /**
     * Método para obtener el tamaño de la cola.
     *
     * @return El número de elementos en la cola.
     */
    public int obtenerTamano() {
        return tamano;
    }
}