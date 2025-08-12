package modelo;

import modelo.enums.Color;

import java.io.Serializable;
import java.util.*;

public class Tablero implements Serializable {
    private static final int FILAS = 7;
    private static final int COLUMNAS = 7;
    private final Posicion[][] posiciones = new Posicion[FILAS][COLUMNAS];

    public Tablero() {
        inicializarPosiciones();
        conectarAdyacencias();
    }

    private void inicializarPosiciones() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (esPosicionValida(i, j)) {
                    posiciones[i][j] = new Posicion(i, j);
                }
            }
        }
    }

    private void conectarAdyacencias() {
        conectarPosiciones(getPosicion(0, 0), getPosicion(0, 3));
        conectarPosiciones(getPosicion(0, 0), getPosicion(3, 0));
        conectarPosiciones(getPosicion(0, 3), getPosicion(1, 3));
        conectarPosiciones(getPosicion(0, 3), getPosicion(0, 6));
        conectarPosiciones(getPosicion(0, 6), getPosicion(3, 6));
        conectarPosiciones(getPosicion(1, 1), getPosicion(3, 1));
        conectarPosiciones(getPosicion(1, 1), getPosicion(1, 3));
        conectarPosiciones(getPosicion(1, 3), getPosicion(1, 5));
        conectarPosiciones(getPosicion(1, 3), getPosicion(2, 3));
        conectarPosiciones(getPosicion(1, 5), getPosicion(3, 5));
        conectarPosiciones(getPosicion(2, 2), getPosicion(3, 2));
        conectarPosiciones(getPosicion(2, 2), getPosicion(2, 3));
        conectarPosiciones(getPosicion(2, 3), getPosicion(2, 4));
        conectarPosiciones(getPosicion(2, 4), getPosicion(3, 4));
        conectarPosiciones(getPosicion(3, 0), getPosicion(6, 0));
        conectarPosiciones(getPosicion(3, 0), getPosicion(3, 1));
        conectarPosiciones(getPosicion(3, 1), getPosicion(3, 2));
        conectarPosiciones(getPosicion(3, 1), getPosicion(5, 1));
        conectarPosiciones(getPosicion(3, 2), getPosicion(4, 2));
        conectarPosiciones(getPosicion(3, 2), getPosicion(2, 2));
        conectarPosiciones(getPosicion(3, 4), getPosicion(4, 4));
        conectarPosiciones(getPosicion(3, 4), getPosicion(2, 4));
        conectarPosiciones(getPosicion(3, 4), getPosicion(3, 5));
        conectarPosiciones(getPosicion(3, 5), getPosicion(5, 5));
        conectarPosiciones(getPosicion(3, 5), getPosicion(3, 6));
        conectarPosiciones(getPosicion(3, 6), getPosicion(6, 6));
        conectarPosiciones(getPosicion(4, 2), getPosicion(4, 3));
        conectarPosiciones(getPosicion(4, 2), getPosicion(3, 2));
        conectarPosiciones(getPosicion(4, 3), getPosicion(4, 4));
        conectarPosiciones(getPosicion(4, 3), getPosicion(5, 3));
        conectarPosiciones(getPosicion(4, 4), getPosicion(3, 4));
        conectarPosiciones(getPosicion(5, 1), getPosicion(5, 3));
        conectarPosiciones(getPosicion(5, 1), getPosicion(3, 1));
        conectarPosiciones(getPosicion(5, 3), getPosicion(5, 5));
        conectarPosiciones(getPosicion(5, 3), getPosicion(4, 3));
        conectarPosiciones(getPosicion(5, 3), getPosicion(6, 3));
        conectarPosiciones(getPosicion(5, 5), getPosicion(3, 5));
        conectarPosiciones(getPosicion(6, 0), getPosicion(6, 3));
        conectarPosiciones(getPosicion(6, 3), getPosicion(6, 6));
    }

    private void conectarPosiciones(Posicion n1, Posicion n2) {
        if (n1 != null && n2 != null) {
            n1.agregarAdyacente(n2);
            n2.agregarAdyacente(n1);
        }
    }

    public boolean esPosicionValida(int fila, int columna) {
        // Lista de posiciones inválidas
        String[] invalidas = {"0,1", "0,2", "0,4", "0,5", "1,0", "1,2", "1,4", "1,6", "2,0", "2,1", "2,5", "2,6", "3,3", "4,0", "4,1", "4,5", "4,6", "5,0", "5,2", "5,4", "5,6", "6,1", "6,2", "6,4", "6,5"};
        String pos = fila + "," + columna;
        for (String invalida : invalidas) {
            if (invalida.equals(pos)) return false;
        }
        return (fila >= 0 && fila < FILAS) && (columna >= 0 && columna < COLUMNAS);
    }

    public boolean posicionOcupada(int fila, int columna) {
        Posicion posicion = getPosicion(fila, columna);
        if (posicion == null)
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        return posicion.getFicha() != null;
    }

    public void colocarFicha(int fila, int columna, Ficha ficha) {
        Posicion posicion = getPosicion(fila, columna);
        if (posicion == null) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (posicion.getFicha() != null) {
            throw new IllegalStateException("La posición ya está ocupada por otra ficha");
        }
        posicion.setFicha(ficha);
        ficha.colocarFicha();
    }

    public Ficha obtenerFicha(int fila, int columna) {
        Posicion posicion = getPosicion(fila, columna);
        return posicion != null ? posicion.getFicha() : null;
    }

    public void moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        Posicion origen = getPosicion(filaOrigen, columnaOrigen);
        Posicion destino = getPosicion(filaDestino, columnaDestino);
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (origen.getFicha() == null) {
            throw new IllegalStateException("No hay ficha en la posición de origen");
        }
        if (destino.getFicha() != null) {
            throw new IllegalStateException("La posición de destino ya está ocupada por otra ficha");
        }
        Ficha ficha = origen.getFicha();
        destino.setFicha(ficha);
        origen.setFicha(null);
    }

    public void eliminarFicha(int fila, int columna) {
        Posicion posicion = getPosicion(fila, columna);
        if (posicion == null) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (posicion.getFicha() == null) {
            throw new IllegalStateException("No hay ficha en la posición");
        }
        Ficha ficha = posicion.getFicha();
        ficha.eliminarFicha();
        posicion.setFicha(null);
    }

    public boolean sonAdyacentes(int fila1, int columna1, int fila2, int columna2) {
        Posicion n1 = getPosicion(fila1, columna1);
        Posicion n2 = getPosicion(fila2, columna2);
        return n1 != null && n2 != null && n1.getAdyacentes().contains(n2);
    }

    public boolean hayMolino(int fila, int columna) {
        if (!esPosicionValida(fila, columna)) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (!posicionOcupada(fila, columna)) {
            throw new IllegalStateException("No hay ficha en la posición");
        }

        Posicion pos = getPosicion(fila, columna);
        Ficha ficha = pos.getFicha();
        if (ficha == null) {
            throw new IllegalStateException("No hay ficha en la posición");
        }
        Color color = ficha.getColor();

        List<Posicion> adyacentes = pos.getAdyacentes();

        for (Posicion adyacente : adyacentes) {
            Ficha fichaAdyacente = adyacente.getFicha();
            if (fichaAdyacente != null && !ficha.equals(fichaAdyacente) && fichaAdyacente.getColor() == color) {
                int filaAdyacente = adyacente.getFila();
                int columnaAdyacente = adyacente.getColumna();
                int[][] posiblesMolinos = {
                        {filaAdyacente + Math.abs(fila - filaAdyacente), columnaAdyacente + Math.abs(columna - columnaAdyacente)},
                        {filaAdyacente - Math.abs(fila - filaAdyacente), columnaAdyacente - Math.abs(columna - columnaAdyacente)},
                        {fila + Math.abs(fila - filaAdyacente), columna + Math.abs(columna - columnaAdyacente)},
                        {fila - Math.abs(fila - filaAdyacente), columna - Math.abs(columna - columnaAdyacente)}
                };

                for (int[] posibleMolino : posiblesMolinos) {
                    int filaMolino = posibleMolino[0];
                    int columnaMolino = posibleMolino[1];
                    if (esPosicionValida(filaMolino, columnaMolino)) {
                        Posicion posMolino = getPosicion(filaMolino, columnaMolino);
                        Ficha fichaMolino = posMolino.getFicha();
                        if (fichaMolino != null &&
                                fichaMolino.getColor() == color &&
                                !ficha.equals(fichaMolino) &&
                                !fichaAdyacente.equals(fichaMolino)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public void limpiarTablero() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Posicion posicion = posiciones[i][j];
                if (posicion != null) {
                    posicion.setFicha(null);
                }
            }
        }
    }

    public Posicion getPosicion(int fila, int columna) {
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) return null;
        return posiciones[fila][columna];
    }

    public Posicion[][] getPosiciones() {
        return posiciones;
    }

    public int getFilas() {
        return FILAS;
    }

    public int getColumnas() {
        return COLUMNAS;
    }
}
