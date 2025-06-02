package controlador;

import modelo.Juego;
import modelo.Jugador;
import modelo.Nodo;
import modelo.Tablero;
import modelo.enums.EstadoFicha;
import observer.Notificaciones;
import observer.Observer;
import vistas.IVista;

public class Controlador implements Observer {
    private final Juego juego;
    private final Tablero tablero;
    private IVista vista;

    public Controlador(Juego juego) {
        this.juego = juego;
        this.tablero = juego.getTablero();
        juego.agregarObservador(this);
    }

    public void setVista(IVista vista) {
        this.vista = vista;
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
            vista.mostrarMensaje("Error al colocar ficha: " + e.getMessage());
            return false;
        }
    }

    public boolean moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        try {
            juego.moverFicha(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
            return true;
        } catch (Exception e) {
            vista.mostrarMensaje("Error al mover ficha: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarFicha(int fila, int columna) {
        try {
            juego.eliminarFicha(fila, columna);
            return true;
        } catch (Exception e) {
            vista.mostrarMensaje("Error al eliminar ficha: " + e.getMessage());
            return false;
        }
    }

    public void imprimirTablero() {
        Nodo[][] nodos = tablero.getNodos();
        vista.mostrarTablero(nodos);
    }

    @Override
    public void notificar(Notificaciones notificacion) {
        switch (notificacion) {
            case ESPERA:
                vista.mostrarMensaje("Esperando otro jugador...");
                break;
            case COLOCAR:
                vista.colocarFicha();
                break;
            case MOVER:
                vista.moverFicha();
                break;
            case MOLINO:
                vista.eliminarFicha();
                break;
            case FIN:
                System.out.println("El ganador es: " + juego.getGanador().getNombre());
                break;
        }
    }
}
