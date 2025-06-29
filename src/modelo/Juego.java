package modelo;

import modelo.enums.Color;
import modelo.enums.EstadoFicha;
import modelo.enums.FaseJuego;
import observer.Notificaciones;
import observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Juego extends Observable {
    private final Tablero tablero;
    private static final List<Jugador> jugadores = new ArrayList<>();
    private static int turnoActual;
    private static FaseJuego fase;
    private static Jugador ganador = null;

    public Juego(Tablero tablero) {
        super();
        this.tablero = tablero;
        turnoActual = 0;
        fase = FaseJuego.ESPERA;
        ganador = null;
    }

    private void cambiarTurno() {
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
            observers.get(turnoActual % 2).notificar(Notificaciones.MOVER);
        } else if (fase == FaseJuego.COLOCANDO) {
            observers.get(turnoActual % 2).notificar(Notificaciones.COLOCAR);
        }
    }

    public void agregarJugador(Jugador jugador) {
        if (jugadores.size() >= 2) {
            throw new IllegalStateException("Ya hay dos jugadores en el juego");
        }
        if (jugadores.contains(jugador)) {
            throw new IllegalArgumentException("El jugador ya está en la lista");
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
            observers.getFirst().notificar(Notificaciones.ESPERA);
        }

    }

    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual % 2);
    }

    public void iniciarJuego() {
        fase = FaseJuego.COLOCANDO;
        notificarObservadores(Notificaciones.IMPRIMIR_TABLERO);
        observers.get(turnoActual % 2).notificar(Notificaciones.COLOCAR);
    }

    public void colocarFicha(int fila, int columna, Ficha ficha) {
        if (fase != FaseJuego.COLOCANDO) {
            throw new IllegalStateException("No se puede colocar ficha en esta fase");
        }
        if (!jugadores.get(turnoActual % 2).puedeColocarFicha()) {
            throw new IllegalStateException("No hay fichas en la mano del jugador");
        }
        if (jugadores.get(turnoActual % 2).getColor() != ficha.getColor()) {
            throw new IllegalArgumentException("El jugador debe colocar una ficha de su color");
        }

        tablero.colocarFicha(fila, columna, ficha);

        if (jugadores.get(0).contarFichasEnMano() == 0 && jugadores.get(1).contarFichasEnMano() == 0) {
            fase = FaseJuego.MOVIENDO;
        }

        if (tablero.hayMolino(fila, columna)) {
            notificarObservadores(Notificaciones.IMPRIMIR_TABLERO);
            observers.get(turnoActual % 2).notificar(Notificaciones.MOLINO);
            return;
        }

        cambiarTurno();
    }

    public void moverFicha(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        if (fase != FaseJuego.MOVIENDO) {
            throw new IllegalStateException("No se puede mover fichas en esta fase");
        }
        if (!jugadores.get(turnoActual % 2).puedeMoverFicha()) {
            throw new IllegalStateException("El jugador todavia no puede mover fichas");
        }

        Ficha ficha = tablero.obtenerFicha(filaOrigen, columnaOrigen);
        Jugador jugador = jugadores.get(turnoActual % 2);
        if (jugador.getColor() != ficha.getColor()) {
            throw new IllegalArgumentException("El jugador debe mover una ficha de su color");
        }
        if (!jugador.puedeSaltarFicha() && !tablero.sonAdyacentes(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            throw new IllegalArgumentException("El jugador solo puede mover a una posición adyacente");
        }

        tablero.moverFicha(filaOrigen, columnaOrigen, filaDestino, columnaDestino);

        if (tablero.hayMolino(filaDestino, columnaDestino)) {
            notificarObservadores(Notificaciones.IMPRIMIR_TABLERO);
            observers.get(turnoActual % 2).notificar(Notificaciones.MOLINO);
            return;
        }

        cambiarTurno();
    }

    public void eliminarFicha(int fila, int columna) {
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

    public Jugador getGanador() {
        return ganador;
    }

    public Tablero getTablero() {
        return tablero;
    }
}
