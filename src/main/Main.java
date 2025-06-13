package main;

import controlador.Controlador;
import modelo.Juego;
import modelo.Tablero;
import vistas.IVista;
import vistas.terminal.VistaTerminal;

public class Main {
    public static void main(String[] args) {
        Tablero tablero = new Tablero();
        Juego juego = new Juego(tablero);
        Controlador controlador = new Controlador(juego);
        IVista jugador1 = new VistaTerminal(controlador);
        Controlador controlador2 = new Controlador(juego);
        IVista jugador2 = new VistaTerminal(controlador2);
    }
}