package modelo;

import modelo.enums.Color;

import java.util.*;

public class Tablero {
    private static final int FILAS = 7;
    private static final int COLUMNAS = 7;
    private final Nodo[][] nodos = new Nodo[FILAS][COLUMNAS];

    public Tablero() {
        inicializarNodos();
        conectarAdyacencias();
    }

    private void inicializarNodos() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (esPosicionValida(i, j)) {
                    nodos[i][j] = new Nodo(i, j);
                }
            }
        }
    }

    private void conectarAdyacencias() {
        // Definición de adyacencias
        conectarNodos(0,0, 0,3); conectarNodos(0,0, 3,0);
        conectarNodos(0,3, 1,3); conectarNodos(0,3, 0,6);
        conectarNodos(0,6, 3,6);
        conectarNodos(1,1, 3,1); conectarNodos(1,1, 1,3);
        conectarNodos(1,3, 1,5); conectarNodos(1,3, 2,3);
        conectarNodos(1,5, 3,5);
        conectarNodos(2,2, 3,2); conectarNodos(2,2, 2,3);
        conectarNodos(2,3, 2,4);
        conectarNodos(2,4, 3,4);
        conectarNodos(3,0, 6,0); conectarNodos(3,0, 3,1);
        conectarNodos(3,1, 3,2); conectarNodos(3,1, 5,1);
        conectarNodos(3,2, 4,2); conectarNodos(3,2, 2,2);
        conectarNodos(3,4, 4,4); conectarNodos(3,4, 2,4); conectarNodos(3,4, 3,5);
        conectarNodos(3,5, 5,5); conectarNodos(3,5, 3,6);
        conectarNodos(3,6, 6,6);
        conectarNodos(4,2, 4,3); conectarNodos(4,2, 3,2);
        conectarNodos(4,3, 4,4); conectarNodos(4,3, 5,3);
        conectarNodos(4,4, 3,4);
        conectarNodos(5,1, 5,3); conectarNodos(5,1, 3,1);
        conectarNodos(5,3, 5,5); conectarNodos(5,3, 4,3); conectarNodos(5,3, 6,3);
        conectarNodos(5,5, 3,5);
        conectarNodos(6,0, 6,3);
        conectarNodos(6,3, 6,6);
    }

    private void conectarNodos(int f1, int c1, int f2, int c2) {
        Nodo n1 = getNodo(f1, c1);
        Nodo n2 = getNodo(f2, c2);
        if (n1 != null && n2 != null) {
            n1.agregarAdyacente(n2);
            n2.agregarAdyacente(n1);
        }
    }

    public boolean esPosicionValida(int fila, int columna) {
        // Lista de posiciones inválidas
        String[] invalidas = {"0,1","0,2","0,4","0,5","1,0","1,2","1,4","1,6","2,0","2,1","2,5","2,6","3,3","4,0","4,1","4,5","4,6","5,0","5,2","5,4","5,6","6,1","6,2","6,4","6,5"};
        String pos = fila + "," + columna;
        for (String invalida : invalidas) {
            if (invalida.equals(pos)) return false;
        }
        return (fila >= 0 && fila < FILAS) && (columna >= 0 && columna < COLUMNAS);
    }

    public boolean posicionOcupada(int fila, int columna) {
        Nodo nodo = getNodo(fila, columna);
        if (nodo == null)
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        return nodo.getFicha() != null;
    }

    public void colocarFicha(int fila, int columna, Ficha ficha) {
        Nodo nodo = getNodo(fila, columna);
        if (nodo == null) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (nodo.getFicha() != null) {
            throw new IllegalStateException("La posición ya está ocupada por otra ficha");
        }
        nodo.setFicha(ficha);
        ficha.colocarFicha();
    }

    public Ficha obtenerFicha(int fila, int columna) {
        Nodo nodo = getNodo(fila, columna);
        return nodo != null ? nodo.getFicha() : null;
    }

    public void moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        Nodo origen = getNodo(filaOrigen, columnaOrigen);
        Nodo destino = getNodo(filaDestino, columnaDestino);
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
        Nodo nodo = getNodo(fila, columna);
        if (nodo == null) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (nodo.getFicha() == null) {
            throw new IllegalStateException("No hay ficha en la posición");
        }
        Ficha ficha = nodo.getFicha();
        ficha.eliminarFicha();
        nodo.setFicha(null);
    }

    public boolean sonAdyacentes(int fila1, int columna1, int fila2, int columna2) {
        Nodo n1 = getNodo(fila1, columna1);
        Nodo n2 = getNodo(fila2, columna2);
        return n1 != null && n2 != null && n1.getAdyacentes().contains(n2);
    }

    public boolean hayMolino(int fila, int columna) {
        if (!esPosicionValida(fila, columna)) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (!posicionOcupada(fila, columna)) {
            throw new IllegalStateException("No hay ficha en la posición");
        }
        Ficha ficha = obtenerFicha(fila, columna);
        if (ficha == null) return false;
        Color color = ficha.getColor();
        Nodo nodo = getNodo(fila, columna);
        List<Nodo> adyacentes = nodo.getAdyacentes();
        for (Nodo ady : adyacentes) {
            Ficha fichaAdy = ady.getFicha();
            if (fichaAdy != null && fichaAdy.getColor() == color) {
                int diferenciaFila = ady.getFila() - fila;
                int diferenciaColumna = ady.getColumna() - columna;
                int filaTercera = ady.getFila() + diferenciaFila;
                int columnaTercera = ady.getColumna() + diferenciaColumna;
                Nodo tercerNodo = getNodo(filaTercera, columnaTercera);
                if (tercerNodo != null) {
                    Ficha fichaTercera = tercerNodo.getFicha();
                    if (fichaTercera != null && fichaTercera.getColor() == color) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String imprimirFicha(int fila, int columna) {
        Nodo nodo = getNodo(fila, columna);
        if (nodo == null) {
            return " ";
        }
        Ficha ficha = nodo.getFicha();
        return ficha != null ? ficha.getColor().toString().substring(0, 1) : "@";
    }

    public void imprimirTablero() {
        // si estas corrigiendo esto te pido perdon, no se me ocurrio una forma mejor de hacerlo
        System.out.println(" " + imprimirFicha(0, 0) + "-----------" + imprimirFicha(0, 3) + "-----------" + imprimirFicha(0, 6));
        System.out.println(" |           |           |");
        System.out.println(" |   " + imprimirFicha(1, 1) + "-------" + imprimirFicha(1, 3) + "-------" + imprimirFicha(1, 5) + "   |");
        System.out.println(" |   |       |       |   |");
        System.out.println(" |   |   " + imprimirFicha(2, 2) + "---" + imprimirFicha(2, 3) + "---" + imprimirFicha(2, 4) + "   |   |");
        System.out.println(" |   |   |       |   |   |");
        System.out.println(imprimirFicha(3, 0) + "---" + imprimirFicha(3, 1) + "---" + imprimirFicha(3, 2) + "       " + imprimirFicha(3, 4) + "---" + imprimirFicha(3, 5) + "---" + imprimirFicha(3, 6));
        System.out.println(" |   |   |       |   |   |");
        System.out.println(" |   |   " + imprimirFicha(4, 2) + "---" + imprimirFicha(4, 3) + "---" + imprimirFicha(4, 4) + "   |   |");
        System.out.println(" |   |       |       |   |");
        System.out.println(" |   " + imprimirFicha(5, 1) + "-------" + imprimirFicha(5, 3) + "-------" + imprimirFicha(5, 5) + "   |");
        System.out.println(" |           |           |");
        System.out.println(" " + imprimirFicha(6, 0) + "-----------" + imprimirFicha(6, 3) + "-----------" + imprimirFicha(6, 6));
    }

    public void limpiarTablero() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Nodo nodo = nodos[i][j];
                if (nodo != null) {
                    nodo.setFicha(null);
                }
            }
        }
    }

    public Nodo getNodo(int fila, int columna) {
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) return null;
        return nodos[fila][columna];
    }

    public int getFilas() {
        return FILAS;
    }

    public int getColumnas() {
        return COLUMNAS;
    }
}
