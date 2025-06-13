package vistas.terminal;

import controlador.Controlador;
import modelo.Ficha;
import modelo.Posicion;
import vistas.IVista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaTerminal implements IVista {
    private final Controlador controlador;
    private final String nombreJugador;

    // Componentes de la interfaz
    private JFrame frame;
    private JTextArea logArea;
    private JTextField inputField;
    private JLabel estadoLabel;

    // Variables para control del estado de entrada
    private boolean esperandoEntrada = false;
    private String tipoEntradaActual = "";
    private int[] valoresEntrada;
    private int valorActual = 0;

    public VistaTerminal(Controlador controlador) {
        this.controlador = controlador;
        controlador.setVista(this);

        inicializarUI();

        // Solicitar nombre del jugador
        nombreJugador = JOptionPane.showInputDialog(frame, "Nombre del jugador:");
        if (nombreJugador != null && !nombreJugador.trim().isEmpty()) {
            controlador.crearJugador(nombreJugador);
            mostrarMensaje("¡Bienvenido " + nombreJugador + "!");
        } else {
            mostrarMensaje("Nombre no válido. Usando 'Jugador'");
            controlador.crearJugador("Jugador");
        }
    }

    private void inicializarUI() {
        frame = new JFrame("Juego del Molino - Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Panel superior con etiqueta de estado
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        estadoLabel = new JLabel("Esperando...");
        topPanel.add(estadoLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // Área de texto para mostrar logs y tablero
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(logArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con entrada de texto y botón
        JPanel bottomPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setEnabled(false); // Deshabilitado inicialmente

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarEntrada();
            }
        });

        bottomPanel.add(inputField, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void procesarEntrada() {
        if (!esperandoEntrada) return;

        try {
            int valor = Integer.parseInt(inputField.getText().trim());
            inputField.setText("");

            valoresEntrada[valorActual] = valor;
            valorActual++;

            switch (tipoEntradaActual) {
                case "colocar":
                    if (valorActual == 2) {
                        boolean success = controlador.colocarFicha(valoresEntrada[0], valoresEntrada[1]);
                        if (success) {
                            deshabilitarEntrada();
                        } else {
                            valorActual = 0;
                            mostrarMensaje("Intente nuevamente:");
                            mostrarMensaje("Ingrese la fila donde quiere colocar la ficha:");
                        }
                    } else {
                        mostrarMensaje("Ingrese la columna donde quiere colocar la ficha:");
                    }
                    break;

                case "mover":
                    if (valorActual == 4) {
                        boolean success = controlador.moverFicha(
                            valoresEntrada[0], valoresEntrada[1],
                            valoresEntrada[2], valoresEntrada[3]
                        );
                        if (success) {
                            deshabilitarEntrada();
                        } else {
                            valorActual = 0;
                            mostrarMensaje("Intente nuevamente:");
                            mostrarMensaje("Ingrese la fila de la ficha a mover:");
                        }
                    } else {
                        String[] mensajes = {
                            "Ingrese la columna de la ficha a mover:",
                            "Ingrese la fila de destino:",
                            "Ingrese la columna de destino:"
                        };
                        mostrarMensaje(mensajes[valorActual - 1]);
                    }
                    break;

                //TODO fix
                case "eliminar":
                    if (valorActual == 2) {
                        boolean success = controlador.eliminarFicha(valoresEntrada[0], valoresEntrada[1]);
                        if (success) {
                            deshabilitarEntrada();
                        } else {
                            valorActual = 0;
                            mostrarMensaje("Intente nuevamente:");
                            mostrarMensaje("Ingrese la fila de la ficha a eliminar:");
                        }
                    } else {
                        mostrarMensaje("Ingrese la columna de la ficha a eliminar:");
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error: Ingrese un número válido");
        }
    }

    private void habilitarEntrada() {
        esperandoEntrada = true;
        inputField.setEnabled(true);
        inputField.requestFocus();
    }

    private void deshabilitarEntrada() {
        esperandoEntrada = false;
        inputField.setEnabled(false);
        tipoEntradaActual = "";
        valorActual = 0;
    }

    @Override
    public void colocarFicha() {
        tipoEntradaActual = "colocar";
        valoresEntrada = new int[2];
        valorActual = 0;
        mostrarMensaje(nombreJugador + ": Colocar ficha:");
        mostrarMensaje("Ingrese la fila donde quiere colocar la ficha:");
        habilitarEntrada();
        estadoLabel.setText("Acción: Colocar ficha");
    }

    @Override
    public void moverFicha() {
        tipoEntradaActual = "mover";
        valoresEntrada = new int[4];
        valorActual = 0;
        mostrarMensaje(nombreJugador + ": Mover ficha:");
        mostrarMensaje("Ingrese la fila de la ficha a mover:");
        habilitarEntrada();
        estadoLabel.setText("Acción: Mover ficha");
    }

    @Override
    public void eliminarFicha() {
        tipoEntradaActual = "eliminar";
        valoresEntrada = new int[2];
        valorActual = 0;
        mostrarMensaje(nombreJugador + ": Eliminar ficha:");
        mostrarMensaje("Ingrese la fila de la ficha a eliminar:");

        SwingUtilities.invokeLater(() -> {
            habilitarEntrada();
            estadoLabel.setText("Acción: Eliminar ficha");
        });
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(mensaje + "\n");
            // Hacer scroll hacia abajo para mostrar el mensaje más reciente
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private String imprimirFicha(int fila, int columna, Posicion[][] posiciones) {
        Posicion posicion = posiciones[fila][columna];
        if (posicion == null) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        Ficha ficha = posicion.getFicha();
        return ficha != null ? ficha.getColor().toString().substring(0, 1) : "@";
    }

    @Override
    public void mostrarTablero(Posicion[][] posiciones) {
        StringBuilder tablero = new StringBuilder();

        tablero.append(" ").append(imprimirFicha(0, 0, posiciones)).append("-----------").append(imprimirFicha(0, 3, posiciones)).append("-----------").append(imprimirFicha(0, 6, posiciones)).append("\n");
        tablero.append(" |           |           |\n");
        tablero.append(" |   ").append(imprimirFicha(1, 1, posiciones)).append("-------").append(imprimirFicha(1, 3, posiciones)).append("-------").append(imprimirFicha(1, 5, posiciones)).append("   |\n");
        tablero.append(" |   |       |       |   |\n");
        tablero.append(" |   |   ").append(imprimirFicha(2, 2, posiciones)).append("---").append(imprimirFicha(2, 3, posiciones)).append("---").append(imprimirFicha(2, 4, posiciones)).append("   |   |\n");
        tablero.append(" |   |   |       |   |   |\n");
        tablero.append(imprimirFicha(3, 0, posiciones)).append("---").append(imprimirFicha(3, 1, posiciones)).append("---").append(imprimirFicha(3, 2, posiciones)).append("       ").append(imprimirFicha(3, 4, posiciones)).append("---").append(imprimirFicha(3, 5, posiciones)).append("---").append(imprimirFicha(3, 6, posiciones)).append("\n");
        tablero.append(" |   |   |       |   |   |\n");
        tablero.append(" |   |   ").append(imprimirFicha(4, 2, posiciones)).append("---").append(imprimirFicha(4, 3, posiciones)).append("---").append(imprimirFicha(4, 4, posiciones)).append("   |   |\n");
        tablero.append(" |   |       |       |   |\n");
        tablero.append(" |   ").append(imprimirFicha(5, 1, posiciones)).append("-------").append(imprimirFicha(5, 3, posiciones)).append("-------").append(imprimirFicha(5, 5, posiciones)).append("   |\n");
        tablero.append(" |           |           |\n");
        tablero.append(" ").append(imprimirFicha(6, 0, posiciones)).append("-----------").append(imprimirFicha(6, 3, posiciones)).append("-----------").append(imprimirFicha(6, 6, posiciones)).append("\n");

        mostrarMensaje("\n=== TABLERO ACTUAL ===");
        mostrarMensaje(tablero.toString());
    }
}
