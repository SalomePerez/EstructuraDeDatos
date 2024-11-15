package edu.co.uniquindio.EstructuraDeDatos;

/**
 * Clase que implementa una lista enlazada simple.
 *
 * @param <T> El tipo de dato almacenado en la lista.
 */
public class ListaEnlazada<T> {
    private Nodo<T> cabeza; // Nodo inicial de la lista
    private int tamanio = 0; // Tamaño de la lista

    // Método para insertar un nuevo elemento al final de la lista
    public void insertar(T elemento) {
        // Verifica si el elemento ya existe en la lista
        if (!contiene(elemento)) {
            Nodo<T> nuevoNodo = new Nodo<>(elemento); // Crea un nuevo nodo
            if (cabeza == null) {
                cabeza = nuevoNodo; // Si la lista está vacía, el nuevo nodo es la cabeza
            } else {
                Nodo<T> actual = cabeza;
                // Recorre la lista hasta llegar al final
                while (actual.getSiguiente() != null) {
                    actual = actual.getSiguiente();
                }
                // Inserta el nuevo nodo al final
                actual.setSiguiente(nuevoNodo);
            }
            tamanio++; // Aumenta el tamaño de la lista
        } else {
            System.out.println("El elemento " + elemento + " ya está en la lista. No se añadirá.");
        }
    }

    // Método para insertar un nuevo elemento después de un elemento existente
    public void insertarDespuesDe(T elementoExistente, T nuevoElemento) {
        // Verifica si el nuevo elemento ya está en la lista
        if (!contiene(nuevoElemento)) {
            Nodo<T> actual = cabeza;
            // Busca el nodo con el valor del elemento existente
            while (actual != null && !actual.getDato().equals(elementoExistente)) {
                actual = actual.getSiguiente();
            }
            // Si el elemento existe, inserta el nuevo nodo después de él
            if (actual != null) {
                Nodo<T> nuevoNodo = new Nodo<>(nuevoElemento);
                nuevoNodo.setSiguiente(actual.getSiguiente());
                actual.setSiguiente(nuevoNodo);
                tamanio++; // Aumenta el tamaño de la lista
            } else {
                System.out.println("El elemento existente no se encuentra en la lista.");
            }
        } else {
            System.out.println("El elemento " + nuevoElemento + " ya está en la lista. No se añadirá.");
        }
    }

    // Método para verificar si un elemento existe en la lista
    public boolean contiene(T elemento) {
        Nodo<T> actual = cabeza;
        // Recorre la lista buscando el elemento
        while (actual != null) {
            if (actual.getDato().equals(elemento)) {
                return true; // El elemento existe
            }
            actual = actual.getSiguiente();
        }
        return false; // El elemento no existe
    }

    // Getter para obtener la cabeza de la lista
    public Nodo<T> getCabeza() {
        return cabeza;
    }

    // Setter para establecer la cabeza de la lista
    public void setCabeza(Nodo<T> cabeza) {
        this.cabeza = cabeza;
    }

    // Getter para obtener el tamaño de la lista
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

    // Método para obtener un elemento en un índice específico
    public T get(int indice) {
        // Si el índice es inválido, lanza una excepción
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        Nodo<T> actual = cabeza;
        // Recorre la lista hasta el índice indicado
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato(); // Retorna el valor en ese índice
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
}