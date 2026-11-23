import java.util.ArrayList;
import java.util.List;

public class ArbolBMas {

    private final int orden;
    private NodoArbolBMas raiz;

    public ArbolBMas(int orden) {
        if (orden < 3) {
            throw new IllegalArgumentException("El orden del árbol debe ser mayor o igual que 3.");
        }
        this.orden = orden;
        this.raiz = new NodoArbolBMas(true);
    }

    public int getOrden() {
        return orden;
    }

    public boolean estaVacio() {
        return raiz.getClaves().isEmpty();
    }

    public void insertar(int clave) {
        insertar(clave, "Dato " + clave);
    }

    public boolean insertar(int clave, String dato) {
        String valor = (dato == null || dato.trim().isEmpty()) ? "Sin dato" : dato;
        NodoArbolBMas hoja = buscarHoja(clave);
        int existente = posicionExacta(hoja, clave);
        if (existente >= 0) {
            hoja.getDatos().set(existente, valor);
            return false;
        }

        insertarRecursivo(raiz, clave, valor);

        if (raiz.getClaves().size() >= orden) {
            NodoArbolBMas nuevaRaiz = new NodoArbolBMas(false);
            nuevaRaiz.getHijos().add(raiz);
            dividirHijo(nuevaRaiz, 0);
            raiz = nuevaRaiz;
        }

        actualizarSeparadores(raiz);
        return true;
    }

    private void insertarRecursivo(NodoArbolBMas nodo, int clave, String dato) {
        if (nodo.esHoja()) {
            int i = 0;
            while (i < nodo.getClaves().size() && clave > nodo.getClaves().get(i)) {
                i++;
            }
            nodo.getClaves().add(i, clave);
            nodo.getDatos().add(i, dato);
            return;
        }

        int i = indiceHijo(nodo, clave);
        NodoArbolBMas hijo = nodo.getHijos().get(i);
        insertarRecursivo(hijo, clave, dato);

        if (hijo.getClaves().size() >= orden) {
            dividirHijo(nodo, i);
        }
    }

    private void dividirHijo(NodoArbolBMas padre, int indice) {
        NodoArbolBMas lleno = padre.getHijos().get(indice);
        NodoArbolBMas nuevo = new NodoArbolBMas(lleno.esHoja());
        int mitad = orden / 2;

        if (lleno.esHoja()) {
            nuevo.setClaves(new ArrayList<>(lleno.getClaves().subList(mitad, lleno.getClaves().size())));
            nuevo.setDatos(new ArrayList<>(lleno.getDatos().subList(mitad, lleno.getDatos().size())));
            lleno.setClaves(new ArrayList<>(lleno.getClaves().subList(0, mitad)));
            lleno.setDatos(new ArrayList<>(lleno.getDatos().subList(0, mitad)));

            nuevo.setSiguiente(lleno.getSiguiente());
            lleno.setSiguiente(nuevo);

            padre.getClaves().add(indice, nuevo.getClaves().get(0));
        } else {
            int claveMedia = lleno.getClaves().get(mitad);
            nuevo.setClaves(new ArrayList<>(lleno.getClaves().subList(mitad + 1, lleno.getClaves().size())));
            lleno.setClaves(new ArrayList<>(lleno.getClaves().subList(0, mitad)));
            nuevo.setHijos(new ArrayList<>(lleno.getHijos().subList(mitad + 1, lleno.getHijos().size())));
            lleno.setHijos(new ArrayList<>(lleno.getHijos().subList(0, mitad + 1)));
            padre.getClaves().add(indice, claveMedia);
        }

        padre.getHijos().add(indice + 1, nuevo);
    }

    public boolean buscar(int clave) {
        return posicionExacta(buscarHoja(clave), clave) >= 0;
    }

    public String buscarDato(int clave) {
        NodoArbolBMas hoja = buscarHoja(clave);
        int i = posicionExacta(hoja, clave);
        return i < 0 ? null : hoja.getDatos().get(i);
    }

