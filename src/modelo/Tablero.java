package modelo;

public class Tablero {
    private final Ficha[][] tablero;
    private static final int FILAS = 7;
    private static final int COLUMNAS = 7;

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

        Ficha ficha = tablero[filaOrigen][columnaOrigen];
        tablero[filaDestino][columnaDestino] = ficha;
        tablero[filaOrigen][columnaOrigen] = null;
        ficha.moverFicha(filaDestino, columnaDestino);
    }

    public boolean sonAdyacentes(int fila1, int columna1, int fila2, int columna2) {
        //TODO algoritmo para verificar si las posiciones son adyacentes en el tablero
        return false;
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
