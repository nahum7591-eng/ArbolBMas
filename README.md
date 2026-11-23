# Árbol B+ – Práctica de Estructuras de Datos

Implementación en Java de un Árbol B+ con interfaz de consola, elaborada a
partir del proyecto facilitado por el docente.

## Requisitos de la consigna

| Requisito | Dónde se implementa |
| --- | --- |
| `ArbolBMas` con únicamente `raiz` y `orden` | `src/ArbolBMas.java` |
| Inserción con división automática de nodos | `insertar`, `insertarRecursivo`, `dividirHijo` |
| `eliminar(clave)` con rebalanceo (préstamos y fusiones) | `eliminar`, `rebalancear`, `prestarDeIzquierda`, `prestarDeDerecha`, `fusionar` |
| `recorrerRango(clave, n)` usando hojas enlazadas | `recorrerRango` |
| `NodoArbolBMas` con `datos` y `siguiente` (null en internos) | `src/NodoArbolBMas.java` |
| Menú de consola intuitivo y `main()` ordenado | `src/Main.java` |

## Compilación y ejecución

### Desde IntelliJ IDEA

1. `File > Open...` y seleccionar la carpeta `ArbolBMas`.
2. Si IntelliJ pide configurar el JDK, elegir cualquiera instalado (el código
   es compatible desde Java 8).
3. Verificar que `src` aparezca como *Sources Root* (ya viene configurado en
   `ArbolBMas.iml`).
4. Abrir `src/Main.java`, clic derecho y `Run 'Main.main()'`.

### Desde terminal

```bash
javac -encoding UTF-8 -d out/production/ArbolBMas src/*.java
java -cp out/production/ArbolBMas Main
```

## Descripción de los algoritmos

- **Inserción:** se desciende hasta la hoja correspondiente y se insertan
  clave y dato de forma ordenada. Al volver de la recursión, todo nodo con
  `orden` claves se divide. En hojas, la primera clave de la hoja derecha se
  copia al padre y se actualiza el enlace `siguiente`. En internos, la clave
  media sube al padre.
- **Eliminación:** se localiza la clave en su hoja. Si la hoja queda por debajo
  de su ocupación mínima, se intenta prestar un elemento de un hermano; si
  ninguno puede prestar, se fusiona con un hermano y el proceso se repite hacia
  la raíz. Cuando la raíz se queda sin claves, desaparece y sube su único hijo.
- **Separadores:** después de insertar o eliminar se recalculan las claves de
  los nodos internos, ya que éstas son el límite inferior del hijo de la
  derecha y pueden cambiar cuando se modifica la primera clave de una hoja.
- **Recorrido por rango:** se busca la hoja donde comienza la clave pedida y
  se avanza por los enlaces `siguiente`, imprimiendo clave y dato hasta
  completar `n` elementos.

## Uso del menú

Al iniciar se solicita el orden del árbol (mínimo 3, recomendado 4) y se ofrece
cargar datos de ejemplo. Luego están disponibles: insertar, buscar, eliminar,
recorrer rango, imprimir el árbol y salir.
