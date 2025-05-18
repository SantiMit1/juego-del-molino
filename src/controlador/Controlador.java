package controlador;

import modelo.Juego;
import modelo.Jugador;
import modelo.Tablero;
import modelo.enums.EstadoFicha;
import observer.Observer;

public class Controlador {
    private final Juego juego;

    public Controlador() {
        this.juego = new Juego(new Tablero());
    }

    public void agregarObserver(Observer vista) {
        juego.agregarObservador(vista);
    }

    public void agregarJugador(Jugador jugador) {
        juego.agregarJugador(jugador);
    }

    public void eliminarObserver(Observer vista) {
        juego.eliminarObservador(vista);
    }

    public void colocarFicha(int fila, int columna) {
        juego.colocarFicha(fila, columna, juego.getJugadorActual().obtenerFichasPorEstado(EstadoFicha.EN_MANO).getFirst());
    }

    public void moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        juego.moverFicha(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
    }

    public void eliminarFicha(int fila, int columna) {
        juego.eliminarFicha(fila, columna);
    }
}
