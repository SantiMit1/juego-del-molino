package main;

import controlador.Controlador;
import vistas.IVista;
import vistas.VistaTerminal;

public class Main {
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        IVista jugador1 = new VistaTerminal(controlador);
        IVista jugador2 = new VistaTerminal(controlador);
    }
}