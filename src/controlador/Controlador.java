package controlador;

import modelo.Juego;
import modelo.Jugador;
import modelo.Tablero;
import modelo.enums.EstadoFicha;
import observer.Observer;

public class Controlador {
    private final Juego juego;
    private final Jugador jugador;

    public Controlador(Jugador jugador) {
        this.jugador = jugador;
        this.juego = new Juego(new Tablero());
        juego.agregarJugador(jugador);
    }

    public void agregarObserver(Observer vista) {
        juego.agregarObservador(vista);
    }

    public void eliminarObserver(Observer vista) {
        juego.eliminarObservador(vista);
    }

    public void colocarFicha(int fila, int columna) {
        juego.colocarFicha(fila, columna, jugador.obtenerFichasPorEstado(EstadoFicha.EN_MANO).getFirst());
    }

    public void moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        juego.moverFicha(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
    }

    public void eliminarFicha(int fila, int columna) {
        juego.eliminarFicha(fila, columna);
    }
}
