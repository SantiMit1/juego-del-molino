package vistas.terminal;

import controlador.Controlador;
import vistas.Modos;
import vistas.Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaTerminal extends Vista {
    // Componentes de la interfaz
    private JFrame frame;
    private JTextArea logArea;
    private JTextField inputField;
    private JLabel estadoLabel;

    // Variables para control del estado de entrada
    private boolean esperandoEntrada = false;
    private String[] entradasPosicion;
    private int valorActual = 0;

    public VistaTerminal(Controlador controlador) {
        super(controlador);

        boolean nombreValido = false;
        while (!nombreValido) {
            String nombreJugador = JOptionPane.showInputDialog(frame, "Nombre del jugador:");
            if (nombreJugador != null && !nombreJugador.trim().isEmpty()) {
                setNombreJugador(nombreJugador);
                nombreValido = controlador.crearJugador(nombreJugador);
            }
        }

        inicializarUI();
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
        inputField.setEnabled(false);

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
            String entrada = inputField.getText().trim().toUpperCase();
            inputField.setText("");

            if (entrada.length() < 2) {
                mostrarMensaje("Error: Formato inválido. Ingrese una letra (A-G) seguida de un número (0-6). Ejemplo: A0");
                return;
            }

            entradasPosicion[valorActual] = entrada;
            mostrarMensaje("Posición ingresada: " + entrada);
            valorActual++;

            switch (modo) {
                case Modos.COLOCAR:
                    if (valorActual == 1) {
                        int[] coordenadas = convertirPosicionACoordenadas(entrada);
                        if (coordenadas != null) {
                            boolean success = controlador.colocarFicha(coordenadas[0], coordenadas[1]);
                            if (success) {
                                deshabilitarEntrada();
                            } else {
                                valorActual = 0;
                                mostrarMensaje("Posición inválida. Intente nuevamente:");
                                mostrarMensaje("Ingrese la posición donde quiere colocar la ficha (ejemplo: A0):");
                            }
                        } else {
                            valorActual = 0;
                            mostrarMensaje("Formato inválido. Intente nuevamente:");
                            mostrarMensaje("Ingrese la posición donde quiere colocar la ficha (ejemplo: A0):");
                        }
                    }
                    break;

                case Modos.MOVER:
                    if (valorActual == 1) {
                        mostrarMensaje("Ingrese la posición de destino (ejemplo: B3):");
                    } else if (valorActual == 2) {
                        int[] coordenadasOrigen = convertirPosicionACoordenadas(entradasPosicion[0]);
                        int[] coordenadasDestino = convertirPosicionACoordenadas(entradasPosicion[1]);

                        if (coordenadasOrigen != null && coordenadasDestino != null) {
                            boolean success = controlador.moverFicha(
                                    coordenadasOrigen[0], coordenadasOrigen[1],
                                    coordenadasDestino[0], coordenadasDestino[1]
                            );
                            if (success) {
                                deshabilitarEntrada();
                            } else {
                                valorActual = 0;
                                mostrarMensaje("Movimiento inválido. Intente nuevamente:");
                                mostrarMensaje("Ingrese la posición de la ficha a mover (ejemplo: A0):");
                            }
                        } else {
                            valorActual = 0;
                            mostrarMensaje("Formato inválido. Intente nuevamente:");
                            mostrarMensaje("Ingrese la posición de la ficha a mover (ejemplo: A0):");
                        }
                    }
                    break;

                case Modos.ELIMINAR:
                    if (valorActual == 1) {
                        int[] coordenadas = convertirPosicionACoordenadas(entrada);
                        if (coordenadas != null) {
                            boolean success = controlador.eliminarFicha(coordenadas[0], coordenadas[1]);
                            if (success) {
                                deshabilitarEntrada();
                            } else {
                                valorActual = 0;
                                mostrarMensaje("Posición inválida. Intente nuevamente:");
                                mostrarMensaje("Ingrese la posición de la ficha a eliminar (ejemplo: A0):");
                            }
                        } else {
                            valorActual = 0;
                            mostrarMensaje("Formato inválido. Intente nuevamente:");
                            mostrarMensaje("Ingrese la posición de la ficha a eliminar (ejemplo: A0):");
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            mostrarMensaje("Error: Formato inválido. Ingrese una letra (A-G) seguida de un número (0-6). Ejemplo: A0");
        }
    }

    private int[] convertirPosicionACoordenadas(String posicion) {
        if (posicion == null || posicion.length() < 2) {
            return null;
        }

        char primerCaracter = posicion.charAt(0);
        char segundoCaracter = posicion.charAt(1);

        char letraColumna;
        char numeroFila;

        if (Character.isLetter(primerCaracter) && Character.isDigit(segundoCaracter)) {
            letraColumna = primerCaracter;
            numeroFila = segundoCaracter;
        } else if (Character.isDigit(primerCaracter) && Character.isLetter(segundoCaracter)) {
            numeroFila = primerCaracter;
            letraColumna = segundoCaracter;
        } else {
            return null;
        }

        if (letraColumna < 'A' || letraColumna > 'G') {
            return null;
        }

        if (numeroFila < '0' || numeroFila > '6') {
            return null;
        }

        int fila = numeroFila - '0';
        int columna = letraColumna - 'A';

        return new int[] {fila, columna};
    }

    private void habilitarEntrada() {
        esperandoEntrada = true;
        inputField.setEnabled(true);
        inputField.requestFocus();
    }

    private void deshabilitarEntrada() {
        esperandoEntrada = false;
        inputField.setEnabled(false);
        modo = null;
        valorActual = 0;
    }

    @Override
    public void colocarFicha() {
        SwingUtilities.invokeLater(() -> {
            modo = Modos.COLOCAR;
            entradasPosicion = new String[2];
            valorActual = 0;
            mostrarMensaje(nombreJugador + ": Colocar ficha:");
            mostrarMensaje("Ingrese la posición donde quiere colocar la ficha (ejemplo: A0):");

            habilitarEntrada();
            estadoLabel.setText("Acción: Colocar ficha");
        });
    }

    @Override
    public void moverFicha() {
        SwingUtilities.invokeLater(() -> {
            modo = Modos.MOVER;
            entradasPosicion = new String[2];
            valorActual = 0;
            mostrarMensaje(nombreJugador + ": Mover ficha:");
            mostrarMensaje("Ingrese la posición de la ficha a mover (ejemplo: A0):");

            habilitarEntrada();
            estadoLabel.setText("Acción: Mover ficha");
        });
    }

    @Override
    public void eliminarFicha() {
        SwingUtilities.invokeLater(() -> {
            modo = Modos.ELIMINAR;
            entradasPosicion = new String[2];
            valorActual = 0;
            mostrarMensaje(nombreJugador + ": Eliminar ficha:");
            mostrarMensaje("Ingrese la posición de la ficha a eliminar (ejemplo: A0):");

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

    @Override
    public void mostrarTablero(String posiciones) {
        StringBuilder tablero = new StringBuilder();
        tablero.append("  A   B   C   D   E   F   G\n");
        tablero.append("0 ").append(posiciones.charAt(0)).append("-----------").append(posiciones.charAt(1)).append("-----------").append(posiciones.charAt(2)).append("\n");
        tablero.append("  |           |           |\n");
        tablero.append("1 |   ").append(posiciones.charAt(3)).append("-------").append(posiciones.charAt(4)).append("-------").append(posiciones.charAt(5)).append("   |\n");
        tablero.append("  |   |       |       |   |\n");
        tablero.append("2 |   |   ").append(posiciones.charAt(6)).append("---").append(posiciones.charAt(7)).append("---").append(posiciones.charAt(8)).append("   |   |\n");
        tablero.append("  |   |   |       |   |   |\n");
        tablero.append("3 ").append(posiciones.charAt(9)).append("---").append(posiciones.charAt(10)).append("---").append(posiciones.charAt(11)).append("       ").append(posiciones.charAt(12)).append("---").append(posiciones.charAt(13)).append("---").append(posiciones.charAt(14)).append("\n");
        tablero.append("  |   |   |       |   |   |\n");
        tablero.append("4 |   |   ").append(posiciones.charAt(15)).append("---").append(posiciones.charAt(16)).append("---").append(posiciones.charAt(17)).append("   |   |\n");
        tablero.append("  |   |       |       |   |\n");
        tablero.append("5 |   ").append(posiciones.charAt(18)).append("-------").append(posiciones.charAt(19)).append("-------").append(posiciones.charAt(20)).append("   |\n");
        tablero.append("  |           |           |\n");
        tablero.append("6 ").append(posiciones.charAt(21)).append("-----------").append(posiciones.charAt(22)).append("-----------").append(posiciones.charAt(23)).append("\n");
        mostrarMensaje("\n=== TABLERO ACTUAL ===");
        mostrarMensaje(tablero.toString());
    }
}
