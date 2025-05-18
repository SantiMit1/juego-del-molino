package main;

import controlador.Controlador;
import modelo.Jugador;
import observer.Observer;
import vistas.VistaTerminal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        VistaTerminal jugador1 = new VistaTerminal(controlador);
        VistaTerminal jugador2 = new VistaTerminal(controlador);


    }
}