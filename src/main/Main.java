package main;

import controlador.Controlador;
import vistas.VistaTerminal;

public class Main {
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        VistaTerminal jugador1 = new VistaTerminal(controlador);
        VistaTerminal jugador2 = new VistaTerminal(controlador);
    }
}