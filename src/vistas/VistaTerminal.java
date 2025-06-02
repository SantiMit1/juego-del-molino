package vistas;

import controlador.Controlador;
import modelo.Ficha;
import modelo.Jugador;
import modelo.Nodo;

import java.util.Scanner;

public class VistaTerminal implements IVista {
    private final Scanner sc = new Scanner(System.in);
    private final Controlador controlador;
    private final Jugador jugador;

    public VistaTerminal(Controlador controlador) {
        System.out.println("Nombre del jugador: ");
        this.jugador = new Jugador(sc.nextLine());
        this.controlador = controlador;
        controlador.setVista(this);
        controlador.agregarJugador(jugador);
    }

    @Override
    public void colocarFicha() {
        boolean flag = false;
        while (!flag) {
            System.out.println(jugador.getNombre() + ": Colocar ficha: ");
            System.out.println("Ingrese la fila donde quiere colocar la ficha: ");
            int fila = sc.nextInt();
            System.out.println("Ingrese la columna donde quiere colocar la ficha: ");
            int columna = sc.nextInt();
            flag = controlador.colocarFicha(fila, columna);
        }
    }

    @Override
    public void moverFicha() {
        boolean flag = false;
        while (!flag) {
            System.out.println(jugador.getNombre() + ": Mover ficha: ");
            System.out.println("Ingrese la fila de la ficha a mover: ");
            int filaOrigen = sc.nextInt();
            System.out.println("Ingrese la columna de la ficha a mover: ");
            int columnaOrigen = sc.nextInt();
            System.out.println("Ingrese la fila de destino: ");
            int filaDestino = sc.nextInt();
            System.out.println("Ingrese la columna de destino: ");
            int columnaDestino = sc.nextInt();
            flag = controlador.moverFicha(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
        }
    }

    @Override
    public void eliminarFicha() {
        boolean flag = false;
        while (!flag) {
            System.out.println(jugador.getNombre() + ": Eliminar ficha: ");
            System.out.println("Ingrese la fila de la ficha a eliminar: ");
            int fila = sc.nextInt();
            System.out.println("Ingrese la columna de la ficha a eliminar: ");
            int columna = sc.nextInt();
            flag = controlador.eliminarFicha(fila, columna);
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    private String imprimirFicha(int fila, int columna, Nodo[][] nodos) {
        Nodo nodo = nodos[fila][columna];
        if (nodo == null) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        Ficha ficha = nodo.getFicha();
        return ficha != null ? ficha.getColor().toString().substring(0, 1) : "@";
    }

    @Override
    public void mostrarTablero(Nodo[][] nodos) {
        // si estas corrigiendo esto te pido perdon, no se me ocurrio una forma mejor de hacerlo
        System.out.println(" " + imprimirFicha(0, 0, nodos) + "-----------" + imprimirFicha(0, 3, nodos) + "-----------" + imprimirFicha(0, 6, nodos));
        System.out.println(" |           |           |");
        System.out.println(" |   " + imprimirFicha(1, 1, nodos) + "-------" + imprimirFicha(1, 3, nodos) + "-------" + imprimirFicha(1, 5, nodos) + "   |");
        System.out.println(" |   |       |       |   |");
        System.out.println(" |   |   " + imprimirFicha(2, 2, nodos) + "---" + imprimirFicha(2, 3, nodos) + "---" + imprimirFicha(2, 4, nodos) + "   |   |");
        System.out.println(" |   |   |       |   |   |");
        System.out.println(imprimirFicha(3, 0, nodos) + "---" + imprimirFicha(3, 1, nodos) + "---" + imprimirFicha(3, 2, nodos) + "       " + imprimirFicha(3, 4, nodos) + "---" + imprimirFicha(3, 5, nodos) + "---" + imprimirFicha(3, 6, nodos));
        System.out.println(" |   |   |       |   |   |");
        System.out.println(" |   |   " + imprimirFicha(4, 2, nodos) + "---" + imprimirFicha(4, 3, nodos) + "---" + imprimirFicha(4, 4, nodos) + "   |   |");
        System.out.println(" |   |       |       |   |");
        System.out.println(" |   " + imprimirFicha(5, 1, nodos) + "-------" + imprimirFicha(5, 3, nodos) + "-------" + imprimirFicha(5, 5, nodos) + "   |");
        System.out.println(" |           |           |");
        System.out.println(" " + imprimirFicha(6, 0, nodos) + "-----------" + imprimirFicha(6, 3, nodos) + "-----------" + imprimirFicha(6, 6, nodos));
    }
}
