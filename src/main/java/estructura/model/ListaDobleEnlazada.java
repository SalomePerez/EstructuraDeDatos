package estructura.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ListaDobleEnlazada<E> implements Iterable<E>, Serializable {

    private Nodo<E> cabeza;
    private Nodo<E> cola;
    private int size;

    private static class Nodo<E> {
        E elemento;
        Nodo<E> siguiente;
        Nodo<E> anterior;

        Nodo(E elemento, Nodo<E> anterior, Nodo<E> siguiente) {
            this.elemento = elemento;
            this.anterior = anterior;
            this.siguiente = siguiente;
        }

        /**
         * Constructor para crear un nuevo nodo sin elemento y sin referencias de nodo siguiente y anterior.
         */
        Nodo() {
            this(null, null, null);
        }
    }

    public ListaDobleEnlazada() {
        cabeza = new Nodo<>();
        cola = new Nodo<>(null, cabeza, null);
        cabeza.siguiente = cola;
        size = 0;
    }

    public void agregarUltimo(E elemento) {
        if(cola.anterior == null)
            agregarEntre(elemento, cabeza, cola);
        agregarEntre(elemento, cola.anterior, cola);
    }

    private void agregarEntre(E elemento, Nodo<E> predecesor, Nodo<E> sucesor) {
        // Crear y enlazar un nuevo nodo entre el predecesor y el sucesor
        Nodo<E> nuevoNodo = new Nodo<>(elemento, predecesor, sucesor);
        predecesor.siguiente = nuevoNodo;
        sucesor.anterior = nuevoNodo;
        size++;
    }

    public E eliminarPrimero() {
        if (estaVacia()) return null;
        return eliminar(cabeza.siguiente);
    }

    public E eliminarUltimo() {
        if (estaVacia()) return null;
        return eliminar(cola.anterior);
    }

    public void eliminar(E elemento) {
        Nodo<E> actual = cabeza.siguiente;
        while (actual != cola) {
            if (actual.elemento.equals(elemento)) {
                eliminar(actual);
                return;
            }
            actual = actual.siguiente;
        }
    }

    public boolean estaVacia() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    /**
     * Encuentra el primer elemento que coincide con el predicado dado.
     *
     * @param predicado un predicado para aplicar a cada elemento y determinar si debe ser devuelto
     * @return el primer elemento que coincide, o null si no hay coincidencia
     */
    public E encontrarPrimero(java.util.function.Predicate<E> predicado) {
        Nodo<E> actual = cabeza.siguiente;
        while (actual != cola) {
            if (predicado.test(actual.elemento)) {
                return actual.elemento;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    /**
     * Realiza la acción dada para cada elemento de la lista hasta que se procesen todos los elementos
     * o la acción lance una excepción. El orden de iteración está determinado por el parámetro reverse.
     *
     * @param acción  la acción a realizar para cada elemento
     * @param reverso si es true, la lista se itera en orden inverso
     */
    public void forEach(Consumer<E> acción, boolean reverso) {
        if (reverso) {
            Nodo<E> actual = cola.anterior; // Comenzamos por el final
            while (actual != cabeza) { // Mientras no lleguemos al nodo ficticio de cabeza
                acción.accept(actual.elemento);
                actual = actual.anterior;
            }
        } else {
            Nodo<E> actual = cabeza.siguiente; // Comenzamos por el principio
            while (actual != cola) { // Mientras no lleguemos al nodo ficticio de cola
                acción.accept(actual.elemento);
                actual = actual.siguiente;
            }
        }
    }

    /**
     * Elimina el primer elemento que coincide con el predicado dado.
     *
     * @param predicado un predicado para aplicar a cada elemento y determinar si debe ser eliminado
     * @return true si se eliminó un elemento, false en caso contrario
     */
    public boolean eliminarSi(java.util.function.Predicate<E> predicado) {
        Nodo<E> actual = cabeza.siguiente;
        while (actual != cola) {
            if (predicado.test(actual.elemento)) {
                eliminar(actual);
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Convierte esta ListaDobleEnlazada a una lista estándar.
     *
     * @return Una lista estándar que contiene los elementos de la ListaDobleEnlazada.
     */
    public List<E> aLista() {
        List<E> lista = new ArrayList<>();
        this.forEach(lista::add, false);
        return lista;
    }


    private E eliminar(Nodo<E> nodo) {
        Nodo<E> predecesor = nodo.anterior;
        Nodo<E> sucesor = nodo.siguiente;
        predecesor.siguiente = sucesor;
        sucesor.anterior = predecesor;
        size--;
        return nodo.elemento;
    }

    public void borrarLista() {
        cabeza = new Nodo<>();
        cola = new Nodo<>(null, cabeza, null);
        cabeza.siguiente = cola;
        size = 0;
    }

    /**
     * Convierte una lista estándar a una ListaDobleEnlazada.
     *
     * @param lista La lista estándar a convertir.
     * @param <E>   El tipo de elementos en la lista.
     * @return Una ListaDobleEnlazada que contiene los elementos de la lista.
     */
    public static <E> ListaDobleEnlazada<E> desdeLista(List<E> lista) {
        ListaDobleEnlazada<E> listaPersonalizada = new ListaDobleEnlazada<>();
        for (E elemento : lista) {
            listaPersonalizada.agregarUltimo(elemento);
        }
        return listaPersonalizada;
    }

    public boolean contieneId(int id) {
        Nodo<E> actual = cabeza.siguiente;
        while (actual != cola) {
            if (actual.elemento instanceof Integer && (Integer) actual.elemento == id) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new ListaDobleEnlazadaIterator();
    }

    /**
     * Clase interna que implementa el iterador para la lista doblemente enlazada.
     */
    private class ListaDobleEnlazadaIterator implements Iterator<E> {
        private Nodo<E> actual = cabeza.siguiente; // Comenzamos desde el primer elemento
        private Nodo<E> ultimoRetornado = null; // Último elemento retornado por next()

        @Override
        public boolean hasNext() {
            return actual != cola;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            E elemento = actual.elemento;
            ultimoRetornado = actual;
            actual = actual.siguiente;
            return elemento;
        }

        @Override
        public void remove() {
            if (ultimoRetornado == null) {
                throw new IllegalStateException("next() no ha sido llamado o el elemento ya ha sido eliminado");
            }
            Nodo<E> predecesor = ultimoRetornado.anterior;
            Nodo<E> sucesor = ultimoRetornado.siguiente;
            predecesor.siguiente = sucesor;
            sucesor.anterior = predecesor;
            size--;
            ultimoRetornado = null;

        }
    }
}