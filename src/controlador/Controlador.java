package controlador;

import modelo.Juego;
import modelo.Jugador;
import modelo.Tablero;
import modelo.enums.EstadoFicha;
import observer.Notificaciones;
import observer.Observer;

public class Controlador {
    private final Juego juego;

    public Controlador() {
        this.juego = new Juego(new Tablero());
    }

    public void agregarObserver(Observer vista) {
        juego.agregarObservador(vista);
        vista.notificar(Notificaciones.ESPERA);
    }

    public void agregarJugador(Jugador jugador) {
        juego.agregarJugador(jugador);
    }

    public void eliminarObserver(Observer vista) {
        juego.eliminarObservador(vista);
    }

    public boolean colocarFicha(int fila, int columna) {
        try {
            juego.colocarFicha(fila, columna, juego.getJugadorActual().obtenerFichasPorEstado(EstadoFicha.EN_MANO).getFirst());
            return true;
        } catch (Exception e) {
            System.out.println("Error al colocar ficha: " + e.getMessage());
            return false;
        }
    }

    public boolean moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        try {
            juego.moverFicha(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
            return true;
        } catch (Exception e) {
            System.out.println("Error al mover ficha: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarFicha(int fila, int columna) {
        try {
            juego.eliminarFicha(fila, columna);
            return true;
        } catch (Exception e) {
            System.out.println("Error al eliminar ficha: " + e.getMessage());
            return false;
        }
    }
}
