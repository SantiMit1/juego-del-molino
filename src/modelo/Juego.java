package modelo;

import modelo.enums.Color;
import modelo.enums.FaseJuego;
import observer.Notificaciones;
import observer.Observable;
import observer.Observer;

import java.util.List;

public class Juego extends Observable {
    private final Tablero tablero;
    private static List<Jugador> jugadores;
    private static int turnoActual;
    private static FaseJuego fase;

    public Juego(Tablero tablero) {
        super();
        this.tablero = tablero;
        turnoActual = 0;
        fase = FaseJuego.INICIO;
        notificarObservadores(Notificaciones.INICIO);
    }

    private void cambiarTurno() {
        turnoActual++;
        if(fase == FaseJuego.MOVIENDO) {
            notificarObservador(Notificaciones.MOVER, observers.get(turnoActual % 2));
        } else if(fase == FaseJuego.COLOCANDO) {
            notificarObservador(Notificaciones.COLOCAR, observers.get(turnoActual % 2));
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
        }
    }

    public void iniciarJuego() {
        fase = FaseJuego.COLOCANDO;
        tablero.imprimirTablero();
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
            notificarObservador(Notificaciones.MOLINO, observers.get(turnoActual % 2));
        }

        if (jugadores.get(0).contarFichasEnMano() == 0 && jugadores.get(1).contarFichasEnMano() == 0) {
            fase = FaseJuego.MOVIENDO;
        }
        turnoActual++;
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

        if (hayMolino(filaDestino, columnaDestino)) {
            notificarObservador(Notificaciones.MOLINO, observers.get(turnoActual % 2));
        }

        turnoActual++;
    }

    public void eliminarFicha(int fila, int columna) {
        Ficha ficha = tablero.obtenerFicha(fila, columna);
        if (jugadores.get(turnoActual % 2).getColor() == ficha.getColor()) {
            throw new IllegalArgumentException("El jugador no puede eliminar su propia ficha");
        }

        tablero.eliminarFicha(fila, columna);

        if (hayGanador() != null) {
            notificarObservadores(Notificaciones.FIN);
        }
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
                                    !ficha.equals(posibleMolino) && !adyacente.equals(posibleMolino)) {
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
        Jugador jugador1 = jugadores.get(0);
        Jugador jugador2 = jugadores.get(1);
        if (jugador1.contarFichasEnMano() == 0 && jugador1.contarFichasEnTablero() < 3) {
            fase = FaseJuego.FINALIZADO;
            turnoActual = 0;
            return jugador2;
        } else if (jugador2.contarFichasEnMano() == 0 && jugador2.contarFichasEnTablero() < 3) {
            fase = FaseJuego.FINALIZADO;
            turnoActual = 0;
            return jugador1;
        }

        return null;
    }
}
