package controlador;

import modelo.Juego;
import modelo.Jugador;
import modelo.Posicion;
import modelo.Tablero;
import modelo.enums.EstadoFicha;
import observer.Notificaciones;
import observer.Observer;
import vistas.Vista;
public class Controlador implements Observer {
    private final Juego juego;
    private final Tablero tablero;
    private Vista vista;

    public Controlador(Juego juego) {
        this.juego = juego;
        this.tablero = juego.getTablero();
        juego.agregarObservador(this);
    }

    public void setVista(Vista vista) {
        this.vista = vista;
    }

    public void crearJugador(String nombre) {
        Jugador jugador = new Jugador(nombre);
        vista.mostrarMensaje("Jugador " + nombre + " creado y agregado al juego.");
        juego.agregarJugador(jugador);
    }

    private void eliminarObserver() {
        juego.eliminarObservador(this);
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
            vista.mostrarMensaje("asbdaskhgdkasds");
            juego.eliminarFicha(fila, columna);
            return true;
        } catch (Exception e) {
            vista.mostrarMensaje("Error al eliminar ficha: " + e.getMessage());
            return false;
        }
    }

    public void imprimirTablero() {
        Posicion[][] posiciones = tablero.getPosiciones();
        StringBuilder tableroString = new StringBuilder();
        for (Posicion[] fila : posiciones) {
            for (Posicion pos : fila) {
                if (pos != null && tablero.esPosicionValida(pos.getFila(), pos.getColumna())) {
                    if (pos.getFicha() != null) {
                        tableroString.append(pos.getFicha().getColor().toString().charAt(0));
                    } else {
                        tableroString.append("@");
                    }
                }
            }
        }

        vista.mostrarTablero(tableroString.toString());
    }

    @Override
    public void notificar(Notificaciones notificacion) {
        switch (notificacion) {
            case IMPRIMIR_TABLERO:
                imprimirTablero();
                break;
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
                vista.mostrarMensaje("El ganador es: " + juego.getGanador().getNombre());
                eliminarObserver();
                break;
        }
    }
}
