package vistas.grafica;

import controlador.Controlador;
import vistas.Modos;
import vistas.Vista;

import javax.swing.*;
import java.awt.*;

public class VistaGrafica extends Vista {
    private JFrame frame;
    private TableroGrafico tableroGrafico;
    private JLabel faseLabel;
    private JLabel accionLabel;
    private JTextArea mensajesArea;

    // Variables para control del estado
    private int[] posicionOrigen = null;
    private boolean esperandoAccion = false;

    public VistaGrafica(Controlador controlador) {
        super(controlador);

        inicializarUI();

        boolean nombreValido = false;
        while (!nombreValido) {
            String nombreJugadorNuevo = JOptionPane.showInputDialog(frame, "Nombre del jugador:");
            if (nombreJugadorNuevo != null && !nombreJugadorNuevo.trim().isEmpty()) {
                setNombreJugador(nombreJugadorNuevo);
                nombreValido = controlador.crearJugador(nombreJugadorNuevo);
            }
        }

    }

    private Color[] seleccionarColores() {
        Color[] coloresDisponibles = {Color.WHITE, Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PINK};
        String[] nombresColores = {"Blanco", "Negro", "Rojo", "Azul", "Verde", "Naranja", "Rosa"};

        Color color1 = null;
        Color color2 = null;
        int indiceColor1 = -1;
        int indiceColor2 = -1;

        while (color1 == null) {
            indiceColor1 = JOptionPane.showOptionDialog(null, "Seleccione el color del Jugador 1",
                    "Color Jugador 1", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, nombresColores, nombresColores[0]);
            color1 = (indiceColor1 >= 0) ? coloresDisponibles[indiceColor1] : Color.WHITE;
        }

        while (color2 == null || color1 == color2) {
            indiceColor2 = JOptionPane.showOptionDialog(null, "Seleccione el color del Jugador 2",
                    "Color Jugador 2", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, nombresColores, nombresColores[0]);
            color2 = (indiceColor2 >= 0) ? coloresDisponibles[indiceColor2] : Color.BLACK;
            if (color1 == color2) {
                JOptionPane.showMessageDialog(null, "El color ya está seleccionado. Elija otro color.");
            }

        }

        return new Color[]{color1, color2};
    }

    private void inicializarUI() {
        frame = new JFrame("Juego del Molino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 800);

        // Panel superior con información del juego
        JPanel infoPanel = crearPanelInformacion();
        frame.add(infoPanel, BorderLayout.NORTH);

        // Panel inferior con mensajes
        JPanel mensajesPanel = crearPanelMensajes();
        frame.add(mensajesPanel, BorderLayout.SOUTH);

        // Panel central con el tablero
        Color[] colores = seleccionarColores();
        tableroGrafico = new TableroGrafico(this, colores[0], colores[1]);
        frame.add(tableroGrafico, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createTitledBorder("Estado del Juego"));

        faseLabel = new JLabel("Fase: Esperando...", SwingConstants.CENTER);
        faseLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        accionLabel = new JLabel("Acción: Ninguna", SwingConstants.CENTER);
        accionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        panel.add(faseLabel);
        panel.add(accionLabel);

        return panel;
    }

    private JPanel crearPanelMensajes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Mensajes"));

        mensajesArea = new JTextArea(5, 0);
        mensajesArea.setEditable(false);
        mensajesArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(mensajesArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void onPosicionClicked(int fila, int columna) {
        if (!esperandoAccion) return;

        switch (modo) {
            case COLOCAR:
                boolean colocarExito = controlador.colocarFicha(fila, columna);
                if (colocarExito) {
                    finalizarAccion();
                } else {
                    mostrarMensaje("Posición inválida. Seleccione otra posición.");
                }
                break;

            case MOVER:
                if (posicionOrigen == null) {
                    // Primera selección: origen
                    posicionOrigen = new int[]{fila, columna};
                    mostrarMensaje("Posición origen seleccionada: " + convertirCoordenadasAPosicion(fila, columna));
                    mostrarMensaje("Ahora seleccione la posición de destino:");
                    tableroGrafico.resaltarPosicion(fila, columna, true);
                } else {
                    // Segunda selección: destino
                    boolean moverExito = controlador.moverFicha(posicionOrigen[0], posicionOrigen[1], fila, columna);
                    if (moverExito) {
                        tableroGrafico.resaltarPosicion(posicionOrigen[0], posicionOrigen[1], false);
                        finalizarAccion();
                    } else {
                        mostrarMensaje("Movimiento inválido. Seleccione otra posición.");
                        posicionOrigen = null;
                        tableroGrafico.limpiarResaltados();
                    }
                }
                break;

            case ELIMINAR:
                boolean eliminarExito = controlador.eliminarFicha(fila, columna);
                if (eliminarExito) {
                    finalizarAccion();
                } else {
                    mostrarMensaje("No se puede eliminar esa ficha. Seleccione otra posición.");
                }
                break;
        }
    }

    private void finalizarAccion() {
        esperandoAccion = false;
        modo = null;
        posicionOrigen = null;
        tableroGrafico.limpiarResaltados();
        accionLabel.setText("Acción: Esperando próximo turno...");
    }

    private String convertirCoordenadasAPosicion(int fila, int columna) {
        char letra = (char) ('A' + columna);
        return letra + String.valueOf(fila);
    }

    @Override
    public void colocarFicha() {
        SwingUtilities.invokeLater(() -> {
            modo = Modos.COLOCAR;
            esperandoAccion = true;
            posicionOrigen = null;
            faseLabel.setText("Fase: Colocando fichas");
            accionLabel.setText("Acción: Haga clic en una posición vacía para colocar una ficha");
            mostrarMensaje(nombreJugador + ": Seleccione dónde colocar la ficha");
        });
    }

    @Override
    public void moverFicha() {
        SwingUtilities.invokeLater(() -> {
            modo = Modos.MOVER;
            esperandoAccion = true;
            posicionOrigen = null;
            faseLabel.setText("Fase: Moviendo fichas");
            accionLabel.setText("Acción: Seleccione la ficha a mover, luego la posición de destino");
            mostrarMensaje(nombreJugador + ": Seleccione la ficha que desea mover");
        });
    }

    @Override
    public void eliminarFicha() {
        SwingUtilities.invokeLater(() -> {
            modo = Modos.ELIMINAR;
            esperandoAccion = true;
            posicionOrigen = null;
            faseLabel.setText("Fase: Eliminando ficha del oponente");
            accionLabel.setText("Acción: Seleccione una ficha del oponente para eliminar");
            mostrarMensaje(nombreJugador + ": Seleccione la ficha del oponente a eliminar");
        });
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            mensajesArea.append(mensaje + "\n");
            mensajesArea.setCaretPosition(mensajesArea.getDocument().getLength());
        });
    }

    @Override
    public void mostrarTablero(String posiciones) {
        SwingUtilities.invokeLater(() -> {
            tableroGrafico.actualizarTablero(posiciones);
        });
    }
}
