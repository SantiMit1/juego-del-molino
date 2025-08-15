package modelo;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import modelo.enums.Color;
import modelo.enums.EstadoFicha;
import modelo.enums.FaseJuego;
import modelo.enums.Notificaciones;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Juego extends ObservableRemoto implements IJuego {
    private final Tablero tablero;
    private final List<Jugador> jugadores = new ArrayList<>();
    private int turnoActual;
    private FaseJuego fase;
    private Jugador ganador;

    public Juego(Tablero tablero) {
        super();
        this.tablero = tablero;
        this.turnoActual = 0;
        this.fase = FaseJuego.ESPERA;
        this.ganador = null;
    }

    private void cambiarTurno() throws RemoteException {
        // verifica si hay un ganador antes de cambiar el turno
        if (ganador == null) {
            ganador = hayGanador();
            if (ganador != null) {
                notificarObservadores(Notificaciones.IMPRIMIR_TABLERO);
                notificarObservadores(Notificaciones.FIN);
                finalizarJuego();
                return;
            }
        }

        turnoActual++;
        notificarObservadores(Notificaciones.IMPRIMIR_TABLERO);
        if (fase == FaseJuego.MOVIENDO) {
            //observers.get(turnoActual % 2).notificar(Notificaciones.MOVER);
            notificarObservadores(Notificaciones.MOVER);
        } else if (fase == FaseJuego.COLOCANDO) {
            //observers.get(turnoActual % 2).notificar(Notificaciones.COLOCAR);
            notificarObservadores(Notificaciones.COLOCAR);
        }
    }

    @Override
    public void agregarJugador(Jugador jugador) throws RemoteException {
        if (jugadores.size() >= 2) {
            throw new IllegalStateException("Ya hay dos jugadores en el juego");
        }
        if (jugadores.contains(jugador)) {
            throw new IllegalArgumentException("El jugador ya está en la lista");
        }
        for (Jugador j : jugadores) {
            if (j.getNombre().equals(jugador.getNombre())) {
                throw new IllegalArgumentException("Ya existe un jugador con el mismo nombre");
            }
        }

        if (jugadores.isEmpty()) {
            jugador.setColor(Color.BLANCO);
        } else {
            jugador.setColor(Color.NEGRO);
        }

        for (int i = 0; i < 9; i++) {
            jugador.agregarFicha(new Ficha(jugador.getColor()));
        }

        jugadores.add(jugador);

        if (jugadores.size() == 2) {
            iniciarJuego();
        } else {
            notificarObservadores(Notificaciones.ESPERA);
        }

    }

    @Override
    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual % 2);
    }

    @Override
    public void iniciarJuego() throws RemoteException {
        fase = FaseJuego.COLOCANDO;
        notificarObservadores(Notificaciones.IMPRIMIR_TABLERO);
        notificarObservadores(Notificaciones.COLOCAR);
    }

    @Override
    public void colocarFicha(int fila, int columna) throws RemoteException {
        if (fase != FaseJuego.COLOCANDO) {
            throw new IllegalStateException("No se puede colocar ficha en esta fase");
        }

        Jugador jugadorActual = jugadores.get(turnoActual % 2);

        if (!jugadorActual.puedeColocarFicha()) {
            throw new IllegalStateException("No hay fichas en la mano del jugador");
        }

        Ficha fichaAColocar = jugadorActual.obtenerFichasPorEstado(EstadoFicha.EN_MANO).getFirst();

        tablero.colocarFicha(fila, columna, fichaAColocar);

        if (jugadores.get(0).contarFichasEnMano() == 0 && jugadores.get(1).contarFichasEnMano() == 0) {
            fase = FaseJuego.MOVIENDO;
        }

        if (tablero.hayMolino(fila, columna)) {
            notificarObservadores(Notificaciones.IMPRIMIR_TABLERO);
            notificarObservadores(Notificaciones.MOLINO);
            return;
        }

        cambiarTurno();
    }

    @Override
    public void moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) throws RemoteException {
        if (fase != FaseJuego.MOVIENDO) {
            throw new IllegalStateException("No se puede mover fichas en esta fase");
        }

        Jugador jugadorActual = jugadores.get(turnoActual % 2);

        if (!jugadorActual.puedeMoverFicha()) {
            throw new IllegalStateException("El jugador todavia no puede mover fichas");
        }

        Ficha ficha = tablero.obtenerFicha(filaOrigen, columnaOrigen);
        if (ficha == null) {
            throw new IllegalArgumentException("No hay ficha en la posición de origen");
        }

        if (jugadorActual.getColor() != ficha.getColor()) {
            throw new IllegalArgumentException("El jugador debe mover una ficha de su color");
        }

        if (!jugadorActual.puedeSaltarFicha() && !tablero.sonAdyacentes(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            throw new IllegalArgumentException("El jugador solo puede mover a una posición adyacente");
        }

        tablero.moverFicha(filaOrigen, columnaOrigen, filaDestino, columnaDestino);

        if (tablero.hayMolino(filaDestino, columnaDestino)) {
            notificarObservadores(Notificaciones.IMPRIMIR_TABLERO);
            notificarObservadores(Notificaciones.MOLINO);
            return;
        }

        cambiarTurno();
    }

    @Override
    public void eliminarFicha(int fila, int columna) throws RemoteException {
        Ficha ficha = tablero.obtenerFicha(fila, columna);
        if (ficha == null) {
            throw new IllegalArgumentException("No hay ficha en la posición elegida");
        }

        if (jugadores.get(turnoActual % 2).getColor() == ficha.getColor()) {
            throw new IllegalArgumentException("El jugador no puede eliminar su propia ficha");
        }

        if (existenFichasOponenteFueraDeMolino(ficha.getColor()) && tablero.hayMolino(fila, columna)) {
            throw new IllegalArgumentException("El jugador no puede eliminar una ficha en molino porque hay fichas que no forman un molino");
        }

        tablero.eliminarFicha(fila, columna);

        cambiarTurno();
    }

    private boolean existenFichasOponenteFueraDeMolino(Color colorOponente) {
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                Ficha ficha = tablero.obtenerFicha(fila, columna);
                if (ficha != null && ficha.getColor() == colorOponente) {
                    if (!tablero.hayMolino(fila, columna)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Jugador hayGanador() {
        Jugador jugador = jugadores.get((turnoActual + 1) % 2); // el jugador que no jugo en este turno
        // si el jugador tiene menos de 3 fichas pierde
        if (jugador.contarFichasEnTablero() < 3 && jugador.contarFichasEnMano() == 0) {
            // retorna el jugador que no perdio
            return jugadores.get(turnoActual % 2);
        }

        // si el jugador no puede saltar fichas y no tiene movimientos legales pierde
        if (!jugador.puedeSaltarFicha() && fase == FaseJuego.MOVIENDO) {
            for (Posicion[] fila : tablero.getPosiciones()) {
                for (Posicion pos : fila) {
                    if (pos != null) {
                        Ficha ficha = pos.getFicha();
                        if (ficha != null && ficha.getColor() == jugador.getColor() && ficha.getEstado() == EstadoFicha.EN_TABLERO) {
                            for (Posicion adyacente : pos.getAdyacentes()) {
                                if (adyacente.getFicha() == null) {
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
            // retorna el jugador que no perdio
            return jugadores.get(turnoActual % 2);
        }

        return null;
    }

    private void finalizarJuego() {
        fase = FaseJuego.FINALIZADO;
        turnoActual = 0;
        tablero.limpiarTablero();
    }

    @Override
    public Jugador getGanador() {
        return ganador;
    }

    @Override
    public Tablero getTablero() {
        return tablero;
    }
}
