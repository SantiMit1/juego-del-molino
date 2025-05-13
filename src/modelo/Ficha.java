package modelo;

import modelo.enums.Color;
import modelo.enums.EstadoFicha;

public class Ficha {
    private final Color color;
    private EstadoFicha estado;
    private int fila, columna;

    public Ficha(Color color) {
        this.color = color;
        this.estado = EstadoFicha.EN_MANO;
        this.fila = -1;
        this.columna = -1;
    }

    public void colocarFicha(int fila, int columna) {
        if (this.estado == EstadoFicha.EN_MANO) {
            this.fila = fila;
            this.columna = columna;
            this.estado = EstadoFicha.EN_TABLERO;
        }
    }

    public void moverFicha(int fila, int columna) {
        if (fila < 1 || columna < 1) {
            throw new IllegalArgumentException("Fila y columna deben ser mayores o iguales a 1");
        }

        if(this.estado == EstadoFicha.EN_MANO || this.estado == EstadoFicha.ELIMINADA) {
            throw new IllegalStateException("No se puede mover una ficha que está en la mano o eliminada");
        }

        this.fila = fila;
        this.columna = columna;
    }

    public void eliminarFicha() {
        this.estado = EstadoFicha.ELIMINADA;
        this.fila = -1;
        this.columna = -1;
    }

    public Color getColor() {
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

    public void setPosicion(int fila, int columna) {
        if (this.estado == EstadoFicha.ELIMINADA) return;
        this.fila = fila;
        this.columna = columna;
        this.estado = EstadoFicha.EN_TABLERO;
    }
}
