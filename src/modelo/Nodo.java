package modelo;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private final int fila;
    private final int columna;
    private Ficha ficha;
    private final List<Nodo> adyacentes;

    public Nodo(int fila, int columna) {
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

    public List<Nodo> getAdyacentes() {
        return adyacentes;
    }

    public void agregarAdyacente(Nodo nodo) {
        if (!adyacentes.contains(nodo)) {
            adyacentes.add(nodo);
        }
    }
}

