package modelo;

import modelo.enums.ColorFicha;
import modelo.enums.EstadoFicha;

public class Ficha {
    private final ColorFicha color;
    private EstadoFicha estado;
    private int fila, columna;

    public Ficha(ColorFicha color) {
        this.color = color;
        this.estado = EstadoFicha.EN_MANO;
        this.fila = -1;
        this.columna = -1;
    }

    public void eliminarFicha() {
        this.estado = EstadoFicha.ELIMINADA;
        this.fila = -1;
        this.columna = -1;
    }

    public ColorFicha getColor() {
        return color;
    }

    public EstadoFicha getEstado() {
        return estado;
    }

    public void setEstado(EstadoFicha estado) {
        this.estado = estado;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setPocicion(int fila, int columna) {
        if (this.estado == EstadoFicha.ELIMINADA) return;
        this.fila = fila;
        this.columna = columna;
        this.estado = EstadoFicha.EN_TABLERO;
    }
}
