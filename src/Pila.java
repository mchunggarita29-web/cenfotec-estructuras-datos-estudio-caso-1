/**
 * Implementación propia de una pila (stack) genérica mediante nodos
 * enlazados (sin usar java.util.Stack ni java.util.Deque).
 * Operaciones LIFO clásicas: apilar (push), desapilar (pop),
 * observar el tope (peek), consultar si está vacía y su tamaño.
 */
public class Pila<T> {
    private Nodo<T> tope;
    private int tamanio;

    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }

    public void apilar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(tope);
        tope = nuevo;
        tamanio++;
    }

    public T desapilar() {
        if (esVacia()) {
            throw new IllegalStateException("No se puede desapilar: la pila está vacía.");
        }
        T dato = tope.getDato();
        tope = tope.getSiguiente();
        tamanio--;
        return dato;
    }

    public T verTope() {
        if (esVacia()) {
            throw new IllegalStateException("No se puede consultar el tope: la pila está vacía.");
        }
        return tope.getDato();
    }

    public boolean esVacia() {
        return tope == null;
    }

    public int tamanio() {
        return tamanio;
    }

    /** Representación del contenido de la pila de arriba (tope) hacia abajo, útil para trazas en el menú. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[tope -> ");
        Nodo<T> actual = tope;
        while (actual != null) {
            sb.append(actual.getDato());
            if (actual.getSiguiente() != null) sb.append(", ");
            actual = actual.getSiguiente();
        }
        sb.append(" <- fondo]");
        return sb.toString();
    }
}
