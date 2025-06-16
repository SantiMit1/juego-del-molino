package vistas.terminal;

import controlador.Controlador;
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
    private Modos tipoEntradaActual = null;
    private int[] valoresEntrada;
    private int valorActual = 0;

    public VistaTerminal(Controlador controlador) {
        super(controlador);

        inicializarUI();

        // Solicitar nombre del jugador
        boolean nombreValido = false;

        while (!nombreValido) {
            setNombreJugador(JOptionPane.showInputDialog(frame, "Nombre del jugador:"));
            if (nombreJugador != null && !nombreJugador.trim().isEmpty()) {
                nombreValido = true;
                controlador.crearJugador(nombreJugador);
                mostrarMensaje("¡Bienvenido " + nombreJugador + "!");
            }
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
            int valor = Integer.parseInt(inputField.getText().trim());
            inputField.setText("");

            valoresEntrada[valorActual] = valor;
            mostrarMensaje("Valor ingresado: " + valor);
            valorActual++;

            switch (tipoEntradaActual) {
                case Modos.COLOCAR:
                    if (valorActual == 2) {
                        boolean success = controlador.colocarFicha(valoresEntrada[0], valoresEntrada[1]);
                        if (success) {
                            deshabilitarEntrada();
                            valorActual = 0;
                        } else {
                            valorActual = 0;
                            mostrarMensaje("Intente nuevamente:");
                            mostrarMensaje("Ingrese la fila donde quiere colocar la ficha:");
                        }
                    } else {
                        mostrarMensaje("Ingrese la columna donde quiere colocar la ficha:");
                    }
                    break;

                case Modos.MOVER:
                    if (valorActual == 4) {
                        boolean success = controlador.moverFicha(
                                valoresEntrada[0], valoresEntrada[1],
                                valoresEntrada[2], valoresEntrada[3]
                        );
                        if (success) {
                            deshabilitarEntrada();
                            valorActual = 0;
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

                case Modos.ELIMINAR:
                    if (valorActual == 2) {
                        boolean success = controlador.eliminarFicha(valoresEntrada[0], valoresEntrada[1]);
                        if (success) {
                            deshabilitarEntrada();
                            valorActual = 0;
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
        tipoEntradaActual = null;
        valorActual = 0;
    }

    @Override
    public void colocarFicha() {
        SwingUtilities.invokeLater(() -> {
            tipoEntradaActual = Modos.COLOCAR;
            valoresEntrada = new int[2];
            valorActual = 0;
            mostrarMensaje(nombreJugador + ": Colocar ficha:");
            mostrarMensaje("Ingrese la fila donde quiere colocar la ficha:");

            habilitarEntrada();
            estadoLabel.setText("Acción: Colocar ficha");
        });
    }

    @Override
    public void moverFicha() {
        SwingUtilities.invokeLater(() -> {
            tipoEntradaActual = Modos.MOVER;
            valoresEntrada = new int[4];
            valorActual = 0;
            mostrarMensaje(nombreJugador + ": Mover ficha:");
            mostrarMensaje("Ingrese la fila de la ficha a mover:");

            habilitarEntrada();
            estadoLabel.setText("Acción: Mover ficha");
        });
    }

    @Override
    public void eliminarFicha() {
        SwingUtilities.invokeLater(() -> {
            tipoEntradaActual = Modos.ELIMINAR;
            valoresEntrada = new int[2];
            valorActual = 0;
            mostrarMensaje(nombreJugador + ": Eliminar ficha:");
            mostrarMensaje("Ingrese la fila de la ficha a eliminar:");

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

        tablero.append(" ").append(posiciones.charAt(0)).append("-----------").append(posiciones.charAt(1)).append("-----------").append(posiciones.charAt(2)).append("\n");
        tablero.append(" |           |           |\n");
        tablero.append(" |   ").append(posiciones.charAt(3)).append("-------").append(posiciones.charAt(4)).append("-------").append(posiciones.charAt(5)).append("   |\n");
        tablero.append(" |   |       |       |   |\n");
        tablero.append(" |   |   ").append(posiciones.charAt(6)).append("---").append(posiciones.charAt(7)).append("---").append(posiciones.charAt(8)).append("   |   |\n");
        tablero.append(" |   |   |       |   |   |\n");
        tablero.append(" ").append(posiciones.charAt(9)).append("---").append(posiciones.charAt(10)).append("---").append(posiciones.charAt(11)).append("       ").append(posiciones.charAt(12)).append("---").append(posiciones.charAt(13)).append("---").append(posiciones.charAt(14)).append("\n");
        tablero.append(" |   |   |       |   |   |\n");
        tablero.append(" |   |   ").append(posiciones.charAt(15)).append("---").append(posiciones.charAt(16)).append("---").append(posiciones.charAt(17)).append("   |   |\n");
        tablero.append(" |   |       |       |   |\n");
        tablero.append(" |   ").append(posiciones.charAt(18)).append("-------").append(posiciones.charAt(19)).append("-------").append(posiciones.charAt(20)).append("   |\n");
        tablero.append(" |           |           |\n");
        tablero.append(" ").append(posiciones.charAt(21)).append("-----------").append(posiciones.charAt(22)).append("-----------").append(posiciones.charAt(23)).append("\n");
        mostrarMensaje("\n=== TABLERO ACTUAL ===");
        mostrarMensaje(tablero.toString());
    }
}
