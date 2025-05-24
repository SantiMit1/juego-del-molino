package vistas;

import controlador.Controlador;
import modelo.Jugador;

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
}
