package modelo;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;

public interface IJuego extends IObservableRemoto {
    void agregarJugador(Jugador jugador) throws RemoteException;

    Jugador getJugadorActual() throws  RemoteException;

    void iniciarJuego() throws RemoteException;

    void colocarFicha(int fila, int columna) throws RemoteException;

    void moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) throws RemoteException;

    void eliminarFicha(int fila, int columna) throws RemoteException;

    Jugador getGanador() throws  RemoteException;

    Tablero getTablero() throws   RemoteException;
}
