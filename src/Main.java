import java.util.Scanner;

public class Main {

    private static final int INSERTAR = 1;
    private static final int BUSCAR = 2;
    private static final int ELIMINAR = 3;
    private static final int RANGO = 4;
    private static final int IMPRIMIR = 5;
    private static final int SALIR = 6;

    public static void main(String[] args) {
        Scanner lector = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("   ARBOL B+ - ESTRUCTURA DE DATOS");
        System.out.println("=========================================");

        ArbolBMas arbol = new ArbolBMas(leerOrden(lector));

        if (confirmar(lector, "Desea cargar datos de ejemplo? (s/n): ")) {
            cargarDatosEjemplo(arbol);
            System.out.println("Datos de ejemplo cargados correctamente.");
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero(lector, "Elija una opcion: ");

            switch (opcion) {
                case INSERTAR:
                    int claveNueva = leerEntero(lector, "Ingrese la clave a insertar: ");
                    System.out.print("Ingrese el dato asociado: ");
                    String dato = lector.nextLine().trim();
                    boolean esNueva = arbol.insertar(claveNueva, dato);
                    if (esNueva) {
                        System.out.println("Clave " + claveNueva + " insertada correctamente.");
                    } else {
                        System.out.println("La clave " + claveNueva + " ya existia; se actualizo su dato.");
                    }
                    break;

                case BUSCAR:
                    int claveBuscada = leerEntero(lector, "Ingrese la clave a buscar: ");
                    if (arbol.buscar(claveBuscada)) {
                        System.out.println("Clave " + claveBuscada + " encontrada. Dato: "
                                + arbol.buscarDato(claveBuscada));
                    } else {
                        System.out.println("La clave " + claveBuscada + " no se encuentra en el arbol.");
                    }
                    break;

                case ELIMINAR:
                    if (arbol.estaVacio()) {
                        System.out.println("El arbol esta vacio. No hay nada que eliminar.");
                    } else {
                        int claveEliminar = leerEntero(lector, "Ingrese la clave a eliminar: ");
                        if (arbol.eliminar(claveEliminar)) {
                            System.out.println("Clave " + claveEliminar + " eliminada y arbol rebalanceado.");
                        } else {
                            System.out.println("La clave " + claveEliminar + " no existe en el arbol.");
                        }
                    }
                    break;

                case RANGO:
                    if (arbol.estaVacio()) {
                        System.out.println("El arbol esta vacio. No hay datos para recorrer.");
                    } else {
                        int claveInicio = leerEntero(lector, "Desde que clave desea comenzar: ");
                        int cantidad = leerEntero(lector, "Cuantos elementos desea recuperar: ");
                        System.out.println("Resultados:");
                        arbol.recorrerRango(claveInicio, cantidad);
                    }
                    break;

                case IMPRIMIR:
                    if (arbol.estaVacio()) {
                        System.out.println("El arbol esta vacio.");
                    } else {
                        arbol.imprimirArbol();
                    }
                    break;

                case SALIR:
                    System.out.println("Gracias por usar el programa. Hasta luego!");
                    break;

                default:
                    System.out.println("Opcion no valida. Elija un numero entre 1 y 6.");
            }

            if (opcion != SALIR) {
                pausa(lector);
            }

        } while (opcion != SALIR);

        lector.close();
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("---------- MENU PRINCIPAL ----------");
        System.out.println("1. Insertar clave y dato");
        System.out.println("2. Buscar clave");
        System.out.println("3. Eliminar clave");
        System.out.println("4. Recorrer rango");
        System.out.println("5. Imprimir arbol");
        System.out.println("6. Salir");
        System.out.println("-----------------------------------");
    }

    private static int leerEntero(Scanner lector, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = lector.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero. Intente nuevamente.");
            }
        }
    }

    private static int leerOrden(Scanner lector) {
        System.out.println("Ingrese el orden del arbol (minimo 3, se recomienda 4): ");
        while (true) {
            int orden = leerEntero(lector, "Orden: ");
            if (orden >= 3) {
                return orden;
            }
            System.out.println("El orden debe ser 3 o mayor. Intente nuevamente.");
        }
    }

    private static boolean confirmar(Scanner lector, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String respuesta = lector.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Responda con 's' o 'n'.");
        }
    }

    private static void cargarDatosEjemplo(ArbolBMas arbol) {
        int[] claves = {10, 20, 5, 6, 15, 30, 25, 40, 45, 50};
        for (int clave : claves) {
            arbol.insertar(clave, "Registro " + clave);
        }
    }

    private static void pausa(Scanner lector) {
        System.out.print("Presione Enter para continuar...");
        lector.nextLine();
    }
}
