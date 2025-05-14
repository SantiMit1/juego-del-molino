package modelo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tablero {
    private final Ficha[][] tablero;
    private static final int FILAS = 7;
    private static final int COLUMNAS = 7;
    private static final Map<String, List<String>> adyacencias = new HashMap<>();

    static {
        // Definición de adyacencias
        adyacencias.put("0,0", Arrays.asList("0,3", "3,0"));
        adyacencias.put("0,3", Arrays.asList("0,0", "1,3", "0,6"));
        adyacencias.put("0,6", Arrays.asList("0,3", "3,6"));
        adyacencias.put("1,1", Arrays.asList("3,1", "1,3"));
        adyacencias.put("1,3", Arrays.asList("1,1", "0,3", "1,5", "2,3"));
        adyacencias.put("1,5", Arrays.asList("1,3", "3,5"));
        adyacencias.put("2,2", Arrays.asList("3,2", "2,3"));
        adyacencias.put("2,3", Arrays.asList("2,2", "1,3", "2,4"));
        adyacencias.put("2,4", Arrays.asList("2,3", "3,4"));
        adyacencias.put("3,0", Arrays.asList("0,0", "6,0", "3,1"));
        adyacencias.put("3,1", Arrays.asList("3,0", "1,1", "3,2", "5,1"));
        adyacencias.put("3,2", Arrays.asList("3,1", "4,2", "2,2"));
        adyacencias.put("3,4", Arrays.asList("4,4", "2,4", "3,5"));
        adyacencias.put("3,5", Arrays.asList("3,4", "5,5", "1,5", "3,6"));
        adyacencias.put("3,6", Arrays.asList("0,6", "3,5", "6,6"));
        adyacencias.put("4,2", Arrays.asList("4,3", "3,2"));
        adyacencias.put("4,3", Arrays.asList("4,2", "4,4", "5,3"));
        adyacencias.put("4,4", Arrays.asList("4,3", "3,4"));
        adyacencias.put("5,1", Arrays.asList("3,1", "5,3"));
        adyacencias.put("5,3", Arrays.asList("5,5", "5,1", "4,3", "6,3"));
        adyacencias.put("5,5", Arrays.asList("5,3", "3,5"));
        adyacencias.put("6,0", Arrays.asList("3,0", "6,3"));
        adyacencias.put("6,3", Arrays.asList("6,0", "5,3", "6,6"));
        adyacencias.put("6,6", Arrays.asList("3,6", "6,3"));
    }

    public Tablero() {
        this.tablero = new Ficha[FILAS][COLUMNAS];
    }

    public boolean posicionValida(int fila, int columna) {
        return fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS;
    }

    public boolean posicionOcupada(int fila, int columna) {
        if (!posicionValida(fila, columna))
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        return tablero[fila][columna] != null;
    }

    public void colocarFicha(int fila, int columna, Ficha ficha) {
        if (!posicionValida(fila, columna)) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (posicionOcupada(fila, columna)) {
            throw new IllegalStateException("La posición ya está ocupada por otra ficha");
        }
        tablero[fila][columna] = ficha;
        ficha.colocarFicha(fila, columna);
    }

    public Ficha obtenerFicha(int fila, int columna) {
        if (!posicionValida(fila, columna)) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        return tablero[fila][columna];
    }

    public void moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        if (!posicionValida(filaOrigen, columnaOrigen) || !posicionValida(filaDestino, columnaDestino)) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (!posicionOcupada(filaOrigen, columnaOrigen)) {
            throw new IllegalStateException("No hay ficha en la posición de origen");
        }
        if (posicionOcupada(filaDestino, columnaDestino)) {
            throw new IllegalStateException("La posición de destino ya está ocupada por otra ficha");
        }
        if (!sonAdyacentes(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            throw new IllegalStateException("Las posiciones no son adyacentes");
        }

        Ficha ficha = tablero[filaOrigen][columnaOrigen];
        tablero[filaDestino][columnaDestino] = ficha;
        tablero[filaOrigen][columnaOrigen] = null;
    }

    public boolean sonAdyacentes(int fila1, int columna1, int fila2, int columna2) {
        String pos1 = fila1 + "," + columna1;
        String pos2 = fila2 + "," + columna2;
        return adyacencias.containsKey(pos1) && adyacencias.get(pos1).contains(pos2);
    }

    public void imprimirTablero() {
        //TODO algoritmo para imprimir el tablero que tiene una forma toda rara
    }

    public void limpiarTablero() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = null;
            }
        }
    }
}
