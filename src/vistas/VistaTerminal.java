package vistas;

import controlador.Controlador;
import modelo.Jugador;
import observer.Notificaciones;
import observer.Observer;
import java.util.Scanner;

public class VistaTerminal implements Observer {
    private final Scanner sc = new Scanner(System.in);
    private final Controlador controlador;
    private final Jugador jugador;

    public VistaTerminal(Controlador controlador) {
        System.out.println("Nombre del jugador: ");
        this.jugador = new Jugador(sc.nextLine());
        this.controlador = controlador;
        controlador.agregarObserver(this);
        controlador.agregarJugador(jugador);
    }

    public void colocarFicha() {
        System.out.println(jugador.getNombre() + ": Colocar ficha (fila columna): ");
        int fila = sc.nextInt();
        int columna = sc.nextInt();
        controlador.colocarFicha(fila, columna);
    }

    public void moverFicha() {
        System.out.println(jugador.getNombre() + ": Mover ficha (filaOrigen columnaOrigen filaDestino columnaDestino): ");
        int filaOrigen = sc.nextInt();
        int columnaOrigen = sc.nextInt();
        int filaDestino = sc.nextInt();
        int columnaDestino = sc.nextInt();
        controlador.moverFicha(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
    }

    public void eliminarFicha() {
        System.out.println(jugador.getNombre() + ": Eliminar ficha (fila columna): ");
        int fila = sc.nextInt();
        int columna = sc.nextInt();
        controlador.eliminarFicha(fila, columna);
    }

    @Override
    public void notificar(Notificaciones notificacion) {
        System.out.println("Notificación recibida");
        switch(notificacion) {
            case INICIO:
                System.out.println("El juego ha comenzado.");
                break;
            case COLOCAR:
                System.out.println("Es tu turno de colocar una ficha.");
                colocarFicha();
                break;
            case MOVER:
                System.out.println("Es tu turno de mover una ficha.");
                moverFicha();
                break;
            case MOLINO:
                System.out.println("¡Has formado un molino!");
                eliminarFicha();
                break;
            case FIN:
                System.out.println("El juego ha terminado.");
                break;
        }
    }
}
