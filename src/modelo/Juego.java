package modelo;

import modelo.enums.Color;
import modelo.enums.FaseJuego;
import observer.Notificaciones;
import observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Juego extends Observable {
    private final Tablero tablero;
    private static final List<Jugador> jugadores = new ArrayList<Jugador>();
    private static int turnoActual;
    private static FaseJuego fase;
    private static Jugador ganador = null;

    public Juego(Tablero tablero) {
        super();
        this.tablero = tablero;
        turnoActual = 0;
        fase = FaseJuego.ESPERA;
    }

    private void cambiarTurno() {
        turnoActual++;
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
        tablero.imprimirTablero();
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
        tablero.imprimirTablero();

        if (hayMolino(fila, columna)) {
            observers.get(turnoActual % 2).notificar(Notificaciones.MOLINO);
        }

        if (jugadores.get(0).contarFichasEnMano() == 0 && jugadores.get(1).contarFichasEnMano() == 0) {
            fase = FaseJuego.MOVIENDO;
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
        tablero.imprimirTablero();

        if (hayMolino(filaDestino, columnaDestino)) {
            observers.get(turnoActual % 2).notificar(Notificaciones.MOLINO);
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

        if (existenFichasOponenteFueraDeMolino(ficha.getColor()) && hayMolino(fila, columna)) {
            throw new IllegalArgumentException("El jugador no puede eliminar una ficha en molino porque hay fichas que no forman un molino");
        }

        tablero.eliminarFicha(fila, columna);
        tablero.imprimirTablero();

        if (hayGanador() != null) {
            notificarObservadores(Notificaciones.FIN);
        }
    }

    private boolean existenFichasOponenteFueraDeMolino(Color colorOponente) {
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                Ficha ficha = tablero.obtenerFicha(fila, columna);
                if (ficha != null && ficha.getColor() == colorOponente) {
                    if (!hayMolino(fila, columna)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hayMolino(int fila, int columna) {
        if (!tablero.posicionValida(fila, columna)) {
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");
        }
        if (!tablero.posicionOcupada(fila, columna)) {
            throw new IllegalStateException("No hay ficha en la posición");
        }

        Ficha ficha = tablero.obtenerFicha(fila, columna);
        Color color = ficha.getColor();

        String pos = fila + "," + columna;
        List<String> adyacentes = tablero.getAdyacencias().get(pos);

        //busca una ficha adyacente del mismo color
        for (String adyacente : adyacentes) {
            String[] partes = adyacente.split(",");
            int filaAdyacente = Integer.parseInt(partes[0]);
            int columnaAdyacente = Integer.parseInt(partes[1]);

            if (tablero.posicionOcupada(filaAdyacente, columnaAdyacente)) {
                Ficha fichaAdyacente = tablero.obtenerFicha(filaAdyacente, columnaAdyacente);
                if (fichaAdyacente.getColor() == color) {

                    //si encuentra una ficha adyacente del mismo color se busca una ficha mas para formar el molino en la misma fila o columna
                    //las coordenadas de esta ficha se obtienen restando o sumando la diferencia entre las filas y columnas de la ficha adyacente
                    int[][] posiblesMolinos = {
                            {filaAdyacente + Math.abs(fila - filaAdyacente), columnaAdyacente + Math.abs(columna - columnaAdyacente)},
                            {filaAdyacente - Math.abs(fila - filaAdyacente), columnaAdyacente - Math.abs(columna - columnaAdyacente)},
                            {fila + Math.abs(fila - filaAdyacente), columna + Math.abs(columna - columnaAdyacente)},
                            {fila - Math.abs(fila - filaAdyacente), columna - Math.abs(columna - columnaAdyacente)}
                    };

                    //se itera sobre las posibles posiciones de la ficha que completa el molino
                    for (int[] posMolino : posiblesMolinos) {
                        int filaPosible = posMolino[0];
                        int columnaPosible = posMolino[1];
                        if (tablero.posicionValida(filaPosible, columnaPosible)) {
                            Ficha posibleMolino = tablero.obtenerFicha(filaPosible, columnaPosible);
                            if (posibleMolino != null && posibleMolino.getColor() == color &&
                                    !ficha.equals(posibleMolino) && !fichaAdyacente.equals(posibleMolino)) {
                                //si encuentra una tercer ficha del mismo color, verifica que no sea la adyacente o la original
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private Jugador hayGanador() {
        for (Jugador jugador : jugadores) {
            if (jugador.contarFichasEnMano() == 0 && jugador.contarFichasEnTablero() < 3) {
                Jugador nuevoGanador = jugadores.get((jugadores.indexOf(jugador) + 1) % 2);
                fase = FaseJuego.FINALIZADO;
                turnoActual = 0;
                tablero.limpiarTablero();
                ganador = nuevoGanador;
                return ganador;
            }
        }
        return null;
    }

    public Jugador getGanador() {
        return ganador;
    }
}
