package modelo;

import java.util.ArrayList;
import java.util.List;

public class Posicion {
    private final int fila;
    private final int columna;
    private Ficha ficha;
    private final List<Posicion> adyacentes;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.adyacentes = new ArrayList<>();
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public List<Posicion> getAdyacentes() {
        return adyacentes;
    }

    public void agregarAdyacente(Posicion posicion) {
        if (!adyacentes.contains(posicion)) {
            adyacentes.add(posicion);
        }
    }
}

