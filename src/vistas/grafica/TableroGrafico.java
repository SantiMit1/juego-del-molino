package vistas.grafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class TableroGrafico extends JPanel {
    private static final int MARGEN = 50;
    private static final int RADIO_POSICION = 15;
    private static final Color COLOR_LINEA = Color.BLACK;
    private static final Color COLOR_VACIO = Color.GRAY;
    private static final Color COLOR_RESALTADO = Color.YELLOW;
    private final Color COLOR_JUGADOR_UNO = Color.WHITE;
    private final Color COLOR_JUGADOR_DOS = Color.BLACK;

    private final VistaGrafica vista;
    private String posicionesTablero = "";
    private Map<String, Point> coordenadasPosiciones;
    private Map<String, Boolean> posicionesResaltadas;

    public TableroGrafico(VistaGrafica vista) {
        this.vista = vista;
        this.coordenadasPosiciones = new HashMap<>();
        this.posicionesResaltadas = new HashMap<>();

        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.LIGHT_GRAY);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClickTablero(e.getX(), e.getY());
            }
        });

        inicializarCoordenadasPosiciones();
    }

    private void inicializarCoordenadasPosiciones() {
        int anchoTablero = 400;
        int altoTablero = 400;

        // Cuadrado exterior
        coordenadasPosiciones.put("0,0", new Point(MARGEN, MARGEN)); // A0
        coordenadasPosiciones.put("0,3", new Point(MARGEN + anchoTablero/2, MARGEN)); // D0
        coordenadasPosiciones.put("0,6", new Point(MARGEN + anchoTablero, MARGEN)); // G0

        // Cuadrado medio
        coordenadasPosiciones.put("1,1", new Point(MARGEN + anchoTablero/6, MARGEN + altoTablero/6)); // B1
        coordenadasPosiciones.put("1,3", new Point(MARGEN + anchoTablero/2, MARGEN + altoTablero/6)); // D1
        coordenadasPosiciones.put("1,5", new Point(MARGEN + anchoTablero*5/6, MARGEN + altoTablero/6)); // F1

        // Cuadrado interior
        coordenadasPosiciones.put("2,2", new Point(MARGEN + anchoTablero/3, MARGEN + altoTablero/3)); // C2
        coordenadasPosiciones.put("2,3", new Point(MARGEN + anchoTablero/2, MARGEN + altoTablero/3)); // D2
        coordenadasPosiciones.put("2,4", new Point(MARGEN + anchoTablero*2/3, MARGEN + altoTablero/3)); // E2

        // Fila central
        coordenadasPosiciones.put("3,0", new Point(MARGEN, MARGEN + altoTablero/2)); // A3
        coordenadasPosiciones.put("3,1", new Point(MARGEN + anchoTablero/6, MARGEN + altoTablero/2)); // B3
        coordenadasPosiciones.put("3,2", new Point(MARGEN + anchoTablero/3, MARGEN + altoTablero/2)); // C3
        coordenadasPosiciones.put("3,4", new Point(MARGEN + anchoTablero*2/3, MARGEN + altoTablero/2)); // E3
        coordenadasPosiciones.put("3,5", new Point(MARGEN + anchoTablero*5/6, MARGEN + altoTablero/2)); // F3
        coordenadasPosiciones.put("3,6", new Point(MARGEN + anchoTablero, MARGEN + altoTablero/2)); // G3

        // Cuadrado interior inferior
        coordenadasPosiciones.put("4,2", new Point(MARGEN + anchoTablero/3, MARGEN + altoTablero*2/3)); // C4
        coordenadasPosiciones.put("4,3", new Point(MARGEN + anchoTablero/2, MARGEN + altoTablero*2/3)); // D4
        coordenadasPosiciones.put("4,4", new Point(MARGEN + anchoTablero*2/3, MARGEN + altoTablero*2/3)); // E4

        // Cuadrado medio inferior
        coordenadasPosiciones.put("5,1", new Point(MARGEN + anchoTablero/6, MARGEN + altoTablero*5/6)); // B5
        coordenadasPosiciones.put("5,3", new Point(MARGEN + anchoTablero/2, MARGEN + altoTablero*5/6)); // D5
        coordenadasPosiciones.put("5,5", new Point(MARGEN + anchoTablero*5/6, MARGEN + altoTablero*5/6)); // F5

        // Cuadrado exterior inferior
        coordenadasPosiciones.put("6,0", new Point(MARGEN, MARGEN + altoTablero)); // A6
        coordenadasPosiciones.put("6,3", new Point(MARGEN + anchoTablero/2, MARGEN + altoTablero)); // D6
        coordenadasPosiciones.put("6,6", new Point(MARGEN + anchoTablero, MARGEN + altoTablero)); // G6
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        dibujarLineasTablero(g2d);
        dibujarPosiciones(g2d);
    }

    private void dibujarLineasTablero(Graphics2D g2d) {
        g2d.setColor(COLOR_LINEA);
        g2d.setStroke(new BasicStroke(2));

        int anchoTablero = 400;
        int altoTablero = 400;

        // Cuadrado exterior
        g2d.drawRect(MARGEN, MARGEN, anchoTablero, altoTablero);

        // Cuadrado medio
        g2d.drawRect(MARGEN + anchoTablero/6, MARGEN + altoTablero/6,
                     anchoTablero*2/3, altoTablero*2/3);

        // Cuadrado interior
        g2d.drawRect(MARGEN + anchoTablero/3, MARGEN + altoTablero/3,
                     anchoTablero/3, altoTablero/3);

        // Líneas horizontales
        // Superior
        g2d.drawLine(MARGEN + anchoTablero/2, MARGEN,
                     MARGEN + anchoTablero/2, MARGEN + altoTablero/3);
        // Inferior
        g2d.drawLine(MARGEN + anchoTablero/2, MARGEN + altoTablero*2/3,
                     MARGEN + anchoTablero/2, MARGEN + altoTablero);

        // Líneas verticales
        // Izquierda
        g2d.drawLine(MARGEN, MARGEN + altoTablero/2,
                     MARGEN + anchoTablero/3, MARGEN + altoTablero/2);
        // Derecha
        g2d.drawLine(MARGEN + anchoTablero*2/3, MARGEN + altoTablero/2,
                     MARGEN + anchoTablero, MARGEN + altoTablero/2);
    }

    private void dibujarPosiciones(Graphics2D g2d) {
        for (Map.Entry<String, Point> entrada : coordenadasPosiciones.entrySet()) {
            String clave = entrada.getKey();
            Point punto = entrada.getValue();

            String[] coords = clave.split(",");
            int fila = Integer.parseInt(coords[0]);
            int columna = Integer.parseInt(coords[1]);

            Color colorFondo = COLOR_VACIO;

            // Obtener el carácter de la posición desde el string del tablero
            char caracterPosicion = obtenerCaracterPosicion(fila, columna);

            // Determinar el color de la posición
            if (caracterPosicion == 'B') {
                colorFondo = COLOR_JUGADOR_UNO;
            } else if (caracterPosicion == 'N') {
                colorFondo = COLOR_JUGADOR_DOS;
            }

            // Si la posicion está resaltada, se sobreescribe el color
            if (posicionesResaltadas.getOrDefault(clave, false)) {
                colorFondo = COLOR_RESALTADO;
            }

            // Dibujar la posición
            g2d.setColor(colorFondo);
            g2d.fillOval(punto.x - RADIO_POSICION, punto.y - RADIO_POSICION,
                        RADIO_POSICION * 2, RADIO_POSICION * 2);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(punto.x - RADIO_POSICION, punto.y - RADIO_POSICION,
                        RADIO_POSICION * 2, RADIO_POSICION * 2);
        }
    }

    private char obtenerCaracterPosicion(int fila, int columna) {
        if (posicionesTablero.isEmpty()) return ' ';

        int indice = obtenerIndicePosicion(fila, columna);

        if (indice >= 0 && indice < posicionesTablero.length()) {
            return posicionesTablero.charAt(indice);
        }
        return ' ';
    }

    private int obtenerIndicePosicion(int fila, int columna) {
        switch (fila) {
            case 0:
                if (columna == 0) return 0;  // A0
                if (columna == 3) return 1;  // D0
                if (columna == 6) return 2;  // G0
                break;
            case 1:
                if (columna == 1) return 3;  // B1
                if (columna == 3) return 4;  // D1
                if (columna == 5) return 5;  // F1
                break;
            case 2:
                if (columna == 2) return 6;  // C2
                if (columna == 3) return 7;  // D2
                if (columna == 4) return 8;  // E2
                break;
            case 3:
                if (columna == 0) return 9;  // A3
                if (columna == 1) return 10; // B3
                if (columna == 2) return 11; // C3
                if (columna == 4) return 12; // E3
                if (columna == 5) return 13; // F3
                if (columna == 6) return 14; // G3
                break;
            case 4:
                if (columna == 2) return 15; // C4
                if (columna == 3) return 16; // D4
                if (columna == 4) return 17; // E4
                break;
            case 5:
                if (columna == 1) return 18; // B5
                if (columna == 3) return 19; // D5
                if (columna == 5) return 20; // F5
                break;
            case 6:
                if (columna == 0) return 21; // A6
                if (columna == 3) return 22; // D6
                if (columna == 6) return 23; // G6
                break;
        }
        return -1;
    }

    private void manejarClickTablero(int x, int y) {
        // Buscar la posición más cercana al click
        String posicionSeleccionada = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (Map.Entry<String, Point> entrada : coordenadasPosiciones.entrySet()) {
            Point punto = entrada.getValue();
            double distancia = Math.sqrt(Math.pow(x - punto.x, 2) + Math.pow(y - punto.y, 2));

            if (distancia <= RADIO_POSICION && distancia < distanciaMinima) {
                distanciaMinima = distancia;
                posicionSeleccionada = entrada.getKey();
            }
        }

        if (posicionSeleccionada != null) {
            String[] coords = posicionSeleccionada.split(",");
            int fila = Integer.parseInt(coords[0]);
            int columna = Integer.parseInt(coords[1]);
            vista.onPosicionClicked(fila, columna);
        }
    }

    public void actualizarTablero(String posiciones) {
        this.posicionesTablero = posiciones;
        repaint();
    }

    public void resaltarPosicion(int fila, int columna, boolean resaltar) {
        String clave = fila + "," + columna;
        posicionesResaltadas.put(clave, resaltar);
        repaint();
    }

    public void limpiarResaltados() {
        posicionesResaltadas.clear();
        repaint();
    }
}
