package modelo;

import modelo.enums.Color;

import java.util.*;

public class Tablero {
    private final Ficha[][] tablero;
    private static final int FILAS = 7;
    private static final int COLUMNAS = 7;
    private static final Map<String, List<String>> adyacencias = new HashMap<>();
    private static final Set<String> posicionesInvalidas = new HashSet<>(Arrays.asList(
            "0,1", "0,2", "0,4", "0,5", "1.0", "1,2", "1,4", "1,6",
            "2,0", "2,1", "2,5", "2,6", "3,3", "4,0", "4,1", "4,5",
            "4,6", "5,0", "5,2", "5,4", "5,6", "6,1", "6,2", "6,4", "6,5"
    ));

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
        return (fila >= 0 && fila < FILAS) && (columna >= 0 && columna < COLUMNAS) && !posicionesInvalidas.contains(fila + "," + columna);
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

    public void eliminarFicha(int fila, int columna) {
        if (!posicionValida(fila, columna)) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (!posicionOcupada(fila, columna)) {
            throw new IllegalStateException("No hay ficha en la posición");
        }
        Ficha ficha = tablero[fila][columna];
        ficha.eliminarFicha();
        tablero[fila][columna] = null;
    }

    public boolean hayMolino(int fila, int columna) {
        if (!posicionValida(fila, columna)) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (!posicionOcupada(fila, columna)) {
            throw new IllegalStateException("No hay ficha en la posición");
        }

        Ficha ficha = tablero[fila][columna];
        Color color = ficha.getColor();

        String pos = fila + "," + columna;
        List<String> adyacentes = adyacencias.get(pos);

        //busca una ficha adyacente deñ mismo color
        for (String adyacente : adyacentes) {
            String[] partes = adyacente.split(",");
            int filaAdyacente = Integer.parseInt(partes[0]);
            int columnaAdyacente = Integer.parseInt(partes[1]);

            if (posicionOcupada(filaAdyacente, columnaAdyacente)) {
                Ficha fichaAdyacente = tablero[filaAdyacente][columnaAdyacente];
                if (fichaAdyacente.getColor() == color) {
                    //si encuentra una ficha adyacente del mismo color entonces
                    //busca que haya otra ficha del mismo color siguiendo por el mismo camino o por el camino opuesto
                    int diferenciaFilas = Math.abs(fila - filaAdyacente);
                    int diferenciaColumnas = Math.abs(columna - columnaAdyacente);

                    int filaPosibleAdyacente1 = fila + diferenciaFilas;
                    int columnaPosibleAdyacente1 = columna + diferenciaColumnas;

                    int filaPosibleAdyacente2 = fila - diferenciaFilas;
                    int columnaPosibleAdyacente2 = columna - diferenciaColumnas;

                    if(posicionValida(filaPosibleAdyacente1, columnaPosibleAdyacente1)) {
                        Ficha posibleAdyacente1 = tablero[filaPosibleAdyacente1][columnaPosibleAdyacente1];
                        return posibleAdyacente1 != null && posibleAdyacente1.getColor() == color;
                    } else if(posicionValida(filaPosibleAdyacente2, columnaPosibleAdyacente2)) {
                        Ficha posibleAdyacente2 = tablero[filaPosibleAdyacente2][columnaPosibleAdyacente2];
                        return posibleAdyacente2 != null && posibleAdyacente2.getColor() == color;
                    }
                }
            }
        }

        return false;
    }

    public boolean sonAdyacentes(int fila1, int columna1, int fila2, int columna2) {
        String pos1 = fila1 + "," + columna1;
        String pos2 = fila2 + "," + columna2;
        return adyacencias.containsKey(pos1) && adyacencias.get(pos1).contains(pos2);
    }

    public String imprimirFicha(int fila, int columna) {
        if (!posicionValida(fila, columna)) {
            throw new IllegalArgumentException("Posición invalida");
        }
        Ficha ficha = tablero[fila][columna];
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
                tablero[i][j] = null;
            }
        }
    }
}
