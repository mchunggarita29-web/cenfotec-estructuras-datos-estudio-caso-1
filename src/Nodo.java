/**
 * Nodo genérico de una pila enlazada.
 * En este estudio de caso, el dato que contiene cada nodo es un Token
 * (número literal, variable numérica, operador o paréntesis) de la
 * expresión aritmética que se está analizando.
 */
public class Nodo<T> {
    private T dato;
    private Nodo<T> siguiente;

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}