    private NodoArbolBMas buscarHoja(int clave) {
        NodoArbolBMas actual = raiz;
        while (!actual.esHoja()) {
            actual = actual.getHijos().get(indiceHijo(actual, clave));
        }
        return actual;
    }

    private int indiceHijo(NodoArbolBMas nodo, int clave) {
        int i = 0;
        while (i < nodo.getClaves().size() && clave >= nodo.getClaves().get(i)) {
            i++;
        }
        return i;
    }

    private int posicionExacta(NodoArbolBMas hoja, int clave) {
        for (int i = 0; i < hoja.getClaves().size(); i++) {
            if (hoja.getClaves().get(i) == clave) {
                return i;
            }
        }
        return -1;
    }

    public boolean eliminar(int clave) {
        List<NodoArbolBMas> camino = new ArrayList<>();
        NodoArbolBMas actual = raiz;

        while (!actual.esHoja()) {
            camino.add(actual);
            actual = actual.getHijos().get(indiceHijo(actual, clave));
        }

        int posicion = posicionExacta(actual, clave);
        if (posicion < 0) {
            return false;
        }

        actual.getClaves().remove(posicion);
        actual.getDatos().remove(posicion);

        rebalancear(camino, actual);

        if (!raiz.esHoja() && raiz.getClaves().isEmpty()) {
            raiz = raiz.getHijos().get(0);
        }

        actualizarSeparadores(raiz);
        return true;
    }

    private void actualizarSeparadores(NodoArbolBMas nodo) {
        if (nodo.esHoja()) {
            return;
        }
        for (int i = 1; i < nodo.getHijos().size(); i++) {
            nodo.getClaves().set(i - 1, primeraClave(nodo.getHijos().get(i)));
        }
        for (NodoArbolBMas hijo : nodo.getHijos()) {
            actualizarSeparadores(hijo);
        }
    }

    private int primeraClave(NodoArbolBMas nodo) {
        while (!nodo.esHoja()) {
            nodo = nodo.getHijos().get(0);
        }
        return nodo.getClaves().get(0);
    }

    private int minimoClaves(NodoArbolBMas nodo) {
        if (nodo.esHoja()) {
            return orden / 2;
        }
        return (orden + 1) / 2 - 1;
    }

    private void rebalancear(List<NodoArbolBMas> camino, NodoArbolBMas nodo) {
        while (nodo != raiz && nodo.getClaves().size() < minimoClaves(nodo)) {
            NodoArbolBMas padre = camino.get(camino.size() - 1);
            int i = indiceHijoExacto(padre, nodo);

            if (i > 0) {
                NodoArbolBMas izquierdo = padre.getHijos().get(i - 1);
                if (izquierdo.getClaves().size() > minimoClaves(izquierdo)) {
                    prestarDeIzquierda(padre, i, izquierdo, nodo);
                    return;
                }
            }

            if (i < padre.getHijos().size() - 1) {
                NodoArbolBMas derecho = padre.getHijos().get(i + 1);
                if (derecho.getClaves().size() > minimoClaves(derecho)) {
                    prestarDeDerecha(padre, i, derecho, nodo);
                    return;
                }
            }

            if (i > 0) {
                fusionar(padre, i - 1, padre.getHijos().get(i - 1), nodo);
            } else {
                fusionar(padre, i, nodo, padre.getHijos().get(i + 1));
            }

            camino.remove(camino.size() - 1);
            if (camino.isEmpty()) {
                break;
            }
            nodo = padre;
        }
    }

    private int indiceHijoExacto(NodoArbolBMas padre, NodoArbolBMas hijo) {
        for (int i = 0; i < padre.getHijos().size(); i++) {
            if (padre.getHijos().get(i) == hijo) {
                return i;
            }
        }
        return -1;
    }

