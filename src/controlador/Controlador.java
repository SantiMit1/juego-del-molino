package controlador;

import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import modelo.*;
import modelo.enums.EstadoFicha;
import observer.Notificaciones;
import vistas.Vista;

import java.rmi.RemoteException;

public class Controlador implements IControladorRemoto {
    private IJuego juego;
    private final Tablero tablero;
    private Vista vista;

    public Controlador(Juego juego) {
        setModeloRemoto(juego);
        this.tablero = juego.getTablero();
    }

    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) {
        this.juego = (IJuego) modeloRemoto;
    }

    public void setVista(Vista vista) {
        this.vista = vista;
    }

    public void crearJugador(String nombre) {
        Jugador jugador = new Jugador(nombre);
        try {
            juego.agregarJugador(jugador);
            vista.mostrarMensaje("Jugador " + nombre + " creado y agregado al juego.");
        } catch (RemoteException ex) {
            vista.mostrarMensaje("Error al agregar jugador: " + ex.getMessage());
        }
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
    public void actualizar(IObservableRemoto iObservableRemoto, Object notificacion) throws RemoteException {
        if (notificacion instanceof Notificaciones) {
            switch ((Notificaciones) notificacion) {
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
                    break;
            }
        }
    }

//    @Override
//    public void notificar(Notificaciones notificacion) {
//        switch (notificacion) {
//            case IMPRIMIR_TABLERO:
//                imprimirTablero();
//                break;
//            case ESPERA:
//                vista.mostrarMensaje("Esperando otro jugador...");
//                break;
//            case COLOCAR:
//                vista.colocarFicha();
//                break;
//            case MOVER:
//                vista.moverFicha();
//                break;
//            case MOLINO:
//                vista.eliminarFicha();
//                break;
//            case FIN:
//                vista.mostrarMensaje("El ganador es: " + juego.getGanador().getNombre());
//                break;
//        }
//    }
}
