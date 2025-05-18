package main;

import modelo.Ficha;
import modelo.Tablero;
import modelo.Juego;
import modelo.enums.Color;

public class Main {
    public static void main(String[] args) {
        Tablero tablero = new Tablero();
        Juego juego = new Juego(tablero);
        Ficha ficha1 = new Ficha(Color.BLANCO);
        Ficha ficha2 = new Ficha(Color.BLANCO);
        Ficha ficha3 = new Ficha(Color.BLANCO);
        Ficha ficha4 = new Ficha(Color.NEGRO);
        Ficha ficha5 = new Ficha(Color.NEGRO);
        Ficha ficha6 = new Ficha(Color.NEGRO);

        tablero.colocarFicha(2,2, ficha1);
        tablero.colocarFicha(2,3, ficha2);
        tablero.colocarFicha(2,4, ficha3);
        tablero.colocarFicha(6,0,ficha4);
        tablero.colocarFicha(3,0,ficha5);
        tablero.colocarFicha(0,0,ficha6);

        tablero.imprimirTablero();
        System.out.println(juego.hayMolino(0,0));

    }
}