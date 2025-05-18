package modelo;

import modelo.enums.Color;
import modelo.enums.FaseJuego;

import java.util.List;

public class Juego {
    private final Tablero tablero;
    private List<Jugador> jugadores;
    int turnoActual;
    private FaseJuego fase;

    public Juego(Tablero tablero) {
        this.tablero = tablero;
        this.turnoActual = 0;
        this.fase = FaseJuego.INICIO;
    }

    public void iniciarJuego(Jugador jugador1, Jugador jugador2) {
        this.jugadores = List.of(jugador1, jugador2);
        this.fase = FaseJuego.COLOCANDO;
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

        if(hayMolino(fila, columna)) {
            //TODO Si se forma un molino, el jugador puede eliminar una ficha del oponente
            //hay que notificar al observer del cotrolador que formó un molino para que pueda eliminar una ficha
        }

        if(jugadores.get(0).contarFichasEnMano() == 0 && jugadores.get(1).contarFichasEnMano() == 0) {
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
        if(!jugador.puedeSaltarFicha() && !tablero.sonAdyacentes(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) {
            throw new IllegalArgumentException("El jugador solo puede mover a una posición adyacente");
        }

        tablero.moverFicha(filaOrigen, columnaOrigen, filaDestino, columnaDestino);

        if(hayMolino(filaDestino, columnaDestino)) {
            //TODO Si se forma un molino, el jugador puede eliminar una ficha del oponente
            //hay que notificar al observer del cotrolador que formó un molino para que pueda eliminar una ficha
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
            //TODO Notificar a los observers que terminó el juego
        }
    }

    public boolean hayMolino(int fila, int columna) {
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

    public Jugador hayGanador() {
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