    private void prestarDeIzquierda(NodoArbolBMas padre, int i,
                                    NodoArbolBMas izquierdo, NodoArbolBMas nodo) {
        if (nodo.esHoja()) {
            int clave = izquierdo.getClaves().remove(izquierdo.getClaves().size() - 1);
            String dato = izquierdo.getDatos().remove(izquierdo.getDatos().size() - 1);
            nodo.getClaves().add(0, clave);
            nodo.getDatos().add(0, dato);
            padre.getClaves().set(i - 1, nodo.getClaves().get(0));
        } else {
            NodoArbolBMas hijoPrestado = izquierdo.getHijos().remove(izquierdo.getHijos().size() - 1);
            nodo.getHijos().add(0, hijoPrestado);
            nodo.getClaves().add(0, padre.getClaves().get(i - 1));
            padre.getClaves().set(i - 1, izquierdo.getClaves().remove(izquierdo.getClaves().size() - 1));
        }
    }

    private void prestarDeDerecha(NodoArbolBMas padre, int i,
                                  NodoArbolBMas derecho, NodoArbolBMas nodo) {
        if (nodo.esHoja()) {
            int clave = derecho.getClaves().remove(0);
            String dato = derecho.getDatos().remove(0);
            nodo.getClaves().add(clave);
            nodo.getDatos().add(dato);
            padre.getClaves().set(i, derecho.getClaves().get(0));
        } else {
            NodoArbolBMas hijoPrestado = derecho.getHijos().remove(0);
            nodo.getHijos().add(hijoPrestado);
            nodo.getClaves().add(padre.getClaves().get(i));
            padre.getClaves().set(i, derecho.getClaves().remove(0));
        }
    }

    private void fusionar(NodoArbolBMas padre, int indice,
                          NodoArbolBMas izquierdo, NodoArbolBMas derecho) {
        if (izquierdo.esHoja()) {
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getDatos().addAll(derecho.getDatos());
            izquierdo.setSiguiente(derecho.getSiguiente());
        } else {
            izquierdo.getClaves().add(padre.getClaves().get(indice));
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getHijos().addAll(derecho.getHijos());
        }
        padre.getClaves().remove(indice);
        padre.getHijos().remove(indice + 1);
    }

    public void recorrerRango(int clave, int n) {
        if (n <= 0) {
            System.out.println("La cantidad de elementos debe ser mayor que cero.");
            return;
        }

        NodoArbolBMas hoja = buscarHoja(clave);
        int i = 0;
        while (i < hoja.getClaves().size() && hoja.getClaves().get(i) < clave) {
            i++;
        }

        int contador = 0;
        while (hoja != null && contador < n) {
            while (i < hoja.getClaves().size() && contador < n) {
                System.out.printf("   %d) Clave: %d | Dato: %s%n",
                        contador + 1, hoja.getClaves().get(i), hoja.getDatos().get(i));
                contador++;
                i++;
            }
            hoja = hoja.getSiguiente();
            i = 0;
        }

        if (contador == 0) {
            System.out.println("No hay elementos a partir de la clave " + clave + ".");
        } else {
            System.out.println("Total recuperado: " + contador + " elemento(s).");
        }
    }

    public void imprimirArbol() {
        imprimirNodo(raiz, "", true);
    }

    private void imprimirNodo(NodoArbolBMas nodo, String indentacion, boolean esUltimo) {
        String etiqueta = nodo.esHoja() ? "Hoja > " : "Interno > ";
        StringBuilder linea = new StringBuilder(indentacion).append("+- ")
                .append(etiqueta).append(nodo.getClaves());

        if (nodo.esHoja()) {
            linea.append("   datos=").append(nodo.getDatos());
            NodoArbolBMas sig = nodo.getSiguiente();
            linea.append("   siguiente=").append(sig == null ? "null" : sig.getClaves().get(0));
        }

        System.out.println(linea);
        String nuevaIndentacion = indentacion + (esUltimo ? "   " : "|  ");
        for (int i = 0; i < nodo.getHijos().size(); i++) {
            imprimirNodo(nodo.getHijos().get(i), nuevaIndentacion,
                    i == nodo.getHijos().size() - 1);
        }
    }
}
