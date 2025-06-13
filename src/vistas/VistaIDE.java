package vistas;

import controlador.Controlador;
import modelo.Ficha;
import modelo.Posicion;

import java.util.Scanner;

public class VistaIDE implements IVista {
    private final Scanner sc = new Scanner(System.in);
    private final Controlador controlador;
    private final String nombreJugador;

    public VistaIDE(Controlador controlador) {
        this.controlador = controlador;
        controlador.setVista(this);
        System.out.println("Nombre del jugador: ");
        this.nombreJugador = sc.nextLine();
        controlador.crearJugador(nombreJugador);
    }

    @Override
    public void colocarFicha() {
        boolean flag = false;
        while (!flag) {
            System.out.println(nombreJugador + ": Colocar ficha: ");
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
            System.out.println(nombreJugador + ": Mover ficha: ");
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
            System.out.println(nombreJugador + ": Eliminar ficha: ");
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
        // si estas corrigiendo esto te pido perdon, no se me ocurrio una forma mejor de hacerlo
        System.out.println(" " + imprimirFicha(0, 0, posiciones) + "-----------" + imprimirFicha(0, 3, posiciones) + "-----------" + imprimirFicha(0, 6, posiciones));
        System.out.println(" |           |           |");
        System.out.println(" |   " + imprimirFicha(1, 1, posiciones) + "-------" + imprimirFicha(1, 3, posiciones) + "-------" + imprimirFicha(1, 5, posiciones) + "   |");
        System.out.println(" |   |       |       |   |");
        System.out.println(" |   |   " + imprimirFicha(2, 2, posiciones) + "---" + imprimirFicha(2, 3, posiciones) + "---" + imprimirFicha(2, 4, posiciones) + "   |   |");
        System.out.println(" |   |   |       |   |   |");
        System.out.println(imprimirFicha(3, 0, posiciones) + "---" + imprimirFicha(3, 1, posiciones) + "---" + imprimirFicha(3, 2, posiciones) + "       " + imprimirFicha(3, 4, posiciones) + "---" + imprimirFicha(3, 5, posiciones) + "---" + imprimirFicha(3, 6, posiciones));
        System.out.println(" |   |   |       |   |   |");
        System.out.println(" |   |   " + imprimirFicha(4, 2, posiciones) + "---" + imprimirFicha(4, 3, posiciones) + "---" + imprimirFicha(4, 4, posiciones) + "   |   |");
        System.out.println(" |   |       |       |   |");
        System.out.println(" |   " + imprimirFicha(5, 1, posiciones) + "-------" + imprimirFicha(5, 3, posiciones) + "-------" + imprimirFicha(5, 5, posiciones) + "   |");
        System.out.println(" |           |           |");
        System.out.println(" " + imprimirFicha(6, 0, posiciones) + "-----------" + imprimirFicha(6, 3, posiciones) + "-----------" + imprimirFicha(6, 6, posiciones));
    }
}
